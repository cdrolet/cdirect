package org.cdrolet.cdirect.service;

import org.cdrolet.cdirect.domain.RequestLog;

import java.util.Collection;

/**
 * Created by c on 4/10/16.
 */
public interface EventService {

    void addRequestLog(RequestLog requestLog);

    RequestLog getRequestLog(long timeStamp);

    Collection<RequestLog> getAllRequestLogs();

}
