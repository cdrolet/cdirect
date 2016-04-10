package org.cdrolet.cdirect.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
public class Company implements Serializable {
	private static final long serialVersionUID = -3689138943301029315L;

	private String country;
	private String email;
	private String name;
	private String phoneNumber;
	private String uuid;
	private String website;
}
