package org.cdrolet.cdirect.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventCreator implements Serializable {
    private static final long serialVersionUID = 2543707612093688306L;

    private String email;
    private String firstName;
    private String lastName;
    private String language;
    private String openId;
    private String uuid;
}
