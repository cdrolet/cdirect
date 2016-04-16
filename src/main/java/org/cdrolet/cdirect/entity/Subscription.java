package org.cdrolet.cdirect.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.cdrolet.cdirect.dto.PricingDuration;

/**
 * Created by c on 4/15/16.
 */
@Builder
@Data
public class Subscription {

    private String accountIdentifier;

    private boolean active;

    private String editionCode;

    private PricingDuration pricingDuration;
}
