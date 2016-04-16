package org.cdrolet.cdirect.service;

import org.cdrolet.cdirect.dto.EventDetail;

import java.net.URL;
import java.util.Map;

/**
 * Created by c on 4/13/16.
 */
public interface AuthorizationService {

    EventDetail authorizeUrl(Map<String, String> authorizationInfo, URL eventUrl);
}
