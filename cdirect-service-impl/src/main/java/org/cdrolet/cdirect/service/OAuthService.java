package org.cdrolet.cdirect.service;

import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.dto.EventDetail;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Optional;

/**
 * Created by c on 4/13/16.
 */
@Service
@Slf4j
public class OAuthService implements AuthorizationService {

    @Override
    public String getAuthorizationId() {
        return OAuthField.AUTHORIZATION_ID;
    }

    @Override
    public Optional<String> getKey(String authContent) {
        return OAuthField.AUTH_KEY.from(authContent);
    }

    @Override
    public Optional<String> getSignature(String authContent) {
        return OAuthField.AUTH_SIGNATURE.from(authContent);
    }

    @Override
    public EventDetail process(String authContent, URL eventUrl) {

        return OAuthUrlProcessor.INSTANCE.process(
                getKey(authContent).get(),
                getSignature(authContent).get(),
                eventUrl);
    }

}
