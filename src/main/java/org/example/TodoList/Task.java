package org.example.TodoList;

import java.time.LocalDateTime;
import java.util.*;

class Task {
    private String taskId;
    private String userId;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Set<String> tags;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime startDate; // Task appears in TODO list from this date

    public Task(String taskId, String userId, String title, String description,
                LocalDateTime deadline, Set<String> tags, LocalDateTime startDate) {
        this(taskId, userId, title, description, deadline, tags,
                TaskStatus.PENDING, LocalDateTime.now(), LocalDateTime.now(),
                startDate != null ? startDate : LocalDateTime.now());
    }

    // Full constructor for creating modified versions
    private Task(String taskId, String userId, String title, String description,
                 LocalDateTime deadline, Set<String> tags, TaskStatus status,
                 LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime startDate) {
        this.taskId = taskId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.tags = tags != null ? new HashSet<>(tags) : new HashSet<>();
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startDate = startDate;
    }

    // Getters
    public String getTaskId() { return taskId; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getDeadline() { return deadline; }
    public Set<String> getTags() { return new HashSet<>(tags); }
    public TaskStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getStartDate() { return startDate; }

    // Builder methods that return new Task instances (immutable pattern)
    public Task withTitle(String title) {
        return new Task(taskId, userId, title, description, deadline, tags,
                status, createdAt, LocalDateTime.now(), startDate);
    }

    public Task withDescription(String description) {
        return new Task(taskId, userId, title, description, deadline, tags,
                status, createdAt, LocalDateTime.now(), startDate);
    }

    public Task withDeadline(LocalDateTime deadline) {
        return new Task(taskId, userId, title, description, deadline, tags,
                status, createdAt, LocalDateTime.now(), startDate);
    }

    public Task withTags(Set<String> tags) {
        return new Task(taskId, userId, title, description, deadline, tags,
                status, createdAt, LocalDateTime.now(), startDate);
    }

    public Task withAddedTag(String tag) {
        Set<String> newTags = new HashSet<>(this.tags);
        newTags.add(tag);
        return new Task(taskId, userId, title, description, deadline, newTags,
                status, createdAt, LocalDateTime.now(), startDate);
    }

    public Task withRemovedTag(String tag) {
        Set<String> newTags = new HashSet<>(this.tags);
        newTags.remove(tag);
        return new Task(taskId, userId, title, description, deadline, newTags,
                status, createdAt, LocalDateTime.now(), startDate);
    }

    public Task withStatus(TaskStatus status) {
        return new Task(taskId, userId, title, description, deadline, tags,
                status, createdAt, LocalDateTime.now(), startDate);
    }

    public Task withStartDate(LocalDateTime startDate) {
        return new Task(taskId, userId, title, description, deadline, tags,
                status, createdAt, LocalDateTime.now(), startDate);
    }

    public boolean isOverdue() {
        return status == TaskStatus.PENDING &&
                deadline != null &&
                LocalDateTime.now().isAfter(deadline);
    }

    public boolean isVisible() {
        return LocalDateTime.now().isAfter(startDate) ||
                LocalDateTime.now().isEqual(startDate);
    }

    @Override
    public String toString() {
        return String.format("Task[id=%s, title=%s, deadline=%s, tags=%s, status=%s]",
                taskId, title, deadline, tags, status);
    }
}