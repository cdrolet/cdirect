package org.cdrolet.cdirect.service;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.converter.EventToSubscription;
import org.cdrolet.cdirect.domain.*;
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

        switch (event.getType()) {
            case SUBSCRIPTION_ORDER:
                addSubscription(event);
                break;
            case SUBSCRIPTION_CHANGE:
                updateSubscription(event);
                break;
            case SUBSCRIPTION_CANCEL:
                removeSubscription(event);
                break;
            case SUBSCRIPTION_NOTICE:
                evaluateSubscription(event);
                break;
        }
        return null;
    }

    private void addSubscription(EventDetail event) {

        Subscription subscription = Subscription.builder()
                .accountIdentifier(String.valueOf(event.hashCode()))
                .isActive(true)
                .build();

        writeSubscription(subscription, event);

    }

    private void updateSubscription(EventDetail event) {

        Subscription previous = loadPreviousSubscription(event);

        writeSubscription(previous, event);

    }

    private void removeSubscription(EventDetail event) {

        Subscription previous = loadPreviousSubscription(event);

        inventory.remove(previous.getAccountIdentifier());
    }

    private Subscription loadPreviousSubscription(EventDetail event) {

        Account account = event.getPayload().getAccount();

        checkNotNull(account, "missing account");

        Subscription subscription = inventory.get(account.getAccountIdentifier());

        checkNotNull(subscription, "missing account identifier");

        return subscription;
    }

    private void evaluateSubscription(EventDetail event) {

        Subscription previous = loadPreviousSubscription(event);

        Notice notice = event.getPayload().getNotice();

        checkNotNull(notice, "missing notice");

        if (notice.getType().equals(NoticeType.CLOSED)) {
            inventory.remove(previous.getAccountIdentifier());
        } else {
            writeSubscription(previous, event);
        }
    }

    private void writeSubscription(Subscription subscription, EventDetail event) {

        subscription = EventToSubscription.INSTANCE.apply(subscription, event);

        inventory.put(subscription.getAccountIdentifier(), subscription);

    }


    private <T> void checkNotNull(T target, String message) {

        if (target == null) {
            log.error(message);
            throw new ProcessException(message, ErrorCode.UNKNOWN_ERROR);
        }
    }

}
