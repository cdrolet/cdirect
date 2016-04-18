package org.cdrolet.cdirect.oauth;

import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.QueryStringSigningStrategy;
import org.cdrolet.cdirect.dto.EventDetail;
import org.cdrolet.cdirect.exception.UnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by c on 4/17/16.
 */
@Slf4j
public enum OAuthUrlProcessor {

    INSTANCE;

    public EventDetail process(String key, String signature, URL eventUrl) {
        OAuthConsumer consumer = newConsumerFrom(key, signature);

        HttpURLConnection connection = connect(consumer, eventUrl);

        if (!isConnectionSuccessful(connection)) {
            throw new UnauthorizedException("signed connection failed");
        }

        return Utility.fromJson(parseResponse(connection), EventDetail.class);
    }

    private OAuthConsumer newConsumerFrom(String key, String signature) {
        OAuthConsumer consumer = new DefaultOAuthConsumer(
                key,
                signature);

        consumer.setSigningStrategy(new QueryStringSigningStrategy());

        return consumer;
    }

    private String parseResponse(HttpURLConnection connection) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

            return readAllLines(in);

        } catch (IOException ex) {

            throw withLog(new RuntimeException("error occur when parsing event url response", ex));
        }
    }

    private String readAllLines(BufferedReader in) throws IOException {
        StringBuilder response = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        return response.toString();

    }

    private boolean isConnectionSuccessful(HttpURLConnection connection) {
        try {
            return connection.getResponseCode() / 100 == 2;
        } catch (IOException ex) {
            return false;
        }
    }


    private HttpURLConnection connect(OAuthConsumer consumer, URL url) {

        try {

            HttpURLConnection connection = openConnection(url);

            consumer.sign(connection);

            connection.connect();

            return connection;

        } catch (OAuthExpectationFailedException | OAuthMessageSignerException ex) {

            throw withLog(new UnauthorizedException("non authorized url: "
                    + url.toString()));

        } catch (IOException | OAuthCommunicationException ex) {

            throw withLog(new RuntimeException("error occur when opening connection from url "
                    + url.toString()));
        }
    }

    private HttpURLConnection openConnection(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        return connection;
    }

    private <T extends Exception> T withLog(T exception) {

        log.error(exception.getMessage(), exception);

        return exception;
    }
}
