package org.cdrolet.cdirect.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.QueryStringSigningStrategy;
import org.cdrolet.cdirect.dto.EventDetail;
import org.cdrolet.cdirect.exception.UnauthorizedException;
import org.cdrolet.cdirect.request.RequestUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static org.cdrolet.cdirect.request.NotificationRequest.AuthHeader.AUTH_KEY;
import static org.cdrolet.cdirect.request.NotificationRequest.AuthHeader.AUTH_SIGNATURE;

/**
 * Created by c on 4/13/16.
 */
@Service
@Slf4j
public class OAuthService implements AuthorizationService {


    @Override
    public EventDetail authorizeUrl(Map<String, String> authorizationInfo, URL eventUrl) {
        OAuthConsumer consumer = newConsumerFrom(
                authorizationInfo.get(AUTH_KEY.getField()),
                authorizationInfo.get(AUTH_SIGNATURE.getField()));

        HttpURLConnection connection = connect(consumer, eventUrl);

        if (!isConnectionSuccessful(connection)) {
            throw new UnauthorizedException("signed connection failed");
        }

        System.out.println(">>>> connection successful");

        String response = parseResponse(connection);

        System.out.println(">>>> response: " + response);

        Gson gson = new Gson();
        EventDetail detail = gson.fromJson(response, EventDetail.class);

        System.out.println("======> " + detail);
        return detail;
    }

    private String parseResponse(HttpURLConnection connection) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return response.toString();

        } catch (IOException ex) {
            String message = "error occur when parsing event url response";
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }
    }

    private boolean isConnectionSuccessful(HttpURLConnection connection) {
        try {
            return connection.getResponseCode() / 100 == 2;
        } catch (IOException ex) {
            return false;
        }
    }

    private OAuthConsumer newConsumerFrom(String key, String signature) {

        System.out.println(">>>> key: " + key);
        System.out.println(">>>> signature: " + signature);

        OAuthConsumer consumer = new DefaultOAuthConsumer(
                key,
                signature);

        consumer.setSigningStrategy(new QueryStringSigningStrategy());
        return consumer;
    }

    private HttpURLConnection connect(OAuthConsumer consumer, URL url) {

        try {

            HttpURLConnection connection = openConnection(url);

            consumer.sign(connection);

            connection.connect();

            System.out.println(">>>> connection done");

            return connection;

        } catch (OAuthExpectationFailedException | OAuthMessageSignerException  ex) {
            String message = "non authorized url: " + url.toString();
            log.error(message, ex);
            throw new UnauthorizedException(message);
        } catch (IOException | OAuthCommunicationException ex) {
            String message = "error occur when opening connection from url " + url.toString();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }
    }

    private HttpURLConnection openConnection(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        return connection;
    }

}
