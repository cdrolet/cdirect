package org.cdrolet.cdirect.service;

import org.cdrolet.cdirect.dto.EventDetail;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

/**
 * Created by c on 4/13/16.
 */
public interface AuthorizationService {

    String getAuthorizationId();

    Optional<String> getKey(String authContent);

    Optional<String> getSignature(String authContent);

    EventDetail process(String authContent, URL eventUrl);
}
