package org.example.circuitBreaker;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;

public class HalfOpenState extends CircuitBreakerState {

    Queue<RunnableResponse> queue;
    int failCounter;
    int successCounter;

    public HalfOpenState() {
        queue = new LinkedList<>();
        failCounter = 0;
        successCounter = 0;
    }

    @Override
    void moveFromHalfOpenToClosedOrOpen(CircuitBreaker circuitBreaker, Callable runnable) {
        RunnableResponse response = getRunnableResponse(runnable);

        // Single failure -> immediately go back to OPEN
        if (response.getStatusCode().equals("500")) {
            moveFromHalfOpenToOpen(circuitBreaker);
            return;
        }

        // Track successful calls
        queue.add(response);

        // After N successful calls -> go to CLOSED
        if (queue.size() >= circuitBreaker.getNoOfHalfOpenCalls()) {
            moveFromHalfOpenToClosed(circuitBreaker);
        }
    }

    @Override
    CircuitState getState() {
        return CircuitState.OPEN;
    }

    void moveFromHalfOpenToClosed(CircuitBreaker circuitBreaker) {
        circuitBreaker.transitionToState(CircuitState.CLOSED);
        // Reset the queue for clean state
        queue.clear();
        failCounter = 0;
        successCounter = 0;
    }

    void moveFromHalfOpenToOpen(CircuitBreaker circuitBreaker) {
        // Transition to OPEN state using factory
        circuitBreaker.transitionToState(CircuitState.OPEN);
        // Reset the queue for clean state
        queue.clear();
        failCounter = 0;
        successCounter = 0;
    }

    private RunnableResponse getRunnableResponse(Callable callable) {
        String statusCode;
        try {
            callable.call();
            statusCode = "200";
        } catch (Exception e) {
            statusCode = "500";
        }

        Long currentTimeStamp = System.currentTimeMillis();
        return RunnableResponse.builder()
                .statusCode(statusCode)
                .timeStamp(currentTimeStamp)
                .build();
    }
}
