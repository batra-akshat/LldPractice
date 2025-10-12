package org.example.TodoList;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class UserTaskIndex {
    // TreeSets are not thread-safe, so we protect them with a ReadWriteLock
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final TreeSet<Task> tasksByDeadline;
    private final TreeSet<Task> tasksByCreatedDate;
    private final TreeSet<Task> tasksByTitle;
    private final Map<String, Task> taskMap;

    public UserTaskIndex() {
        this.tasksByDeadline = new TreeSet<>(new DeadlineComparator());
        this.tasksByCreatedDate = new TreeSet<>(new CreatedDateComparator());
        this.tasksByTitle = new TreeSet<>(new TitleComparator());
        this.taskMap = new HashMap<>();
    }

    /**
     * WRITE operation - acquires write lock
     */
    public void addTask(Task task) {
        lock.writeLock().lock();
        try {
            tasksByDeadline.add(task);
            tasksByCreatedDate.add(task);
            tasksByTitle.add(task);
            taskMap.put(task.getTaskId(), task);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * WRITE operation - acquires write lock
     */
    public void removeTask(String taskId) {
        lock.writeLock().lock();
        try {
            Task task = taskMap.remove(taskId);
            if (task != null) {
                tasksByDeadline.remove(task);
                tasksByCreatedDate.remove(task);
                tasksByTitle.remove(task);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * WRITE operation - acquires write lock
     * Updates all three indices atomically
     */
    public void updateTask(Task oldTask, Task newTask) {
        lock.writeLock().lock();
        try {
            // Remove old version
            tasksByDeadline.remove(oldTask);
            tasksByCreatedDate.remove(oldTask);
            tasksByTitle.remove(oldTask);

            // Add new version
            tasksByDeadline.add(newTask);
            tasksByCreatedDate.add(newTask);
            tasksByTitle.add(newTask);
            taskMap.put(newTask.getTaskId(), newTask);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * READ operation - acquires read lock
     */
    public Task getTask(String taskId) {
        lock.readLock().lock();
        try {
            return taskMap.get(taskId);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * READ operation - acquires read lock
     * Returns a copy of the sorted tasks to prevent external modification
     */
    public Collection<Task> getTasksSortedBy(SortCriteria criteria) {
        lock.readLock().lock();
        try {
            switch (criteria) {
                case DEADLINE:
                    return new ArrayList<>(tasksByDeadline);
                case CREATED_DATE:
                    return new ArrayList<>(tasksByCreatedDate);
                case TITLE:
                    return new ArrayList<>(tasksByTitle);
                default:
                    return new ArrayList<>(tasksByDeadline);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * READ operation - acquires read lock
     */
    public int size() {
        lock.readLock().lock();
        try {
            return taskMap.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * READ operation - acquires read lock
     */
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return taskMap.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }
}
