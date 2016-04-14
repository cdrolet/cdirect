package org.cdrolet.cdirect.domain;

import lombok.*;

/**
 * Created by c on 4/10/16.
 */
@Getter
@Builder
public class NotificationLog {

    public enum State {
        RECEIVED,
        AUTHORIZED,
        PROCESSED,
        COMPLETED;
    }

    public enum Status {
        OK,
        FAILURE
    }

    public static NotificationLog failed(String errorMessage) {
        return NotificationLog.builder()
                .timestamp(System.currentTimeMillis())
                .type(EventType.UNKNOWN)
                .state(State.RECEIVED)
                .status(Status.FAILURE)
                .message(errorMessage)
                .build();
    }

    public static NotificationLog.NotificationLogBuilder from(NotificationLog log) {
        return builder()
                .message(log.getMessage())
                .status(log.status)
                .state(log.state)
                .timestamp(log.timestamp)
                .type(log.type);
    }


    @NonNull
    private final Long timestamp;

    @NonNull
    private final EventType type;

    @NonNull
    private final State state;

    @NonNull
    private final Status status;

    @NonNull
    private final String message;

}
