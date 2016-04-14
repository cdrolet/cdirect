package org.cdrolet.cdirect.service;

import com.google.common.collect.Maps;
import org.cdrolet.cdirect.domain.NotificationLog;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by c on 4/12/16.
 */
public class MemoryNotificationLogService implements NotificationLogService {

    private Map<Long, NotificationLog> inventory = Maps.newHashMap();

    public void addRequestLog(NotificationLog log) {
        inventory.put(log.getTimestamp(), log);
    }

    @Override
    public NotificationLog getRequestLog(long timeStamp) {
        return inventory.get(timeStamp);
    }

    @Override
    public Collection<NotificationLog> getAllRequestLogs() {
        return inventory
                .values()
                .stream()
                .sorted((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()))
                .collect(Collectors.toList());
    }

}
