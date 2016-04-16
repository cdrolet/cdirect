package org.cdrolet.cdirect.controller;

import lombok.RequiredArgsConstructor;
import org.cdrolet.cdirect.converter.SubscriptionToSubscriber;
import org.cdrolet.cdirect.service.SubscriptionServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.stream.Collectors;

/**
 * Created by root on 4/10/16.
 */
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FrontController {


    private final SubscriptionServiceImpl subscriptionService;

    @RequestMapping("/")
    public String welcome(@ModelAttribute("model") ModelMap model) {

        model.addAttribute("subscriberList",
                subscriptionService.getAll()
                        .stream()
                        .map(SubscriptionToSubscriber.INSTANCE)
                        .collect(Collectors.toList()));

        return "index";
    }

}
