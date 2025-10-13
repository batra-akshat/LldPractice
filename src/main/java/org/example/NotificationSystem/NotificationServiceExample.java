package org.example.NotificationSystem;

import java.util.List;
import java.util.Map;

public class NotificationServiceExample {

    public static void main(String[] args) {
        var userEnricher = new UserIdNotificationEnricher();
        User user1 = new User("user1", Map.of(
                NotificationType.FRIEND_REQUEST, NotificationPreference
                        .builder()
                        .type(NotificationType.FRIEND_REQUEST)
                        .channel(NotificationChannel.EMAIL)
                        .build(),
                NotificationType.NEW_MESSAGE, NotificationPreference
                        .builder()
                        .type(NotificationType.NEW_MESSAGE)
                        .channel(NotificationChannel.SMS)
                        .build()
        ),
                List.of(userEnricher));
        User user2 = new User("user2", Map.of(
                NotificationType.FRIEND_REQUEST, NotificationPreference
                        .builder()
                        .type(NotificationType.FRIEND_REQUEST)
                        .channel(NotificationChannel.SMS)
                        .build(),
                NotificationType.NEW_MESSAGE, NotificationPreference
                        .builder()
                        .type(NotificationType.NEW_MESSAGE)
                        .channel(NotificationChannel.EMAIL)
                        .build()
        ), List.of(userEnricher));
        NotificationChannelServiceFactory service = getFactory();
        service.sendNotification(Notification.builder()
                .id("Notifcation1")
                .content("This is a new friend request for user 1")
                .heading("Friend request")
                .type(NotificationType.FRIEND_REQUEST)
                .build(), user1);

        service.sendNotification(Notification.builder()
                .id("Notifcation2")
                .content("This is a new friend request for user 2")
                .heading("Friend request")
                .type(NotificationType.FRIEND_REQUEST)
                .build(), user2);
    }

    private static NotificationChannelServiceFactory getFactory() {
        EmailNotificationChannelService emailNotificationChannelService = new EmailNotificationChannelService();
        PushNotificationChannelService pushNotificationChannelService = new PushNotificationChannelService();
        SMSNotificationChannelService smsNotificationChannelService = new SMSNotificationChannelService();
        return new NotificationChannelServiceFactory(emailNotificationChannelService,
                smsNotificationChannelService, pushNotificationChannelService);
    }
}
