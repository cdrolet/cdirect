package org.cdrolet.cdirect.service;

import com.google.common.collect.Maps;
import org.cdrolet.cdirect.domain.RequestLog;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cdrolet on 4/10/16.
 */
@Service
public class DynamoEventService implements EventService {

    private Map<Long, RequestLog> requests = Maps.newHashMap();

    public void addRequestLog(RequestLog log) {
        requests.put(log.getTimestamp(), log);
    }

    @Override
    public RequestLog getRequestLog(long timeStamp) {
        return requests.get(timeStamp);
    }

    @Override
    public Collection<RequestLog> getAllRequestLogs() {
        return requests
                .values()
                .stream()
                .sorted((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()))
                .collect(Collectors.toList());
    }
}
