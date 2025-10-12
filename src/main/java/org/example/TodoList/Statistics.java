package org.example.TodoList;

import java.time.LocalDateTime;

class Statistics {
    private int tasksAdded;
    private int tasksCompleted;
    private int tasksSpilledOver;
    private int tasksRemoved;
    private final LocalDateTime startPeriod;
    private final LocalDateTime endPeriod;

    public Statistics(LocalDateTime startPeriod, LocalDateTime endPeriod) {
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    public int getTasksAdded() { return tasksAdded; }
    public void setTasksAdded(int tasksAdded) { this.tasksAdded = tasksAdded; }
    public void incrementTasksAdded() { this.tasksAdded++; }

    public int getTasksCompleted() { return tasksCompleted; }
    public void setTasksCompleted(int tasksCompleted) { this.tasksCompleted = tasksCompleted; }
    public void incrementTasksCompleted() { this.tasksCompleted++; }

    public int getTasksSpilledOver() { return tasksSpilledOver; }
    public void setTasksSpilledOver(int tasksSpilledOver) { this.tasksSpilledOver = tasksSpilledOver; }
    public void incrementTasksSpilledOver() { this.tasksSpilledOver++; }

    public int getTasksRemoved() { return tasksRemoved; }
    public void setTasksRemoved(int tasksRemoved) { this.tasksRemoved = tasksRemoved; }
    public void incrementTasksRemoved() { this.tasksRemoved++; }

    public LocalDateTime getStartPeriod() { return startPeriod; }
    public LocalDateTime getEndPeriod() { return endPeriod; }

    @Override
    public String toString() {
        return String.format("Statistics[Period: %s to %s]\n" +
                        "  Tasks Added: %d\n" +
                        "  Tasks Completed: %d\n" +
                        "  Tasks Spilled Over: %d\n" +
                        "  Tasks Removed: %d",
                startPeriod, endPeriod, tasksAdded, tasksCompleted, tasksSpilledOver, tasksRemoved);
    }
}
