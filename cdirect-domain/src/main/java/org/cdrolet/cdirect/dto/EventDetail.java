package org.cdrolet.cdirect.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EventDetail implements Serializable {
    private static final long serialVersionUID = 2658400228024450854L;

    private EventType type;
    private MarketPlace marketplace;
    private EventFlag flag;
    private EventCreator creator;
    private EventPayload payload;
    private String returnUrl;

}
