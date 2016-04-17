package org.cdrolet.cdirect.dto;


import lombok.Data;
import org.cdrolet.cdirect.type.UsageUnit;

import java.io.Serializable;

@Data
public class Item implements Serializable {
    private static final long serialVersionUID = 1850642171706386994L;

    private UsageUnit unit;
    private int quantity;

}
