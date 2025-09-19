package org.example.DistributedLogger;

public enum LogLevel {
    ERROR(1),
    WARN(2),
    INFO(3),
    DEBUG(4);

    private final int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public boolean hasHigherOrEqualPriority(LogLevel other) {
        return this.priority <= other.priority;
    }

}
// maybe define priority here
