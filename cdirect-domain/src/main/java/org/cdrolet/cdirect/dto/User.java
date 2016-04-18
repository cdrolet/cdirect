package org.cdrolet.cdirect.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Created by c on 4/17/16.
 */
@Data
@Builder
public class User {

    private String uuid;

    private String email;

    private String lastName;

}
