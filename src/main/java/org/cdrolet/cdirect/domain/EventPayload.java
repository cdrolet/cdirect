package org.cdrolet.cdirect.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

@Getter @Setter @NoArgsConstructor
public class EventPayload implements Serializable {
	private static final long serialVersionUID = 3080925569209286979L;

	private EventCreator user;
	private Company company;
	private Order order;
	private HashMap<String, String> configuration = new HashMap<>();
}
