package org.cdrolet.cdirect.converter;

import org.cdrolet.cdirect.domain.*;
import org.cdrolet.cdirect.entity.Subscription;
import org.cdrolet.cdirect.exception.ProcessException;

import java.util.function.BiFunction;

/**
 * Created by c on 4/15/16.
 */
public enum EventToSubscription implements BiFunction<Subscription, EventDetail, Subscription> {

    INSTANCE;

    @Override
    public Subscription apply(Subscription previous, EventDetail eventDetail) {

        Subscription.SubscriptionBuilder builder = Subscription.builder();

        Account account = eventDetail.getPayload().getAccount();
        if (account != null) {
            builder.accountIdentifier(account.getAccountIdentifier());
            builder.isActive(account.getStatus().isActive());
        } else {
            builder.accountIdentifier(previous.getAccountIdentifier());
            builder.isActive(previous.isActive());
        }

        Order order = eventDetail.getPayload().getOrder();
        if (order != null) {
            builder.pricingDuration(order.getPricingDuration());
            builder.editionCode(order.getEditionCode());
        } else {
            builder.pricingDuration(previous.getPricingDuration());
            builder.editionCode(previous.getEditionCode());
        }

        Notice notice = eventDetail.getPayload().getNotice();
        if (notice != null) {
            switch(notice.getType()) {
                case DEACTIVATED:
                    builder.isActive(false);
                    break;
                case REACTIVATED:
                    builder.isActive(true);
                    break;
                case CLOSED:
                case UPCOMING_INVOICE:
                    break;
                default:
                    throw new ProcessException("unknown notice type" + notice.getType(), ErrorCode.UNKNOWN_ERROR);
            }

        }

        return builder.build();
    }
}
