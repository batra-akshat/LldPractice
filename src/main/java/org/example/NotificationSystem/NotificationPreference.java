package org.example.NotificationSystem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class NotificationPreference {
    private NotificationType type;
    private NotificationChannel channel;
}
