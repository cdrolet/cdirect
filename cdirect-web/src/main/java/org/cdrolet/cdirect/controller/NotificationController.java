package org.cdrolet.cdirect.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.dto.EventDetail;
import org.cdrolet.cdirect.dto.EventResult;
import org.cdrolet.cdirect.exception.ProcessException;
import org.cdrolet.cdirect.exception.UnauthorizedException;
import org.cdrolet.cdirect.service.AuthorizationService;
import org.cdrolet.cdirect.service.SubscriptionService;
import org.cdrolet.cdirect.type.ErrorCode;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.URL;

@RestController
@RequestMapping(value = "/v1", produces = "application/json")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class NotificationController {

    public static final String NOTIFICATION_PATH = "notification";

    private final SubscriptionService subscriptionService;

    private final AuthorizationService authService;

    @RequestMapping(value = "/" + NOTIFICATION_PATH)
    ResponseEntity handleNotificationEvent(
            @RequestParam(value = "eventUrl") URL eventUrl,
            HttpServletRequest request) {

        EventDetail eventDetail = signedFetch(request, eventUrl);

        EventResult result = subscriptionService.processEvent(eventDetail);

        log.info("returning result {}", result);

        return ResponseEntity
                .accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    private EventDetail signedFetch(HttpServletRequest request, URL eventUrl) {

        String authHeaders = request.getHeader(authService.getAuthorizationId());

        return authService.process(authHeaders, eventUrl);
    }

    @ExceptionHandler(ProcessException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public EventResult handleBusinessException(ProcessException ex) {

        EventResult result = ex.getErrorCode().toResult(ex.getMessage());

        log.info("returning process error {} ", result);

        return result;
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public EventResult handleUnauthorizedException(UnauthorizedException ex) {

        EventResult result = ErrorCode.FORBIDDEN.toResult(ex.getMessage());

        log.info("returning unauthorized error {} ", result);

        return result;
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public EventResult handleSqlException(DataAccessException ex) {

        EventResult result = ErrorCode.UNKNOWN_ERROR.toResult(ex.getMessage());

        log.info("returning process error {} ", result);

        return result;
    }



}
