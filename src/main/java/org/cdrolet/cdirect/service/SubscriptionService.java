package org.cdrolet.cdirect.service;

import org.cdrolet.cdirect.domain.Subscriber;

import java.util.Collection;

/**
 * Created by root on 4/10/16.
 */
public interface SubscriptionService {

    boolean isSubscriptionExist(String email);

    void addSubscription(Subscriber subscriber);

    void removeSubscription(String email);

    Collection<Subscriber> getAllSubscriptions();

}
