package org.example.TodoList;

import java.util.Comparator;

class TitleComparator implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        int titleComp = t1.getTitle().compareTo(t2.getTitle());
        return titleComp != 0 ? titleComp : t1.getTaskId().compareTo(t2.getTaskId());
    }
}
