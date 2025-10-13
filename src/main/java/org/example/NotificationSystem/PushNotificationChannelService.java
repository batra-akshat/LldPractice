package org.example.NotificationSystem;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;

import java.util.Map;

@Slf4j
public class PushNotificationChannelService implements NotificationChannelService{
    @Override
    public void sendNotification(Notification notification, User user) {
        log.info(Markers.appendEntries(Map.of(
                "User", user,
                "Notification", notification,
                "Channel", "Push notification"
        )), "Sending message");
    }
}
