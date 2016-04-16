package org.cdrolet.cdirect.dto;

import lombok.Data;

import java.io.Serializable;
import org.cdrolet.cdirect.request.RequestUtil;

@Data
public class EventDetail implements Serializable {
    private static final long serialVersionUID = 2658400228024450854L;

    private EventType type;
    private MarketPlace marketplace;
    private EventFlag flag;
    private EventCreator creator;
    private EventPayload payload;
    private String returnUrl;


    public static EventDetail fromJson(String json) {
        return RequestUtil.fromJson(json, EventDetail.class);
    }
}
