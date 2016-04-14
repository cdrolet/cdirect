package org.cdrolet.cdirect.service;

import org.cdrolet.cdirect.domain.NotificationLog;

import java.util.Collection;

/**
 * Created by c on 4/12/16.
 */
public interface NotificationLogService {

    void addRequestLog(NotificationLog requestLog);

    NotificationLog getRequestLog(long timeStamp);

    Collection<NotificationLog> getAllRequestLogs();

}
