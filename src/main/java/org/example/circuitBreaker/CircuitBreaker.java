package org.example.circuitBreaker;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Callable;


@Getter
@Setter
@Builder
public class CircuitBreaker {
    private Integer failureThreshold;
    private Integer failureRate;
    private Long timeoutForOpenStateInMillis; // OPEN -> HALF_OPEN
    private Integer timeWindowForCountingFailureInSeconds;
    private Integer noOfHalfOpenCalls;
    private CircuitBreakerState circuitBreakerState;
    private String serviceName;
    private Long lastRequestServed;

    public CircuitBreaker(String serviceName, CircuitBreakerConfig config) {
        this.serviceName = serviceName;
        this.failureThreshold = config.getFailureThreshold();
        this.failureRate = config.getFailureRate();
        this.timeoutForOpenStateInMillis = config.getTimeoutForOpenStateInMillis();
        this.noOfHalfOpenCalls = config.getNoOfHalfOpenCalls();
        this.timeWindowForCountingFailureInSeconds = config.getTimeWindowForCountingFailureInSeconds();
        circuitBreakerState = new ClosedState();
    }

    CircuitBreakerResponse<String> execute(Callable callable) {
        String response = "success";
        if (circuitBreakerState instanceof ClosedState) {
            circuitBreakerState.openCircuit(this, callable);
            lastRequestServed = System.currentTimeMillis();
        } else if (circuitBreakerState instanceof OpenState) {
            circuitBreakerState.moveFromOpenToHalfOpen(this);
            response = "short circuit";
        } else {
            lastRequestServed = System.currentTimeMillis();
            circuitBreakerState.moveFromHalfOpenToClosedOrOpen(this, callable);
            response = "May not or may proceed";
        }
        return CircuitBreakerResponse.<String>builder()
                .isAllowed(true)
                .circuitBreakerState(circuitBreakerState.getClass().getName())
                .apiResponse(response)
                .build();
    }

    void reset() {
        circuitBreakerState = new ClosedState();
    }
}
