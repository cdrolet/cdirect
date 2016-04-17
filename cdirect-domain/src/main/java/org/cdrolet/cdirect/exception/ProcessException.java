package org.cdrolet.cdirect.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cdrolet.cdirect.dto.ErrorCode;

/**
 * Created by c on 4/14/16.
 */
@RequiredArgsConstructor
@Getter
public class ProcessException extends RuntimeException {

    private final ErrorCode errorCode;

    public ProcessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
