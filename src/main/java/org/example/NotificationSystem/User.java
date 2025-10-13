package org.example.NotificationSystem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {
    private String userId;
    private Map<NotificationType, NotificationPreference> notificationSubscriptions;
    private List<NotificationEnricher> userPersonalisedEnrichers;
}
