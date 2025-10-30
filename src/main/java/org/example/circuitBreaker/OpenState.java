package org.example.circuitBreaker;

public class OpenState extends CircuitBreakerState {
    @Override
    void moveFromOpenToHalfOpen(CircuitBreaker circuitBreaker) {
        long currentTime = System.currentTimeMillis();
        long timeDiff = circuitBreaker.getLastRequestServed();
        if (currentTime - timeDiff >= circuitBreaker.getTimeoutForOpenStateInMillis()) {
            circuitBreaker.transitionToState(CircuitState.HALF_OPEN);
        }
    }

    @Override
    CircuitState getState() {
        return CircuitState.OPEN;
    }
}
