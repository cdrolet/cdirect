package org.cdrolet.cdirect.dto;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.assertj.core.api.Assertions.assertThat;

public class EventDetailTest {

    private final Gson gson = new Gson();

    //@Test
    public void eventDetail_can_be_parsed_from_subscription_order() throws Exception {

        EventDetail detail = gson.fromJson(readerOf("./target/test-classes/subscriptionOrder.json"), EventDetail.class);

        assertThat(detail).isNotNull();
    }

    private JsonReader readerOf(String fileName) throws FileNotFoundException {
        return new JsonReader(new FileReader(fileName));
    }
}
