package org.example.TodoList;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

class TodoApplication {
    public static void main(String[] args) throws InterruptedException {
        TodoService todoService = new TodoService();

        // Register users
        todoService.registerUser("user1", "Alice");
        todoService.registerUser("user2", "Bob");
        todoService.registerUser("user3", "Charlie");

        System.out.println("=== TODO Task Tracking Application (THREAD-SAFE) ===\n");

        // Test 1: Single-threaded operations
        System.out.println("--- Test 1: Single-threaded Operations ---");
        Task task1 = new Task(
                UUID.randomUUID().toString(), "user1",
                "Complete project proposal", "Write and submit",
                LocalDateTime.now().plusDays(5),
                new HashSet<>(Arrays.asList("work", "urgent")), null
        );
        todoService.addTask(task1);
        System.out.println("Added: " + task1.getTitle());

        // Test 2: Multi-threaded concurrent adds
        System.out.println("\n--- Test 2: Multi-threaded Concurrent Adds ---");
        int numThreads = 5;
        int tasksPerThread = 10;
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            final String userId = "user" + ((i % 3) + 1); // Distribute across 3 users

            threads[i] = new Thread(() -> {
                for (int j = 0; j < tasksPerThread; j++) {
                    Task task = new Task(
                            UUID.randomUUID().toString(),
                            userId,
                            "Thread-" + threadId + " Task-" + j,
                            "Description for task " + j,
                            LocalDateTime.now().plusDays(j + 1),
                            new HashSet<>(Arrays.asList("test", "thread-" + threadId)),
                            null
                    );
                    todoService.addTask(task);
                }
                System.out.println("Thread " + threadId + " completed adding " + tasksPerThread + " tasks");
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("All threads completed!");
        todoService.printPerformanceMetrics();

        // Test 3: Concurrent reads while writing
        System.out.println("\n--- Test 3: Concurrent Reads During Writes ---");
        Thread writerThread = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                Task task = new Task(
                        UUID.randomUUID().toString(), "user1",
                        "Writer task " + i, "Description",
                        LocalDateTime.now().plusDays(i+1),
                        new HashSet<>(Arrays.asList("concurrent")), null
                );
                todoService.addTask(task);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread[] readerThreads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final int readerId = i;
            readerThreads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    TaskFilter filter = new TaskFilter();
                    filter.setUserId("user1");
                    List<Task> tasks = todoService.listTasks(filter, SortCriteria.DEADLINE);
                    System.out.println("Reader " + readerId + " read " + tasks.size() + " tasks");
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            readerThreads[i].start();
        }

        writerThread.start();
        writerThread.join();
        for (Thread reader : readerThreads) {
            reader.join();
        }

        System.out.println("Concurrent read/write test completed!");

//        // Test 4: Concurrent modifications
//        System.out.println("\n--- Test 4: Concurrent Modifications ---");
//        TaskFilter user1Filter = new TaskFilter();
//        user1Filter.setUserId("user1");
//        List<Task> user1Tasks = todoService.listTasks(user1Filter, SortCriteria.DEADLINE);
//
//        if (user1Tasks.size() >= 3) {
//            Thread[] modifierThreads = new Thread[3];
//            for (int i = 0; i < 3; i++) {
//                final Task taskToModify = user1Tasks.get(i);
//                final int modifierId = i;
//
//                modifierThreads[i] = new Thread(() -> {
//                    Task modified = taskToModify.withTitle(
//                            taskToModify.getTitle() + " [Modified by thread " + modifierId + "]"
//                    );
//                    todoService.modifyTask(modified);
//                    System.out.println("Thread " + modifierId + " modified task: " + taskToModify.getTaskId());
//                });
//                modifierThreads[i].start();
//            }
//
//            for (Thread modifier : modifierThreads) {
//                modifier.join();
//            }
//
//            System.out.println("Concurrent modification test completed!");
//        }
//
//        // Test 5: Verify data consistency
//        System.out.println("\n--- Test 5: Data Consistency Check ---");
//        user1Tasks = todoService.listTasks(user1Filter, SortCriteria.DEADLINE);
//        System.out.println("User1 has " + user1Tasks.size() + " tasks");
//        System.out.println("First 3 tasks:");
//        for (int i = 0; i < Math.min(3, user1Tasks.size()); i++) {
//            System.out.println("  " + user1Tasks.get(i));
//        }
//
//        // Test 6: Statistics and activity log under concurrent access
//        System.out.println("\n--- Test 6: Statistics & Activity Log ---");
//        Statistics stats = todoService.getStatistics(
//                LocalDateTime.now().minusHours(1),
//                LocalDateTime.now()
//        );
//        System.out.println(stats);
//
//        List<ActivityLog> recentLogs = todoService.getActivityLog(
//                LocalDateTime.now().minusMinutes(5),
//                LocalDateTime.now()
//        );
//        System.out.println("\nRecent activity log entries: " + recentLogs.size());
//        System.out.println("Last 5 activities:");
//        for (int i = 0; i < Math.min(5, recentLogs.size()); i++) {
//            System.out.println("  " + recentLogs.get(i));
//        }
//
//        System.out.println("\n=== THREAD-SAFE Application Demo Complete ===");
//        System.out.println("\nThread-Safety Mechanisms:");
//        System.out.println("1. ConcurrentHashMap for main data structures (lock-free reads, fine-grained writes)");
//        System.out.println("2. ReadWriteLock in UserTaskIndex (multiple concurrent readers, exclusive writer)");
//        System.out.println("3. NO method-level synchronization (maximum concurrency!)");
//        System.out.println("4. Immutable Task objects (modifications create new instances)");
//        System.out.println("5. Synchronized blocks only for activity log consistency");
//        System.out.println("\nPerformance Benefits:");
//        System.out.println("- Different users can add/modify tasks CONCURRENTLY");
//        System.out.println("- Multiple threads can read from same user's tasks CONCURRENTLY");
//        System.out.println("- Only WriteLock contention is per-user (not global)");
//        System.out.println("- No unnecessary blocking between independent operations");
    }
}
