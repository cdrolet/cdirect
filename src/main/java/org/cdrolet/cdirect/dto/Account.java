package org.cdrolet.cdirect.dto;

import lombok.Data;

/**
 * Created by root on 4/10/16.
 */
@Data
public class Account {

    private String accountIdentifier;

    private AccountStatus status;


}