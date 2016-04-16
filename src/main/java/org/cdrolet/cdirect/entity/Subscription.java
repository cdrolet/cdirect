package org.cdrolet.cdirect.entity;

import lombok.Builder;
import lombok.Getter;
import org.cdrolet.cdirect.domain.PricingDuration;

/**
 * Created by c on 4/15/16.
 */
@Builder
@Getter
public class Subscription {

    private String accountIdentifier;

    private boolean isActive;

    private String editionCode;

    private PricingDuration pricingDuration;
}
