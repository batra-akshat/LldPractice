package org.example.NotificationSystem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Notification {
    private String id;
    private String content;
    private String heading;
    private NotificationType type;
    private String userId;
}
