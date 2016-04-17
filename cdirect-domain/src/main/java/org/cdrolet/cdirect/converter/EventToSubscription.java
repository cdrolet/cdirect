package org.cdrolet.cdirect.converter;

import com.google.common.base.Strings;
import org.cdrolet.cdirect.dto.*;
import org.cdrolet.cdirect.entity.Subscription;
import org.cdrolet.cdirect.exception.ProcessException;
import org.cdrolet.cdirect.type.ErrorCode;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by c on 4/15/16.
 */
public enum EventToSubscription implements Function<EventDetail, Subscription> {

    INSTANCE;

    @Override
    public Subscription apply(EventDetail eventDetail) {

        Subscription subscription = new Subscription();

        if (eventDetail.getPayload() == null) {
            throw new ProcessException("no payload in event", ErrorCode.UNKNOWN_ERROR);
        }

        convertAccount(eventDetail, subscription);

        convertOrder(eventDetail, subscription);

        convertNotice(eventDetail, subscription);

        return subscription;
    }

    private void convertAccount(EventDetail eventDetail, Subscription subscription) {

        Account account = eventDetail.getPayload().getAccount();

        if (account == null) {
            return;
        }

        if (account.getStatus() != null) {
            subscription.setActive(account.getStatus().isActive());
        }

        if (account.getAccountIdentifier() != null) {
            subscription.setId(account.getAccountIdentifier());
        }
    }

    private void convertOrder(EventDetail eventDetail, Subscription subscription) {

        Order order = eventDetail.getPayload().getOrder();

        if (order == null) {
            return;
        }

        if (order.getPricingDuration() != null) {
            subscription.setPricingDuration(order.getPricingDuration().name());
        }

        if (order.getEditionCode() != null) {
            subscription.setEditionCode(order.getEditionCode());
        }
    }

    private void convertNotice(EventDetail eventDetail, Subscription subscription) {

        Notice notice = eventDetail.getPayload().getNotice();
        if (notice != null) {
            switch(notice.getType()) {
                case DEACTIVATED:
                    subscription.setActive(false);
                    break;
                case REACTIVATED:
                    subscription.setActive(true);
                    break;
                case CLOSED:
                case UPCOMING_INVOICE:
                    break;
                default:
                    throw new ProcessException("unknown notice type" + notice.getType(), ErrorCode.UNKNOWN_ERROR);
            }

        }
    }
}
