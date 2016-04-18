package org.cdrolet.cdirect.service;

import org.cdrolet.cdirect.dto.User;
import org.cdrolet.cdirect.entity.Customer;

import java.util.Collection;

/**
 * Created by c on 4/17/16.
 */
public interface CustomerService extends EventProcessor {

    Collection<User> getAll();

    Customer getCustomer(String id);
}
