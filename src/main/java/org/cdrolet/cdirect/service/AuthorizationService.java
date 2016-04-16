package org.cdrolet.cdirect.service;

import org.cdrolet.cdirect.domain.EventDetail;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

/**
 * Created by c on 4/13/16.
 */
public interface AuthorizationService {

    EventDetail authorizeUrl(Map<String, String> authorizationInfo, URL eventUrl);
}
