package org.cdrolet.cdirect.interceptor;

import org.cdrolet.cdirect.request.NotificationRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by c on 4/13/16.
 */
public interface NotificationInterceptor {

    default boolean isNotificationApiRequest(HttpServletRequest request) {
        return request.getRequestURL().toString().endsWith(NotificationRequest.NOTIFICATION_PATH);
    }

}
