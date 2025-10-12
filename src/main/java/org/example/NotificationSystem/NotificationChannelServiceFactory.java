package org.example.NotificationSystem;

import lombok.Getter;

import java.util.Map;

public class NotificationChannelServiceFactory {
    private static final NotificationChannelServiceFactory instance = new NotificationChannelServiceFactory();
    private final Map<NotificationChannel, NotificationChannelService> channelServices;

    private NotificationChannelServiceFactory() {

        this.channelServices = Map.of(
                NotificationChannel.EMAIL, new EmailNotificationChannelService(),
                NotificationChannel.SMS, new SMSNotificationChannelService(),
                NotificationChannel.PUSH_NOTIFICATION, new PushNotificationChannelService()
        );
    }

    public static NotificationChannelServiceFactory getInstance() {
        return instance;
    }

    public void sendNotification(Notification notification, User user) {
        var notificationType = notification.getType();
        var channel = user.getNotificationSubscriptions().get(notificationType).getChannel();

        // Add null check for safety
        NotificationChannelService service = channelServices.get(channel);
        if (service == null) {
            throw new IllegalArgumentException("No service found for channel: " + channel);
        }

        service.sendNotification(notification, user);
    }
}
