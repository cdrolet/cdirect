package org.cdrolet.cdirect.dto;

import com.google.common.collect.Lists;
import lombok.Data;
import org.cdrolet.cdirect.type.PricingDuration;

import java.io.Serializable;
import java.util.List;

@Data
public class Order implements Serializable {
    private static final long serialVersionUID = -6727466858109325508L;

    private String editionCode;
    private PricingDuration pricingDuration;
    private List<Item> items = Lists.newArrayList();

}
