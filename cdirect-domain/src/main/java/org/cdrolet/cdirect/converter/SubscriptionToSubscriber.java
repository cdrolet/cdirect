package org.cdrolet.cdirect.converter;

import org.cdrolet.cdirect.dto.Subscriber;
import org.cdrolet.cdirect.entity.Subscription;
import org.cdrolet.cdirect.type.PricingDuration;

import java.util.function.Function;

/**
 * Created by c on 4/16/16.
 */
public enum SubscriptionToSubscriber implements Function<Subscription, Subscriber> {
    INSTANCE;

    @Override
    public Subscriber apply(Subscription subscription) {
        return Subscriber.builder()
                .accountIdentifier(subscription.getId())
                .active(subscription.getActive() ? "Yes" : "No")
                .pricingDuration(PricingDuration.valueOf(subscription.getPricingDuration()))
                .editionCode(subscription.getEditionCode())
                .build();
    }

}
