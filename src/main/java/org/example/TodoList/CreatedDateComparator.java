package org.example.TodoList;

import java.util.Comparator;

class CreatedDateComparator implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        int createdComp = t1.getCreatedAt().compareTo(t2.getCreatedAt());
        return createdComp != 0 ? createdComp : t1.getTaskId().compareTo(t2.getTaskId());
    }
}
