package org.cdrolet.cdirect.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;

/**
 * Created by root on 4/10/16.
 */
@Builder
@Getter
public class Subscriber{

    @NonNull
    private final String email;
    @NonNull
    private final String firstName;
    @NonNull
    private final String lastName;
    @NonNull
    private final String editionCode;

}
