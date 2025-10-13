package org.example.NotificationSystem;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;

import java.util.Map;

@Slf4j
public class EmailNotificationChannelService implements NotificationChannelService {
    @Override
    public void sendNotification(Notification notification, User user) {
        log.info("Notification is: {}", notification.getUserId());
        // dummy implementation for interview.
        log.info(Markers.appendEntries(Map.of(
                "User", user,
                "Notification", notification,
                "Channel", "Email"
        )), "Sending message");
    }
}
