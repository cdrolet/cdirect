package org.cdrolet.cdirect.controller;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.signature.QueryStringSigningStrategy;
import org.cdrolet.cdirect.domain.ErrorCode;
import org.cdrolet.cdirect.domain.EventDetail;
import org.cdrolet.cdirect.domain.EventResult;
import org.cdrolet.cdirect.domain.Subscriber;
import org.cdrolet.cdirect.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

/**
 * Created by root on 4/10/16.
 */
@RestController
@RequestMapping(value = "/v1", produces = "application/json")
@Slf4j
public class EventController {

    //TODO to be moved in the EventService
    @Autowired
    private SubscriptionService subService;

    @RequestMapping(value = "/subscription/create/notification")
    ResponseEntity createSubscribe(
            @RequestParam(value = "eventUrl", required = false) URL eventUrl,
            @RequestParam(value = "token", required = false) String token,
            HttpServletRequest request) {

        Collections.list(request.getHeaderNames())
                .forEach(s -> log.info(s + " -> " + request.getHeader(s)));
        log.info(" ->> query  :" + request.getQueryString());
        log.info(" ->> url    :" + request.getRequestURL());

        Map<String, String> oAuthHeader = Splitter.on(",")
                .omitEmptyStrings()
                .trimResults()
                .withKeyValueSeparator("=")
                .split(request.getHeader("authorization").replaceFirst("OAuth ", ""));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>" + oAuthHeader);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!" + oAuthHeader.get("oauth_consumer_key"));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@" + oAuthHeader.get("oauth_signature"));

        OAuthConsumer consumer = new DefaultOAuthConsumer(
                oAuthHeader.get("oauth_consumer_key"),
                oAuthHeader.get("oauth_signature"));

        consumer.setSigningStrategy(new QueryStringSigningStrategy());

        StringBuffer response = new StringBuffer();
        try {
            HttpURLConnection redirect = (HttpURLConnection) eventUrl.openConnection();

            redirect.setRequestProperty("accept", "application/json");

            consumer.sign(redirect);

            redirect.connect();

            //TODO check status returned when not authorized
            if (redirect.getResponseCode() != 200) {
                return ResponseEntity
                        .status(401)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(ErrorCode.INVALID_RESPONSE.toResult());

            }

            System.out.println("!!!!!! Response: " + redirect.getResponseCode() +
                    redirect.getResponseMessage());

            try (BufferedReader in = new BufferedReader(new InputStreamReader(redirect.getInputStream()))) {
                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                //print result
                System.out.println("=======> " + response.toString());
            }
        } catch (Exception ex) {
            log.error("error occur ", ex);
        }

        EventDetail event = new Gson().fromJson(response.toString(), EventDetail.class);

        if (subService.isSubscriptionExist(event.getCreator().getEmail())) {
            System.out.println("!!!!! " + ErrorCode.USER_ALREADY_EXISTS.toResult());

            return ResponseEntity
                    .status(409)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ErrorCode.USER_ALREADY_EXISTS.toResult());
        }

        Subscriber sub = new Subscriber();
        sub.setEmail(event.getCreator().getEmail());
        sub.setFirstName(event.getCreator().getFirstName());
        sub.setLastName(event.getCreator().getLastName());
        sub.setEditionCode(event.getPayload().getOrder().getEditionCode());
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
