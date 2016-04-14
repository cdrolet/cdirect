package org.cdrolet.cdirect.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.signature.QueryStringSigningStrategy;
import org.cdrolet.cdirect.domain.*;
import org.cdrolet.cdirect.request.RequestUtil;
import org.cdrolet.cdirect.service.EventAuthorizationService;
import org.cdrolet.cdirect.service.NotificationLogService;
import org.cdrolet.cdirect.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

import static org.cdrolet.cdirect.request.NotificationRequest.*;


/**
 * Created by root on 4/10/16.
 */
@RestController
@RequestMapping(value = "/v1", produces = "application/json")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class NotificationController {


    //TODO to be moved in the EventService
    private final SubscriptionService subService;

    private final NotificationLogService notificationLogService;

    private final EventAuthorizationService authService;

    @RequestMapping(value = "/subscription/create/" + NOTIFICATION_PATH)
    ResponseEntity createSubscribe(
            @RequestParam(value = "eventUrl") URL eventUrl,
            @RequestParam(value = "token") String token,
            HttpServletRequest request) {


        String authHeaders = AuthHeader.normalizeHeaders(request.getHeader(AUTHORIZATION_HEADERS));

        Optional<EventDetail> eventDetail = authService.authorizeUrl(
                RequestUtil.valuesToMap(authHeaders),
                eventUrl);

        if (!eventDetail.isPresent()) {
            return ResponseEntity
                    .status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorCode.FORBIDDEN.toResult());
        }

        if (subService.isSubscriptionExist(eventDetail.get().getCreator().getEmail())) {

            requestLog.setStatus(NotificationLog.State.FAILED);
            requestLog.setMessage(ErrorCode.USER_ALREADY_EXISTS.getMessage());
            eventService.addRequestLog(requestLog);

            return ResponseEntity
                    .status(409)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorCode.USER_ALREADY_EXISTS.toResult());
        }

        Subscriber sub = Subscriber.builder()
                .email(event.getCreator().getEmail())
                .firstName(event.getCreator().getFirstName())
                .lastName(event.getCreator().getLastName())
                .editionCode(event.getPayload().getOrder().getEditionCode())
                .build();

        subService.addSubscription(sub);

        //401 or 403
        return ResponseEntity
                .accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body(EventResult
                        .builder()
                        .accountIdentifier(String.valueOf(sub.hashCode()))
                        .success(true)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, String> handleValidationException(ValidationException ex) {
        log.error(ex.getMessage(), ex);
        return ex.toMap();
    }

  /*   @RequestMapping(value = "/login")
    ResponseEntity login(@RequestParam URL openIdUrl, HttpServletRequest request) {
        Collections.list(request.getHeaderNames())
                .forEach(s -> log.info(s + " -** " + request.getHeader(s)));
        log.info(" -** query  :" + request.getQueryString());
        log.info(" -** url    :" + request.getRequestURL());

        OAuthConsumer consumer = new DefaultOAuthConsumer("Dummy", "secret");
        URL url = new URL("https://www.appdirect.com/api/events/dummyChange");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        consumer.sign(request);
        request.connect();

    }
*/


    /*
    Subscription Create Notification URL

https://cdirect.herokuapp.com/subscription/create/notification

This is an interactive endpoint
 Collect During Checkout
Subscription Change Notification URL

https://cdirect.herokuapp.com/subscription/change/notification

Additional product settings required during checkout
Subscription Cancel Notification URL

https://cdirect.herokuapp.com/subscription/cancel/notification
Subscription Status Notification URL

https://cdirect.herokuapp.com/subscription/status/notification

Enable upcoming invoice notification

cdirect-103155
OAuth Consumer Secret
MLnebYpj6xwNIhZj

*/

/*
@RequiredArgsConstructor(onConstructor = @__(@Inject))
    @RequestMapping(method = RequestMethod.POST, value = "/**")
    public ResponseEntity<?> defaultExecute(HttpServletRequest request) {

        repository.executeTests(request.getRequestURI());
        return new ResponseEntity<>(
                null,
                buildLocationHeader("/status"),
                HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/**")
    public ResponseEntity<?> defaultAbort(HttpServletRequest request) {

        repository.abortTests(request.getRequestURI());
        return new ResponseEntity<>(
                null,
                buildLocationHeader("/status"),
                HttpStatus.OK);

    }

    private HttpHeaders buildLocationHeader(String locationPath) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(locationPath)
                .build(true)
                .toUri());

        return httpHeaders;
    }
*/
/*
    private ReportType getReportTypeFrom(String contentType) {
        switch (contentType) {
            case MediaType.APPLICATION_JSON_VALUE:
                return ReportType.JSON;
            case MediaType.TEXT_HTML_VALUE:
                return ReportType.HTML;
            case MediaType.APPLICATION_XML_VALUE:
                return ReportType.XML;
            case "text/csv":
                return ReportType.CSV;
            default:
                throw new InvalidContentTypeException();

        }
    }
*/
/*
    @ExceptionHandler(InvalidStoryResource.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String[] handleCustomized4Exception(InvalidStoryResource ex) {
        return new String[]{ex.getMessage()};
    }

    @ExceptionHandler(InvalidContentTypeException.class)
    @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public String[] handleCustomized4Exception(InvalidContentTypeException ex) {
        return new String[]{ex.getMessage()};
    }

    private static class InvalidContentTypeException extends RuntimeException {
    }

*/


}
