package org.example.TodoList;

import java.time.LocalDateTime;
import java.util.Set;

class TaskFilter {
    private String userId;
    private TaskStatus status;
    private Set<String> tags;
    private LocalDateTime deadlineStart;
    private LocalDateTime deadlineEnd;
    private boolean includeOverdue;
    private boolean onlyVisible;

    public TaskFilter() {
        this.onlyVisible = true;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }

    public LocalDateTime getDeadlineStart() { return deadlineStart; }
    public void setDeadlineStart(LocalDateTime deadlineStart) { this.deadlineStart = deadlineStart; }

    public LocalDateTime getDeadlineEnd() { return deadlineEnd; }
    public void setDeadlineEnd(LocalDateTime deadlineEnd) { this.deadlineEnd = deadlineEnd; }

    public boolean isIncludeOverdue() { return includeOverdue; }
    public void setIncludeOverdue(boolean includeOverdue) { this.includeOverdue = includeOverdue; }

    public boolean isOnlyVisible() { return onlyVisible; }
    public void setOnlyVisible(boolean onlyVisible) { this.onlyVisible = onlyVisible; }
}