package org.cdrolet.cdirect.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.dto.ErrorCode;
import org.cdrolet.cdirect.dto.EventDetail;
import org.cdrolet.cdirect.dto.EventResult;
import org.cdrolet.cdirect.exception.ProcessException;
import org.cdrolet.cdirect.exception.UnauthorizedException;
import org.cdrolet.cdirect.request.RequestUtil;
import org.cdrolet.cdirect.service.AuthorizationService;
import org.cdrolet.cdirect.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.URL;

import static org.cdrolet.cdirect.request.NotificationRequest.*;

@RestController
@RequestMapping(value = "/v1", produces = "application/json")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SubscriptionController {


    private final SubscriptionService notificationService;

    private final AuthorizationService authService;

    @RequestMapping(value = "/subscription/ "+ NOTIFICATION_PATH)
    ResponseEntity handleSubscriptionEvent(
            @RequestParam(value = "eventUrl") URL eventUrl,
            HttpServletRequest request) {

        EventDetail eventDetail = signedFetch(request, eventUrl);

        EventResult result = notificationService.processEvent(eventDetail);

        return ResponseEntity
                .accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    private EventDetail signedFetch(HttpServletRequest request, URL eventUrl) {

        String authHeaders = AuthHeader.normalizeHeaders(request.getHeader(AUTHORIZATION_HEADERS));

        return authService.authorizeUrl(RequestUtil.valuesToMap(authHeaders), eventUrl);
    }

    @ExceptionHandler(ProcessException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public EventResult handleBusinessException(ProcessException ex) {

        return ex.getErrorCode().toResult();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public EventResult handleUnauthorizedException(UnauthorizedException ex) {

        return ErrorCode.FORBIDDEN.toResult();
    }

}
