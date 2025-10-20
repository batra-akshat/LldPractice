package org.example.circuitBreaker;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;

public class HalfOpenState extends CircuitBreakerState {

    Queue<RunnableResponse> queue;
    private final Random random;
    int failCounter;
    int successCounter;

    public HalfOpenState() {
        queue = new LinkedList<>();
        random = new Random();
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

    void moveFromHalfOpenToClosed(CircuitBreaker circuitBreaker) {
        //
        circuitBreaker.setCircuitBreakerState(new ClosedState());
    }

    void moveFromHalfOpenToOpen(CircuitBreaker circuitBreaker) {
        //
        circuitBreaker.setCircuitBreakerState(new OpenState());
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
