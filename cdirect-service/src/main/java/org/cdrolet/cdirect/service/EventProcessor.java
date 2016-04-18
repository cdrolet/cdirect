package org.cdrolet.cdirect.service;

import org.cdrolet.cdirect.dto.EventDetail;
import org.cdrolet.cdirect.dto.EventResult;

/**
 * Created by c on 4/17/16.
 */
public interface EventProcessor {

    EventResult processEvent(EventDetail event);
}
