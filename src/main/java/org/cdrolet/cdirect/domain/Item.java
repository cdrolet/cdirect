package org.cdrolet.cdirect.domain;


import lombok.Data;

import java.io.Serializable;

@Data
public class Item implements Serializable {
    private static final long serialVersionUID = 1850642171706386994L;

    private UsageUnit unit;
    private int quantity;

}
