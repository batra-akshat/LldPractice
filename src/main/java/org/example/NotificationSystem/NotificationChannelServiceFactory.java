package org.example.NotificationSystem;

import lombok.Getter;

import java.util.Map;

public class NotificationChannelServiceFactory {
    private final Map<NotificationChannel, NotificationChannelService> channelServices;

    public NotificationChannelServiceFactory(EmailNotificationChannelService emailNotificationChannelService,
                                              SMSNotificationChannelService smsNotificationChannelService,
                                              PushNotificationChannelService pushNotificationChannelService) {
        channelServices = Map.of(
                NotificationChannel.EMAIL, emailNotificationChannelService,
                NotificationChannel.SMS, smsNotificationChannelService,
                NotificationChannel.PUSH_NOTIFICATION, pushNotificationChannelService
        );
    }

    public void sendNotification(Notification notification, User user) {
        // Validate inputs
        if (notification == null) {
            throw new IllegalArgumentException("Notification cannot be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        NotificationType notificationType = notification.getType();
        if (notificationType == null) {
            throw new IllegalArgumentException("Notification type cannot be null");
        }

        Map<NotificationType, NotificationPreference> subscriptions =
                user.getNotificationSubscriptions();

        if (subscriptions == null || !subscriptions.containsKey(notificationType)) {
            throw new IllegalStateException(
                    String.format("User %s is not subscribed to notification type %s",
                            user.getUserId(), notificationType)
            );
        }

        NotificationPreference preference = subscriptions.get(notificationType);
        if (preference == null || preference.getChannel() == null) {
            throw new IllegalStateException("Invalid notification preference");
        }

        NotificationChannel channel = preference.getChannel();
        NotificationChannelService service = channelServices.get(channel);

        if (service == null) {
            throw new IllegalArgumentException("No service found for channel: " + channel);
        }

        service.sendNotification(notification, user);
    }
}
