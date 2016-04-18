package org.cdrolet.cdirect.converter;

import org.cdrolet.cdirect.dto.*;
import org.cdrolet.cdirect.entity.Customer;
import org.cdrolet.cdirect.entity.Subscription;
import org.cdrolet.cdirect.exception.ProcessException;
import org.cdrolet.cdirect.type.ErrorCode;
import org.cdrolet.cdirect.type.EventType;

import java.util.function.Function;

/**
 * Created by c on 4/15/16.
 */
public enum EventToCustomer implements Function<EventDetail, Customer> {

    INSTANCE;

    @Override
    public Customer apply(EventDetail eventDetail) {

        if (eventDetail.getPayload() == null) {
            throw new ProcessException("no payload in event", ErrorCode.UNKNOWN_ERROR);
        }

        if (eventDetail.getType().equals(EventType.SUBSCRIPTION_ORDER)) {
            return convertCreator(eventDetail);
        }

        return convertUser(eventDetail);
    }

    private Customer convertUser(EventDetail eventDetail) {

        Customer customer = new Customer();

        User user = eventDetail.getPayload().getUser();

        if (user == null) {
            throw new ProcessException("no user found in event", ErrorCode.UNKNOWN_ERROR);
        }

        customer.setDefaultUser(false);
        customer.setEmail(user.getEmail());
        customer.setLastName(user.getLastName());
        customer.setId(user.getUuid());

        return customer;
    }

    private Customer convertCreator(EventDetail eventDetail) {

        Customer customer = new Customer();

        EventCreator creator = eventDetail.getCreator();

        if (creator == null) {
            throw new ProcessException("no creator found in event", ErrorCode.UNKNOWN_ERROR);
        }

        customer.setDefaultUser(true);
        customer.setEmail(creator.getEmail());
        customer.setLastName(creator.getLastName());
        customer.setId(creator.getUuid());

        return customer;
    }
}
