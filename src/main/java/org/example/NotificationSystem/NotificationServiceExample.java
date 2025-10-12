package org.example.NotificationSystem;

import java.util.Map;

public class NotificationServiceExample {

    public static void main(String[] args) {
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
        ));
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
        ));
        NotificationService service = NotificationService.getInstance();
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
}
