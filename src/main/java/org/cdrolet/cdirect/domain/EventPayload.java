package org.cdrolet.cdirect.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
