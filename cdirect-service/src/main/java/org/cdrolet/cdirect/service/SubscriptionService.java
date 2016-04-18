package org.cdrolet.cdirect.service;

import org.cdrolet.cdirect.dto.EventDetail;
import org.cdrolet.cdirect.dto.EventResult;
import org.cdrolet.cdirect.dto.Subscriber;
import org.cdrolet.cdirect.entity.Subscription;

import java.util.Collection;

/**
 * Created by c on 4/14/16.
 */
public interface SubscriptionService extends EventProcessor {

    Collection<Subscriber> getAll();

}
