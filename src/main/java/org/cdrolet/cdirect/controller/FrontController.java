package org.cdrolet.cdirect.controller;

import lombok.RequiredArgsConstructor;
import org.cdrolet.cdirect.service.EventService;
import org.cdrolet.cdirect.service.NotificationLogService;
import org.cdrolet.cdirect.service.SubscriptionService;
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


    private final SubscriptionService subscriptionService;

    private final NotificationLogService requestLogService;

    @RequestMapping("/")
    public String welcome(@ModelAttribute("model") ModelMap model) {

        model.addAttribute("subscriberList",
                subscriptionService.getAllSubscriptions());

        model.addAttribute("requestList",
                requestLogService.getAllRequestLogs().stream()
                        .limit(6)
                        .collect(Collectors.toList()));

        return "index";
    }

    public String getDisplayClass() {

        if (status.equals(FAILURE)) {
            return
        }

        return state.displayClass;
    }

}
