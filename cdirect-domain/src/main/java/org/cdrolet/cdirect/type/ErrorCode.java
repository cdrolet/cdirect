package org.cdrolet.cdirect.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cdrolet.cdirect.dto.EventResult;

/**
 * Created by root on 4/10/16.
 */
@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    USER_ALREADY_EXISTS("user already existing on CDirect"),
    USER_NOT_FOUND("the user is not currently subscribed to CDirect"),
    ACCOUNT_NOT_FOUND("the account didn't exist"),
    INVALID_RESPONSE("An unexpected error occur when processing the event"),
    TRANSPORT_ERROR("An unexpected error occur when processing the event"),
    UNKNOWN_ERROR("An unexpected error occur"),
    FORBIDDEN("The operation is not allowed.");

    private final String message;

    public EventResult toResult() {

        return toResult(message);
    }

    public EventResult toResult(String text) {

        return EventResult.builder()
                .message(text)
                .errorCode(this.name())
                .success(false)
                .build();
    }
}
