package org.cdrolet.cdirect.oauth;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

/**
 * Created by c on 4/17/16.
 */
@RequiredArgsConstructor
@Getter
enum OAuthField {
    AUTH_TIMESTAMP("oauth_timestamp"),
    AUTH_KEY("oauth_consumer_key"),
    AUTH_SIGNATURE("oauth_signature");

    static final String AUTHORIZATION_ID = "authorization";

    private final String field;


    public Optional<String> from(String content) {
        String header = toMap(content).get(field);

        if (Strings.isNullOrEmpty(header)) {
            return Optional.empty();
        }

        return Optional.of(header);
    }


    public static Map<String, String> toMap(String authContent) {
        return Utility.valuesToMap(normalizeContent(authContent));
    }

    private static String normalizeContent(String authContent) {
        return authContent.replaceFirst("OAuth ", "")
                .replaceAll("\"", "");
    }
}



