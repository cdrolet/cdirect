package org.cdrolet.cdirect.domain;

import lombok.*;

/**
 * Created by c on 4/10/16.
 */
@Data
public class RequestLog {

    @RequiredArgsConstructor
    public enum Status {
        OPENING("warning"),
        PROCESSING("warning"),
        COMPLETED(""),
        REJECTED("danger");

        private final String displayClass;
    }

    private final Long timestamp;

    private final EventType type;

    private Status status = Status.OPENING;

    private String message = "";

    private EventDetail event;

    public String getDisplayClass() {
        return status.displayClass;
    }
}
