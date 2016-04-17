package org.cdrolet.cdirect.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Data
public class EventResult implements Serializable {
    private static final long serialVersionUID = -7599199539526987847L;

    private boolean success;
    private String accountIdentifier;
    private String errorCode;
    private String message;
}
