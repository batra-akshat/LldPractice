package org.example.TodoList;


import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * THREAD-SAFE TodoService using layered synchronization strategy:
 *
 * Layer 1: ConcurrentHashMap for tasks/userIndices - provides thread-safe map operations
 * Layer 2: ReadWriteLock in UserTaskIndex - allows concurrent reads, exclusive writes per user
 * Layer 3: Synchronized blocks for activity logs - ensures log ordering consistency
 *
 * NO METHOD-LEVEL SYNCHRONIZATION - Each layer handles its own thread-safety.
 * This maximizes concurrency: operations on different users don't block each other!
 *
 * Trade-off: Concurrent modifications to the SAME task use "last write wins" semantics.
 * This is acceptable for most TODO applications. For strict ordering, add version numbers.
 */


class TodoService {
    // ConcurrentHashMap allows concurrent reads and writes at map level
    private final Map<String, Task> tasks;
    private final Map<String, UserTaskIndex> userTaskIndices;
    private final Map<String, String> users;


    // Activity logs need synchronized access for consistent ordering
    private final List<ActivityLog> activityLogs;
    private final Object activityLogLock = new Object();

    public TodoService() {
        this.tasks = new ConcurrentHashMap<>();
        this.userTaskIndices = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
        this.activityLogs = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * THREAD-SAFE: User registration
     */
    public void registerUser(String userId, String username) {
        users.put(userId, username);
        userTaskIndices.putIfAbsent(userId, new UserTaskIndex());
    }

    /**
     * THREAD-SAFE: Add task operation
     * No method-level synchronization needed - each data structure handles its own locking:
     * - ConcurrentHashMap for tasks
     * - UserTaskIndex with ReadWriteLock
     * - Synchronized block for activity logs
     *
     * This allows concurrent adds by different users with no contention!
     */
    public String addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        // Ensure user index exists (atomic if absent)
        userTaskIndices.putIfAbsent(task.getUserId(), new UserTaskIndex());

        // Add to global storage (thread-safe via ConcurrentHashMap)
        tasks.put(task.getTaskId(), task);

        // Add to user's sorted index (thread-safe via WriteLock inside UserTaskIndex)
        UserTaskIndex userIndex = userTaskIndices.get(task.getUserId());
        userIndex.addTask(task);

        // Log activity (thread-safe via synchronized block)
        ActivityLog log = new ActivityLog(
                task.getTaskId(),
                task.getUserId(),
                ActivityType.ADDED,
                "Task '" + task.getTitle() + "' added"
        );
        synchronized (activityLogLock) {
            activityLogs.add(log);
        }

        return task.getTaskId();
    }

    /**
     * THREAD-SAFE: Get task operation
     * Read operation - uses ConcurrentHashMap's thread-safe get
     */
    public Task getTask(String taskId) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        return tasks.get(taskId);
    }

    /**
     * THREAD-SAFE: Modify task operation
     * Uses ConcurrentHashMap and UserTaskIndex locking.
     *
     * Note: If two threads modify the same task concurrently, last write wins.
     * This is acceptable for most use cases. For strict ordering, implement
     * optimistic locking with version numbers.
     */
    public void modifyTask(Task updatedTask) {
        if (updatedTask == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        Task existingTask = tasks.get(updatedTask.getTaskId());
        if (existingTask == null) {
            throw new IllegalArgumentException("Task not found: " + updatedTask.getTaskId());
        }

        // Update global storage (thread-safe via ConcurrentHashMap)
        tasks.put(updatedTask.getTaskId(), updatedTask);

        // Update user's sorted index (thread-safe via WriteLock inside UserTaskIndex)
        UserTaskIndex userIndex = userTaskIndices.get(updatedTask.getUserId());
        if (userIndex != null) {
            userIndex.updateTask(existingTask, updatedTask);
        }

        // Log activity (thread-safe via synchronized block)
        ActivityLog log = new ActivityLog(
                updatedTask.getTaskId(),
                updatedTask.getUserId(),
                ActivityType.MODIFIED,
                "Task '" + updatedTask.getTitle() + "' modified"
        );
        synchronized (activityLogLock) {
            activityLogs.add(log);
        }
    }

    /**
     * THREAD-SAFE: Remove task operation
     * Uses atomic remove from ConcurrentHashMap and UserTaskIndex locking.
     */
    public void removeTask(String taskId) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null");
        }

        // Remove from global storage (atomic operation)
        Task task = tasks.remove(taskId);
        if (task != null) {
            // Remove from user's sorted index (thread-safe via WriteLock)
            UserTaskIndex userIndex = userTaskIndices.get(task.getUserId());
            if (userIndex != null) {
                userIndex.removeTask(taskId);
            }

            // Log activity (thread-safe via synchronized block)
            ActivityLog log = new ActivityLog(
                    taskId,
                    task.getUserId(),
                    ActivityType.REMOVED,
                    "Task '" + task.getTitle() + "' removed"
            );
            synchronized (activityLogLock) {
                activityLogs.add(log);
            }
        }
    }

    /**
     * THREAD-SAFE: Complete task operation
     * Uses atomic remove from ConcurrentHashMap and UserTaskIndex locking.
     */
    public void completeTask(String taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }

        // Log activity before removal (thread-safe via synchronized block)
        ActivityLog log = new ActivityLog(
                taskId,
                task.getUserId(),
                ActivityType.COMPLETED,
                "Task '" + task.getTitle() + "' completed"
        );
        synchronized (activityLogLock) {
            activityLogs.add(log);
        }

        // Remove from storage (completed tasks are auto-removed)
        tasks.remove(taskId);

        // Remove from user's sorted index (thread-safe via WriteLock)
        UserTaskIndex userIndex = userTaskIndices.get(task.getUserId());
        if (userIndex != null) {
            userIndex.removeTask(taskId);
        }
    }

    /**
     * THREAD-SAFE: List tasks operation
     * Uses user's pre-sorted index (which has its own ReadWriteLock)
     */
    public List<Task> listTasks(TaskFilter filter, SortCriteria sortCriteria) {
        List<Task> result;

        // OPTIMIZATION: If filtering by user, use user's pre-sorted index
        if (filter != null && filter.getUserId() != null) {
            UserTaskIndex userIndex = userTaskIndices.get(filter.getUserId());
            if (userIndex == null || userIndex.isEmpty()) {
                return new ArrayList<>();
            }

            // UserTaskIndex.getTasksSortedBy() is thread-safe (uses read lock)
            Collection<Task> userTasks = userIndex.getTasksSortedBy(
                    sortCriteria != null ? sortCriteria : SortCriteria.DEADLINE
            );
            result = new ArrayList<>();

            for (Task task : userTasks) {
                if (matchesNonUserFilters(task, filter)) {
                    result.add(task);
                }
            }
        } else {
            // No user filter - create snapshot of all tasks
            // ConcurrentHashMap.values() is weakly consistent - safe for iteration
            result = new ArrayList<>();
            for (Task task : tasks.values()) {
                if (matchesFilter(task, filter)) {
                    result.add(task);
                }
            }
            sortTasks(result, sortCriteria);
        }

        return result;
    }

    private boolean matchesNonUserFilters(Task task, TaskFilter filter) {
        if (filter == null) {
            return true;
        }

        if (filter.getStatus() != null && filter.getStatus() != task.getStatus()) {
            return false;
        }

        if (filter.getTags() != null && !filter.getTags().isEmpty()) {
            if (!task.getTags().containsAll(filter.getTags())) {
                return false;
            }
        }

        if (filter.getDeadlineStart() != null && task.getDeadline() != null) {
            if (task.getDeadline().isBefore(filter.getDeadlineStart())) {
                return false;
            }
        }
        if (filter.getDeadlineEnd() != null && task.getDeadline() != null) {
            if (task.getDeadline().isAfter(filter.getDeadlineEnd())) {
                return false;
            }
        }

        if (!filter.isIncludeOverdue() && task.isOverdue()) {
            return false;
        }

        if (filter.isOnlyVisible() && !task.isVisible()) {
            return false;
        }

        return true;
    }

    private boolean matchesFilter(Task task, TaskFilter filter) {
        if (filter == null) {
            return true;
        }

        if (filter.getUserId() != null && !filter.getUserId().equals(task.getUserId())) {
            return false;
        }

        return matchesNonUserFilters(task, filter);
    }

    private void sortTasks(List<Task> tasks, SortCriteria criteria) {
        if (criteria == null) {
            criteria = SortCriteria.DEADLINE;
        }

        switch (criteria) {
            case DEADLINE:
                tasks.sort(new DeadlineComparator());
                break;
            case CREATED_DATE:
                tasks.sort(new CreatedDateComparator());
                break;
            case TITLE:
                tasks.sort(new TitleComparator());
                break;
        }
    }

    /**
     * THREAD-SAFE: Get statistics
     * Creates consistent snapshot of activity logs for computation
     */
    public Statistics getStatistics(LocalDateTime startPeriod, LocalDateTime endPeriod) {
        if (startPeriod == null) {
            startPeriod = LocalDateTime.MIN;
        }
        if (endPeriod == null) {
            endPeriod = LocalDateTime.now();
        }

        Statistics stats = new Statistics(startPeriod, endPeriod);

        // Create snapshot of activity logs
        List<ActivityLog> logSnapshot;
        synchronized (activityLogLock) {
            logSnapshot = new ArrayList<>(activityLogs);
        }

        for (ActivityLog log : logSnapshot) {
            if (isInPeriod(log.getTimestamp(), startPeriod, endPeriod)) {
                switch (log.getActivityType()) {
                    case ADDED:
                        stats.incrementTasksAdded();
                        break;
                    case COMPLETED:
                        stats.incrementTasksCompleted();
                        break;
                    case REMOVED:
                        stats.incrementTasksRemoved();
                        break;
                }
            }
        }

        // Count spilled over tasks from current task snapshot
        for (Task task : tasks.values()) {
            if (task.getDeadline() != null &&
                    isInPeriod(task.getDeadline(), startPeriod, endPeriod) &&
                    task.getDeadline().isBefore(LocalDateTime.now()) &&
                    task.getStatus() == TaskStatus.PENDING) {
                stats.incrementTasksSpilledOver();
            }
        }

        return stats;
    }

    /**
     * THREAD-SAFE: Get activity log
     * Returns filtered copy of activity logs
     */
    public List<ActivityLog> getActivityLog(LocalDateTime startPeriod, LocalDateTime endPeriod) {
        if (startPeriod == null) {
            startPeriod = LocalDateTime.MIN;
        }
        if (endPeriod == null) {
            endPeriod = LocalDateTime.now();
        }

        List<ActivityLog> result = new ArrayList<>();

        // Create snapshot of activity logs
        List<ActivityLog> logSnapshot;
        synchronized (activityLogLock) {
            logSnapshot = new ArrayList<>(activityLogs);
        }

        final LocalDateTime finalStart = startPeriod;
        final LocalDateTime finalEnd = endPeriod;

        for (ActivityLog log : logSnapshot) {
            if (isInPeriod(log.getTimestamp(), finalStart, finalEnd)) {
                result.add(log);
            }
        }

        result.sort((l1, l2) -> l2.getTimestamp().compareTo(l1.getTimestamp()));

        return result;
    }

    private boolean isInPeriod(LocalDateTime timestamp, LocalDateTime start, LocalDateTime end) {
        return !timestamp.isBefore(start) && !timestamp.isAfter(end);
    }

    public void printPerformanceMetrics() {
        System.out.println("\n=== Performance Metrics ===");
        System.out.println("Total tasks: " + tasks.size());
        System.out.println("Total users with tasks: " + userTaskIndices.size());
        for (Map.Entry<String, UserTaskIndex> entry : userTaskIndices.entrySet()) {
            System.out.println("  User " + entry.getKey() + ": " +
                    entry.getValue().size() + " tasks (pre-sorted, thread-safe)");
        }
    }
}