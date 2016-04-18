package org.cdrolet.cdirect.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.dto.EventDetail;
import org.cdrolet.cdirect.dto.EventResult;
import org.cdrolet.cdirect.exception.ProcessException;
import org.cdrolet.cdirect.type.ErrorCode;
import org.cdrolet.cdirect.type.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by c on 4/17/16.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private final SubscriptionService subscriptionService;

    @Autowired
    private final CustomerService customerService;

    @Override
    public EventResult processEvent(EventDetail event) {

        log.info("Event received: " + event);

        List<EventResult> results = getDelegates(event.getType())
                .stream()
                .map(p -> p.processEvent(event))
                .collect(Collectors.toList());

        return results.get(results.size() - 1);
    }

    private Collection<EventProcessor> getDelegates(EventType type) {

        switch (type) {
            case SUBSCRIPTION_ORDER:
                return ImmutableList.of(customerService, subscriptionService);
            case SUBSCRIPTION_CANCEL:
            case SUBSCRIPTION_CHANGE:
            case SUBSCRIPTION_NOTICE:
                return ImmutableList.of(subscriptionService);
            case USER_ASSIGNMENT:
                return ImmutableList.of(customerService, subscriptionService);
            case USER_UNASSIGNMENT:
            case USER_UPDATED:
                return ImmutableList.of(customerService);
            default:
                throw new ProcessException(
                        type + " is not supported",
                        ErrorCode.UNKNOWN_ERROR);
        }

    }
}
