package org.example.NotificationSystem;

import lombok.Getter;

public class NotificationService {
    @Getter
    private static final NotificationService instance = new NotificationService();
    private final NotificationChannelServiceFactory factory;

    private NotificationService() {
        factory = new NotificationChannelServiceFactory();
    }

    void sendNotification(Notification notification, User user) {
        factory.sendNotification(notification, user);
    }
}
