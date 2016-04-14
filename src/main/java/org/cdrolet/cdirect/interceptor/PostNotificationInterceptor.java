package org.cdrolet.cdirect.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cdrolet.cdirect.domain.NotificationLog;
import org.cdrolet.cdirect.request.NotificationRequest;
import org.cdrolet.cdirect.service.NotificationLogService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.cdrolet.cdirect.request.NotificationRequest.AUTHORIZATION_HEADERS;

/**
 * Created by c on 4/13/16.
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PostNotificationInterceptor extends HandlerInterceptorAdapter implements NotificationInterceptor {

    private final NotificationLogService notificationLogService;

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) throws Exception {

        if (!isNotificationApiRequest(request)) {
            return;
        }

        String timestamp = NotificationRequest.AuthHeader.AUTH_TIMESTAMP
                .from(request.getHeader(AUTHORIZATION_HEADERS)).get();

        NotificationLog notification = notificationLogService.getRequestLog(Long.valueOf(timestamp));
        if (notification == null) {
            return;
        }

        notificationLogService.addRequestLog(
                NotificationLog
                        .from(notification)
                        .state(NotificationLog.State.COMPLETED)
                        .build());
    }

}
