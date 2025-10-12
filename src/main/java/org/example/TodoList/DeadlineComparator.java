package org.example.TodoList;

import java.util.Comparator;

class DeadlineComparator implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        if (t1.getDeadline() == null && t2.getDeadline() == null) {
            return t1.getTaskId().compareTo(t2.getTaskId());
        }
        if (t1.getDeadline() == null) return 1;
        if (t2.getDeadline() == null) return -1;
        int deadlineComp = t1.getDeadline().compareTo(t2.getDeadline());
        return deadlineComp != 0 ? deadlineComp : t1.getTaskId().compareTo(t2.getTaskId());
    }
}
