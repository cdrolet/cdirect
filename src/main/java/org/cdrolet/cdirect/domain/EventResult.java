package org.cdrolet.cdirect.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventResult implements Serializable {
    private static final long serialVersionUID = -7599199539526987847L;

    private boolean success;
    private String accountIdentifier;
}
