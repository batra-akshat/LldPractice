package org.example.circuitBreaker;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

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
    void openCircuit(CircuitBreaker circuitBreaker, Runnable runnable) {
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
        var runnableResponse = getRunnableResponse(runnable);
        queue.add(runnableResponse);
        if (runnableResponse.getStatusCode().equals("500")) {
            failCounter++;
        } else {
            successCounter++;
        }
        var successRate = successCounter / (queue.size());
        if (successRate >= circuitBreaker.getFailureRate()) {
            circuitBreaker.setCircuitBreakerState(new OpenState());
        }
    }

    /**
     * Returns random success or failure response;
     *
     * @return
     */
    private RunnableResponse getRunnableResponse(Runnable runnable) {
        final String[] STATUS_CODES = {"200", "500"};
        String randomStatusCode = STATUS_CODES[random.nextInt(STATUS_CODES.length)];
        Long currentTimeStamp = (System.currentTimeMillis());
        return RunnableResponse.builder()
                .statusCode(randomStatusCode)
                .timeStamp(currentTimeStamp)
                .build();
    }
}
