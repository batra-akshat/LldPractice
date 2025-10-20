package org.example.circuitBreaker;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

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
    void moveFromHalfOpenToClosedOrOpen(CircuitBreaker circuitBreaker, Runnable runnable) {
        queue.add(getRunnableResponse(runnable));
        int cnt = 0;
        while (!queue.isEmpty() && queue.peek().getStatusCode().equals("200")) {
            cnt++;
            queue.poll();
        }
        if (cnt < circuitBreaker.getNoOfHalfOpenCalls()) {
            moveFromOpenToHalfOpen(circuitBreaker);
        } else {
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
