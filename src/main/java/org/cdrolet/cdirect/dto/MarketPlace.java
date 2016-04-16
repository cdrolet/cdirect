package org.cdrolet.cdirect.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MarketPlace implements Serializable {
	private static final long serialVersionUID = 5629970479893675783L;

	private String partner;

	private String baseUrl;

}
