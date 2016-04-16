package org.cdrolet.cdirect.service;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.converter.EventToSubscription;
import org.cdrolet.cdirect.converter.SubscriptionToResult;
import org.cdrolet.cdirect.dto.*;
import org.cdrolet.cdirect.entity.Subscription;
import org.cdrolet.cdirect.exception.ProcessException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * Created by cdrolet on 4/10/16.
 */

@Slf4j
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final Map<String, Subscription> inventory = Maps.newHashMap();

    @Override
    public Collection<Subscription> getAll() {
        return inventory.values();
    }

    @Override
    public EventResult processEvent(EventDetail event) {

        Subscription subscription = getSubscription(event);
        return SubscriptionToResult.INSTANCE.apply(subscription);
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
            default: throw new ProcessException("unknown event type: " + event.getType(), ErrorCode.UNKNOWN_ERROR);
        }

    }

    private Subscription addSubscription(EventDetail event) {

        Subscription subscription = Subscription.builder()
                .accountIdentifier(String.valueOf(event.hashCode()))
                .isActive(true)
                .build();

        return writeSubscription(subscription, event);

    }

    private Subscription updateSubscription(EventDetail event) {

        Subscription previous = loadPreviousSubscription(event);

        return writeSubscription(previous, event);

    }

    private Subscription removeSubscription(EventDetail event) {

        Subscription previous = loadPreviousSubscription(event);

        return inventory.remove(previous.getAccountIdentifier());
    }

    private Subscription loadPreviousSubscription(EventDetail event) {

        Account account = event.getPayload().getAccount();

        checkNotNull(account, "missing account in payload");

        Subscription subscription = inventory.get(account.getAccountIdentifier());

        checkNotNull(subscription, "account identifier: " + account.getAccountIdentifier() + " not found");

        return subscription;
    }

    private Subscription evaluateSubscription(EventDetail event) {

        Subscription previous = loadPreviousSubscription(event);

        Notice notice = null;//event.getPayload().getNotice();

        checkNotNull(notice, "missing notice");

        if (notice.getType().equals(NoticeType.CLOSED)) {
            return inventory.remove(previous.getAccountIdentifier());
        }

        return writeSubscription(previous, event);
    }

    private Subscription writeSubscription(Subscription subscription, EventDetail event) {

        subscription = EventToSubscription.INSTANCE.apply(subscription, event);

        inventory.put(subscription.getAccountIdentifier(), subscription);

        return subscription;
    }

    private <T> void checkNotNull(T target, String message) {

        if (target == null) {
            log.error(message);
            throw new ProcessException(message, ErrorCode.UNKNOWN_ERROR);
        }
    }

}
