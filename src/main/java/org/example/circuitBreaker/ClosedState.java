package org.example.circuitBreaker;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;

public class ClosedState extends CircuitBreakerState {

    Queue<RunnableResponse> queue;
    private final Random random;
    int failCounter;
    int successCounter;

    public ClosedState() {
        queue = new LinkedList<>();
        random = new Random();
        failCounter = 0;
        successCounter = 0;
    }

    @Override
    void openCircuit(CircuitBreaker circuitBreaker, Callable callable) {
        // implement this
        while (!queue.isEmpty() && (System.currentTimeMillis() -
                queue.peek().getTimeStamp()) / 1000 > circuitBreaker.getTimeWindowForCountingFailureInSeconds()) {
            if (queue.peek().getStatusCode().equals("500")) {
                failCounter--;
            } else {
                successCounter--;
            }
            queue.poll();
        }
        var runnableResponse = getRunnableResponse(callable);
        queue.add(runnableResponse);
        if (runnableResponse.getStatusCode().equals("500")) {
            failCounter++;
        } else {
            successCounter++;
        }
        double failureRate = (failCounter * 100.0) / queue.size();
        if (failureRate >= circuitBreaker.getFailureRate()) {
            circuitBreaker.setCircuitBreakerState(new OpenState());
        }
    }

    /**
     * Returns random success or failure response;
     *
     * @return
     */
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
