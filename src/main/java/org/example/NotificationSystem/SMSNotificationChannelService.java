package org.example.NotificationSystem;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.slf4j.Marker;

import java.util.Map;

@Slf4j
public class SMSNotificationChannelService implements NotificationChannelService {
    @Override
    public void sendNotification(Notification notification, User user) {
        // dummy implementation for interview.
        log.info(Markers.appendEntries(Map.of(
                "User", user,
                "Notification", notification,
                "Channel", "SMS"
        )), "Sending message");
    }
}
