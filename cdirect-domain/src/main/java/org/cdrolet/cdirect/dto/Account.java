package org.cdrolet.cdirect.dto;

import lombok.Data;

/**
 * Created by root on 4/10/16.
 */
@Data
public class Account {
    private static final long serialVersionUID = 3080925569209286979L;


    private String accountIdentifier;

    private AccountStatus status;


}
