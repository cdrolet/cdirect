package org.cdrolet.cdirect.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.converter.*;
import org.cdrolet.cdirect.dto.*;
import org.cdrolet.cdirect.entity.Customer;
import org.cdrolet.cdirect.entity.Subscription;
import org.cdrolet.cdirect.exception.ProcessException;
import org.cdrolet.cdirect.repository.CustomerRepository;
import org.cdrolet.cdirect.repository.SubscriptionRepository;
import org.cdrolet.cdirect.type.ErrorCode;
import org.cdrolet.cdirect.type.NoticeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by cdrolet on 4/10/16.
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public EventResult processEvent(EventDetail event) {

        processCustomer(event);

        return EventResult.builder().success(true).build();
    }

    @Override
    public Collection<User> getAll() {

        Pageable pageable = new PageRequest(0, 10, Sort.Direction.DESC, "id");

        Page<Customer> page = repository.findAll(pageable);

        return page.getContent().stream()
                .map(CustomerToUser.INSTANCE)
                .collect(Collectors.toList());
    }


    private Customer processCustomer(EventDetail event) {
        switch (event.getType()) {
            case SUBSCRIPTION_ORDER:
            case USER_ASSIGNMENT:
            case USER_UPDATED:
                return save(event);
            case USER_UNASSIGNMENT:
                return delete(event);
            default:
                throw new ProcessException("unknown event type: " + event.getType(), ErrorCode.UNKNOWN_ERROR);
        }

    }

    private Customer save(EventDetail event) {

        Customer customer = EventToCustomer.INSTANCE.apply(event);
        return repository.save(customer);

    }

    private Customer delete(EventDetail event) {

        Customer customer = repository.findOne(getUserId(event));

        if (customer.getDefaultUser()) {
            throw new ProcessException("can't unassign the default user", ErrorCode.FORBIDDEN);
        }

        customer.getSubscription().getCustomers().remove(customer);

        customer.setSubscription(null);

        repository.delete(customer);

        return customer;

    }

    private String getUserId(EventDetail event) {

        checkNotNull(event.getPayload(), ErrorCode.UNKNOWN_ERROR, "missing payload");

        User user = event.getPayload().getUser();

        checkNotNull(user, ErrorCode.UNKNOWN_ERROR, "missing user in payload");

        checkNotNull(user.getUuid(), ErrorCode.UNKNOWN_ERROR, "missing user uuid");

        return user.getUuid();
    }

    private <T> void checkNotNull(T target, ErrorCode code, String message) {

        if (target == null) {
            log.error(message);
            throw new ProcessException(message, code);
        }
    }

}
