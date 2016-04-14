package org.cdrolet.cdirect.request;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.util.Map;
import java.util.Optional;

/**
 * Created by c on 4/13/16.
 */
public interface RequestField {

    String getField();

    default Optional<String> from(String data) {
        String value = RequestUtil.valuesToMap(data).get(getField());
        if (Strings.isNullOrEmpty(value)) {
            return Optional.empty();
        }

        return Optional.of(value);
    }

    default String getErrorMessage() {
        return "No " + getField() + " found in request";
    }

}
