package org.cdrolet.cdirect;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.signature.QueryStringSigningStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;


@RestController
@SpringBootApplication
@RequestMapping(value = "/v1", produces = "application/json")
@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    @RequestMapping(produces = "text/plain")
    String getHello() {

        return "Hello";
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

        try {

            consumer.setSigningStrategy(new QueryStringSigningStrategy());

            HttpURLConnection redirect = (HttpURLConnection) eventUrl.openConnection();
            //consumer.sign(redirect);

            redirect.connect();
            System.out.println("!!!!!! Response: " + redirect.getResponseCode() + " - "
                    + redirect.getResponseMessage() + redirect.getContent());
        } catch (Exception ex) {
            log.error("error occur ",ex);
        }

        //401 or 403
        return ResponseEntity.accepted().build();
    }

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
