package org.cdrolet.cdirect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.converter.EventToCustomer;
import org.cdrolet.cdirect.converter.EventToSubscription;
import org.cdrolet.cdirect.converter.SubscriptionToResult;
import org.cdrolet.cdirect.converter.SubscriptionToSubscriber;
import org.cdrolet.cdirect.dto.*;
import org.cdrolet.cdirect.entity.Customer;
import org.cdrolet.cdirect.entity.Subscription;
import org.cdrolet.cdirect.exception.ProcessException;
import org.cdrolet.cdirect.repository.CustomerRepository;
import org.cdrolet.cdirect.repository.SubscriptionRepository;
import org.cdrolet.cdirect.type.ErrorCode;
import org.cdrolet.cdirect.type.NoticeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by cdrolet on 4/10/16.
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepo;
    private final CustomerRepository customerRepo;

    @Override
    public EventResult processEvent(EventDetail event) {
        Subscription subscription = getSubscription(event);
        return SubscriptionToResult.INSTANCE.apply(subscription);
    }

    @Override
    public Collection<Subscriber> getAll() {

        Pageable pageable = new PageRequest(0, 10, Sort.Direction.DESC, "id");

        Page<Subscription> page = subscriptionRepo.findAll(pageable);

        return page.getContent().stream()
                .map(SubscriptionToSubscriber.INSTANCE)
                .collect(Collectors.toList());
    }


    private Subscription getSubscription(EventDetail event) {
        switch (event.getType()) {
            case SUBSCRIPTION_ORDER:
                return addSubscription(event);
            case SUBSCRIPTION_CHANGE:
                return updateSubscription(event);
            case SUBSCRIPTION_CANCEL:
                return removeSubscription(event);
            case SUBSCRIPTION_NOTICE:
                return evaluateSubscription(event);
            case USER_ASSIGNMENT:
                return assignUser(event);
            default:
                throw new ProcessException("unknown event type: " + event.getType(), ErrorCode.UNKNOWN_ERROR);
        }

    }

    private Subscription addSubscription(EventDetail event) {

        Subscription subscription = EventToSubscription.INSTANCE.apply(event);
        subscription.setActive(true);

        log.info("saving subscription: " + subscription.toString());

        String customerId = EventToCustomer.INSTANCE.apply(event).getId();
        subscription.addCustomer(customerRepo.findOne(customerId));

        return subscriptionRepo.save(subscription);

    }

    private Subscription assignUser(EventDetail event) {

        Subscription subscription = loadPreviousSubscription(event);

        Customer customer = EventToCustomer.INSTANCE.apply(event);

        subscription.addCustomer(customer);

        return subscription;
    }

    @Transactional
    private Subscription updateSubscription(EventDetail event) {

        Subscription previous = loadPreviousSubscription(event);
        Subscription subscription = Subscription.from(
                previous,
                EventToSubscription.INSTANCE.apply(event));

        return subscriptionRepo.save(subscription);

    }

    @Transactional
    private Subscription removeSubscription(EventDetail event) {

        Subscription previous = loadPreviousSubscription(event);

        subscriptionRepo.delete(getId(event));

        return previous;
    }

    private Subscription loadPreviousSubscription(EventDetail event) {

        String id = getId(event);

        Subscription subscription = subscriptionRepo.findOne(getId(event));

        checkNotNull(subscription, ErrorCode.ACCOUNT_NOT_FOUND, "account identifier: " + id + " not found");

        return subscription;
    }

    private String getId(EventDetail event) {

        checkNotNull(event.getPayload(), ErrorCode.UNKNOWN_ERROR, "missing payload");

        Account account = event.getPayload().getAccount();

        checkNotNull(account, ErrorCode.UNKNOWN_ERROR, "missing account in payload");

        checkNotNull(account.getAccountIdentifier(), ErrorCode.UNKNOWN_ERROR, "missing account identifier");

        return account.getAccountIdentifier();
    }

    private Subscription evaluateSubscription(EventDetail event) {

        Notice notice = event.getPayload().getNotice();

        checkNotNull(notice, ErrorCode.UNKNOWN_ERROR, "missing notice");

        if (notice.getType().equals(NoticeType.CLOSED)) {
            return removeSubscription(event);
        }

        return updateSubscription(event);
    }

    private <T> void checkNotNull(T target, ErrorCode code, String message) {

        if (target == null) {
            log.error(message);
            throw new ProcessException(message, code);
        }
    }

}
