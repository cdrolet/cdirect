package org.cdrolet.cdirect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;


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

    @RequestMapping(value = "/subscription/create/notification")
    ResponseEntity createSubscribe(@RequestParam URL url) {

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
