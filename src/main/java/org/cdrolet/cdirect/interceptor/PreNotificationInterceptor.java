package org.cdrolet.cdirect.interceptor;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.domain.ErrorCode;
import org.cdrolet.cdirect.domain.EventResult;
import org.cdrolet.cdirect.domain.NotificationLog;
import org.cdrolet.cdirect.service.NotificationLogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.cdrolet.cdirect.request.NotificationRequest.*;

/**
 * Created by c on 4/12/16.
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PreNotificationInterceptor extends HandlerInterceptorAdapter implements NotificationInterceptor {

    private final NotificationLogService notificationLogService;

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

        if (!checkQueryParamsAreValid(request, response)) {
            return false;
        }

        if (!checkIfAuthorized(request, response)) {
            return false;
        }

        addSuccessfulRequestLog(request);

        return true;
    }

    private void addSuccessfulRequestLog(HttpServletRequest request) {
        String timestamp = AuthHeader.AUTH_TIMESTAMP.from(request.getHeader(AUTHORIZATION_HEADERS)).get();

        notificationLogService.addRequestLog(NotificationLog.builder()
                .timestamp(Long.valueOf(timestamp))
                .state(NotificationLog.State.RECEIVED)
                .status(NotificationLog.Status.OK)
                .message("pre handling the request")
                .build());
    }

    private boolean checkAuthHeaderAreValid(HttpServletRequest request, HttpServletResponse response) {

        String headers = request.getHeader(AUTHORIZATION_HEADERS);
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

        for (AuthHeader header : AuthHeader.values()) {
            if (!header.from(headers).isPresent()) {
                handleBadRequest(response, header.getErrorMessage());
                return false;
            }
        }

        return true;
    }

    private boolean checkQueryParamsAreValid(HttpServletRequest request, HttpServletResponse response) {

        String params = request.getQueryString();

        for (QueryParam param : QueryParam.values()) {
            if (!param.from(params).isPresent()) {
                handleBadRequest(response, param.getErrorMessage());
                return false;
            }
        }

        return true;
    }

    private boolean checkIfAuthorized(HttpServletRequest request, HttpServletResponse response) {

        String submittedKey = AuthHeader.AUTH_KEY.from(request.getHeader(AUTHORIZATION_HEADERS)).get();

        if (submittedKey.equals(key)) {
            handleUnauthorizedRequest(response, "wrong key");
            return false;
        }

        return true;
    }

    private void handleBadRequest(HttpServletResponse response, String errorMessage) {

        log.warn(errorMessage);

        notificationLogService.addRequestLog(NotificationLog.failed(errorMessage));

        response.setStatus(400);
        response.setContentType("application/json");
        try {
            new Gson().toJson(
                    EventResult
                            .builder()
                            .errorCode(ErrorCode.FORBIDDEN.name())
                            .success(false)
                            .message(errorMessage)
                            .build(),
                    response.getWriter());
        } catch (IOException ex) {
            log.error("error occur when handling invalid request headers", ex);
        }

    }

    private void handleUnauthorizedRequest(HttpServletResponse response, String errorMessage) {

        log.warn(errorMessage);

        notificationLogService.addRequestLog(NotificationLog.failed(errorMessage));

        response.setStatus(403);
        response.setContentType("application/json");
        try {
            new Gson().toJson(
                    EventResult
                            .builder()
                            .errorCode(ErrorCode.FORBIDDEN.name())
                            .success(false)
                            .message(errorMessage)
                            .build(),
                    response.getWriter());
        } catch (IOException ex) {
            log.error("error occur when handling unauthorized request", ex);
        }

    }
}
