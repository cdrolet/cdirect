package org.cdrolet.cdirect.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by root on 4/10/16.
 */
@Data
public class Subscriber implements Serializable {

    private String email;
    private String firstName;
    private String lastName;
    private String editionCode;

}
