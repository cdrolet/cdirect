package org.cdrolet.cdirect.service.oauth;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * Created by c on 4/13/16.
 */
@UtilityClass
public class Utility {

    public static final Gson JsonParser = new Gson();

    public static <T> T fromJson(String json, Class<T> classOfT) {

        try {
            return JsonParser.fromJson(json, classOfT);
        } catch (Exception ex) {
            System.out.println(">>>>>> " + ex);
            return null;
        }

    }

    public static Map<String, String> valuesToMap(String requestData) {
        return Splitter.on(",")
                .omitEmptyStrings()
                .trimResults()
                .withKeyValueSeparator("=")
                .split(requestData);
    }

}
