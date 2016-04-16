package org.cdrolet.cdirect.request;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.Optional;

/**
 * Created by c on 4/12/16.
 */
@UtilityClass
public final class NotificationRequest {


    public static final String NOTIFICATION_PATH = "notification";

    public static final String AUTHORIZATION_HEADERS = "authorization";

    @RequiredArgsConstructor
    @Getter
    public enum AuthHeader implements RequestField {
        AUTH_TIMESTAMP("oauth_timestamp"),
        AUTH_KEY("oauth_consumer_key"),
        AUTH_SIGNATURE("oauth_signature");

        private final String field;

        @Override
        public Optional<String> from(String authHeaders) {
            String header = RequestUtil.valuesToMap(normalizeHeaders(authHeaders)).get(field);

            if (Strings.isNullOrEmpty(header)) {
                return Optional.empty();
            }

            return Optional.of(header);
        }

        public static String normalizeHeaders(String headers) {
            return headers.replaceFirst("OAuth ", "")
                    .replaceAll("\"", "");
        }
    }

    @RequiredArgsConstructor
    @Getter
    public enum QueryParam implements RequestField {
 //       TOKEN("token"),
        EVENT_URL("eventUrl");

        private final String field;
    }


}
