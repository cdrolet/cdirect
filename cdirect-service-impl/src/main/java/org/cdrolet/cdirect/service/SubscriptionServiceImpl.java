package org.cdrolet.cdirect.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.converter.EventToSubscription;
import org.cdrolet.cdirect.converter.SubscriptionToResult;
import org.cdrolet.cdirect.dto.Account;
import org.cdrolet.cdirect.dto.EventDetail;
import org.cdrolet.cdirect.dto.EventResult;
import org.cdrolet.cdirect.dto.Notice;
import org.cdrolet.cdirect.entity.Subscription;
import org.cdrolet.cdirect.exception.ProcessException;
import org.cdrolet.cdirect.repository.SubscriptionRepository;
import org.cdrolet.cdirect.type.ErrorCode;
import org.cdrolet.cdirect.type.EventType;
import org.cdrolet.cdirect.type.NoticeType;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Created by cdrolet on 4/10/16.
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository repository;

    @Override
    public EventResult processEvent(EventDetail event) {

        log.info("Event received: " + event);

        Subscription subscription = getSubscription(event);
        return SubscriptionToResult.INSTANCE.apply(subscription);
    }

    @Override
    public Collection<Subscription> getAll() {
        return Lists.newArrayList(repository.findAll());
    }


    private Subscription getSubscription(EventDetail event) {
        switch (event.getType()) {
            case SUBSCRIPTION_ORDER:
                return updateSubscription(event);
            case SUBSCRIPTION_CHANGE:
                return updateSubscription(event);
            case SUBSCRIPTION_CANCEL:
                return removeSubscription(event);
            case SUBSCRIPTION_NOTICE:
                return evaluateSubscription(event);
            default:
                throw new ProcessException("unknown event type: " + event.getType(), ErrorCode.UNKNOWN_ERROR);
        }

    }

    private Subscription updateSubscription(EventDetail event) {

        Subscription subscription = EventToSubscription.INSTANCE.apply(event);

        if (event.getType().equals(EventType.SUBSCRIPTION_ORDER)) {
            subscription.setActive(true);
        }

        return repository.save(subscription);

    }

    private Subscription removeSubscription(EventDetail event) {

        Subscription previous = loadPreviousSubscription(event);

        repository.delete(getId(event));

        return previous;
    }

    private Subscription loadPreviousSubscription(EventDetail event) {

        String id = getId(event);

        Subscription subscription = repository.findOne(getId(event));

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
