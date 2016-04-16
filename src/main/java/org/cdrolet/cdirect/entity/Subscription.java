package org.cdrolet.cdirect.entity;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by c on 4/15/16.
 */
@Builder
@Getter
public class Subscription {

    private String accountIdentifier;

    private boolean active;

    private String editionCode;

    private String pricingDuration;
}
