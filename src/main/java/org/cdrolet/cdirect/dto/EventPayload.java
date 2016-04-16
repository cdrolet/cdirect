package org.cdrolet.cdirect.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class EventPayload implements Serializable {
	private static final long serialVersionUID = 3080925569209286979L;

	private Company company;
	private Account account;
	private Order order;
	private Notice notice;
	private HashMap<String, String> configuration = new HashMap<>();
}
