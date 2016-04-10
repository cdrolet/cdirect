package org.cdrolet.cdirect.service;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.domain.Subscriber;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * Created by cdrolet on 4/10/16.
 */
@Service
@Slf4j
public class DynamoSubscriptionService implements SubscriptionService {

    private Map<String, Subscriber> subscriptions = Maps.newHashMap();

    @Override
    public boolean isSubscriptionExist(String email) {
        return subscriptions.containsKey(email);
    }

    @Override
    public void addSubscription(Subscriber subscriber) {
        log.info("Adding new subscriber: " + subscriber);
        subscriptions.put(subscriber.getEmail(), subscriber);
    }

    @Override
    public void removeSubscription(String email) {
        subscriptions.remove(email);
    }

    @Override
    public Collection<Subscriber> getAllSubscriptions() {
        return subscriptions.values();
    }
}
