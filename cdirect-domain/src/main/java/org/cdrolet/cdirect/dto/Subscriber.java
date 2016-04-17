package org.cdrolet.cdirect.dto;

import lombok.Builder;
import lombok.Getter;
import org.cdrolet.cdirect.type.PricingDuration;

/**
 * Created by c on 4/16/16.
 */
@Getter
@Builder
public class Subscriber {

    private String accountIdentifier;

    private String active;

    private String editionCode;

    private PricingDuration pricingDuration;

}
