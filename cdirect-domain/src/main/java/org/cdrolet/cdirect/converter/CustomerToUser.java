package org.cdrolet.cdirect.converter;

import org.cdrolet.cdirect.dto.User;
import org.cdrolet.cdirect.entity.Customer;

import java.util.function.Function;

/**
 * Created by c on 4/16/16.
 */
public enum CustomerToUser implements Function<Customer, User> {

    INSTANCE;

    @Override
    public User apply(Customer customer) {
        return User.builder()
                .email(customer.getEmail().trim())
                .uuid(customer.getId())
                .lastName(customer.getLastName().trim())
                .build();
    }

}
