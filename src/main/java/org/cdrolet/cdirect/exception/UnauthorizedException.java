package org.cdrolet.cdirect.exception;

import lombok.Getter;

/**
 * Created by c on 4/14/16.
 */
@Getter
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
