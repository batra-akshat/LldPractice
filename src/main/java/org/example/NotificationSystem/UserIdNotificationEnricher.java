package org.example.NotificationSystem;

public class UserIdNotificationEnricher implements NotificationEnricher{
    @Override
    public void enrichNotification(Notification notification, User user) {
        notification.setUserId(user.getUserId());
    }
}
