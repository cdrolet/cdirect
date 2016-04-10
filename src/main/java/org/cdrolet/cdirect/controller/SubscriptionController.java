package org.cdrolet.cdirect.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by root on 4/10/16.
 */
@Controller
public class SubscriptionController {

    @Value("${application.message:Hello World}")
    private String message = "Welcome";

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("message", this.message);
        return "index";
    }

}
