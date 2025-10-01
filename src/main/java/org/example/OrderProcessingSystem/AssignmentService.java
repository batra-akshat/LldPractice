package org.example.OrderProcessingSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AssignmentService {
    private OrderProcessingSystem instance = OrderProcessingSystem.getOrderProcessingSystemInstance();
    private SlotAssignmentStrategy assignmentStrategy;

    // this will create a threadpool of 100 threads
    // now it will try to read the top element of the queue
    // now once the thread gets the order , it calls the doctorassignmentStrategy interface
    // completes the assignmment in a thread safe way

    ExecutorService executorService = Executors.newFixedThreadPool(100);


    public void processOrders() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for(int i=0;i<100;i++) {
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(assignDoctor(), executorService);
            futures.add(future);
        }
        // wait for all the tasks to complete
        // futures.join();

    }



    private synchronized void assignDoctor() {
        // this method takes in first element from the order queue
        var order = instance.getOrderQueue().poll(); // this is the order with highest priority

    }


}
