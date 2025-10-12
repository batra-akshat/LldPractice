package org.example.TodoList;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class ActivityLog {
    private String logId;
    private String taskId;
    private String userId;
    private ActivityType activityType;
    private LocalDateTime timestamp;
    private String description;
    private Map<String, String> metadata; // Additional info about the activity

    public ActivityLog(String taskId, String userId, ActivityType activityType, String description) {
        this.logId = UUID.randomUUID().toString();
        this.taskId = taskId;
        this.userId = userId;
        this.activityType = activityType;
        this.timestamp = LocalDateTime.now();
        this.description = description;
        this.metadata = new ConcurrentHashMap<>();
    }

    public String getLogId() { return logId; }
    public String getTaskId() { return taskId; }
    public String getUserId() { return userId; }
    public ActivityType getActivityType() { return activityType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    public Map<String, String> getMetadata() { return new HashMap<>(metadata); }

    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (Task: %s, User: %s)",
                timestamp, activityType, description, taskId, userId);
    }
}
