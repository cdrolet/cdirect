package org.cdrolet.cdirect.controller;

import lombok.RequiredArgsConstructor;
import org.cdrolet.cdirect.service.EventService;
import org.cdrolet.cdirect.service.SubscriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * Created by root on 4/10/16.
 */
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class FrontController {


    private final SubscriptionService subscriptionService;

    private final EventService eventService;

    @RequestMapping("/")
    public String welcome(@ModelAttribute("model") ModelMap model) {
        model.addAttribute("subscriberList",
                subscriptionService.getAllSubscriptions());
        model.addAttribute("requestList",
                eventService.getAllRequestLogs());
        return "index";
    }

}
