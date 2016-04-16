package org.cdrolet.cdirect.converter;

import org.cdrolet.cdirect.dto.EventResult;
import org.cdrolet.cdirect.entity.Subscription;

import java.util.function.Function;

/**
 * Created by c on 4/16/16.
 */
public enum SubscriptionToResult implements Function<Subscription, EventResult> {

    INSTANCE;

    @Override
    public EventResult apply(Subscription subscription) {

        return EventResult.builder()
                .accountIdentifier(subscription.getAccountIdentifier())
                .success(true)
                .build();
    }

}
