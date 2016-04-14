package org.cdrolet.cdirect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.signature.QueryStringSigningStrategy;
import org.cdrolet.cdirect.domain.EventDetail;
import org.cdrolet.cdirect.domain.NotificationLog;
import org.cdrolet.cdirect.request.RequestUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

import static org.cdrolet.cdirect.domain.NotificationLog.State;
import static org.cdrolet.cdirect.domain.NotificationLog.Status;
import static org.cdrolet.cdirect.request.NotificationRequest.AuthHeader.*;

/**
 * Created by c on 4/13/16.
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EventOAuthService implements EventAuthorizationService {


    private final NotificationLogService logService;

    @Override
    public Optional<EventDetail> authorizeUrl(Map<String, String> authorizationInfo, URL eventUrl) {


        OAuthConsumer consumer = newConsumerFrom(
                authorizationInfo.get(AUTH_KEY.getField()),
                authorizationInfo.get(AUTH_SIGNATURE.getField()));

        Optional<HttpURLConnection> connection = connect(consumer, eventUrl);

        if (!isConnectionSuccessful(connection)) {
            addFailureLog(authorizationInfo.get(AUTH_TIMESTAMP.getField()), "signed connection failed");
            return Optional.empty();
        }

        Optional<String> response = parseResponse(connection.get());
        if (!response.isPresent()) {
            addFailureLog(authorizationInfo.get(AUTH_TIMESTAMP.getField()), "unable to parse signed response");
            return Optional.empty();
        }

        return Optional.of(RequestUtil.fromJson(response.get(), EventDetail.class));

    }

    private Optional<String> parseResponse(HttpURLConnection connection) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return Optional.of(response.toString());

        } catch (IOException ex) {
            log.error("error occur when parsing response", ex);
            return Optional.empty();
        }

    }

    private boolean isConnectionSuccessful(Optional<HttpURLConnection> connection) {
        try {
            return connection.isPresent() && (connection.get().getResponseCode() / 100 == 2);
        } catch (IOException ex) {
            log.error("unable to read response code from signed connection", ex);
            return false;
        }
    }

    private void addFailureLog(String timestamp, String message) {
        NotificationLog previousLog = logService.getRequestLog(Long.valueOf(timestamp));
        logService.addRequestLog(NotificationLog.from(previousLog)
                .state(State.AUTHORIZED)
                .status(Status.FAILURE)
                .message(message)
                .build());
    }

    private OAuthConsumer newConsumerFrom(String key, String signature) {
        OAuthConsumer consumer = new DefaultOAuthConsumer(
                key,
                signature);

        consumer.setSigningStrategy(new QueryStringSigningStrategy());
        return consumer;
    }

    private Optional<HttpURLConnection> connect(OAuthConsumer consumer, URL url) {

        try {

            HttpURLConnection connection = openConnection(url);

            consumer.sign(connection);

            connection.connect();

            return Optional.of(connection);

        } catch (Exception ex) {

            log.error("fail to sign connection from url " + url.toString(), ex);

            return Optional.empty();

        }
    }

    private HttpURLConnection openConnection(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        return connection;
    }

}
