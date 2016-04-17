package org.cdrolet.cdirect.interceptor;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.dto.EventResult;
import org.cdrolet.cdirect.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.cdrolet.cdirect.controller.NotificationController.NOTIFICATION_PATH;

/**
 * Created by c on 4/12/16.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Component
public class NotificationValidationInterceptor extends HandlerInterceptorAdapter {

    private final AuthorizationService authService;

    private final Gson parser;

    @Value("${auth.key}")
    private String key;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!isNotificationApiRequest(request)) {
            return true;
        }

        if (!checkAuthHeaderAreValid(request, response)) {
            return false;
        }

        if (!checkIfAuthorized(request, response)) {
            return false;
        }

        return true;
    }

    boolean isNotificationApiRequest(HttpServletRequest request) {
        return request.getRequestURL().toString().endsWith(NOTIFICATION_PATH);
    }

    private boolean checkAuthHeaderAreValid(HttpServletRequest request, HttpServletResponse response) {

        String headers = getAuthHeaders(request);
        if (Strings.isNullOrEmpty(headers)) {
            handleBadRequest(response, "No oauth header found in request header");
            return false;
        }

        if (!checkAuthHeader(headers, response)) {
            return false;
        }

        return true;
    }

    private boolean checkAuthHeader(String headers, HttpServletResponse response) {

        if (!authService.getSignature(headers).isPresent()) {
            return false;
        }

        if (!authService.getKey(headers).isPresent()) {
            return false;
        }

        return true;
    }

    private boolean checkIfAuthorized(HttpServletRequest request, HttpServletResponse response) {

        String submittedKey = authService.getKey(getAuthHeaders(request)).get();

        if (!submittedKey.equals(key)) {
            handleUnauthorizedRequest(response, "invalid signature: " + submittedKey);
            return false;
        }

        return true;
    }

    private String getAuthHeaders(HttpServletRequest request) {
        return request.getHeader(authService.getAuthorizationId());
    }

    private void handleBadRequest(HttpServletResponse response, String errorMessage) {

        log.warn(errorMessage);

        response.setStatus(400);
        response.setContentType("application/json");
        try {
            parser.toJson(
                    EventResult
                            .builder()
                            .message(errorMessage)
                            .build(),
                    response.getWriter());
        } catch (IOException ex) {
            log.error("error occur when handling invalid request headers", ex);
        }

    }

    private void handleUnauthorizedRequest(HttpServletResponse response, String errorMessage) {

        log.warn(errorMessage);

        response.setStatus(401);
        response.setContentType("application/json");
        try {
            parser.toJson(
                    EventResult
                            .builder()
                            .message(errorMessage)
                            .build(),
                    response.getWriter());
        } catch (IOException ex) {
            log.error("error occur when handling unauthorized request", ex);
        }

    }
}
