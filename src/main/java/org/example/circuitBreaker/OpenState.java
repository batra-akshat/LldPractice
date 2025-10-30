package org.example.circuitBreaker;

public class OpenState extends CircuitBreakerState {
    @Override
    void moveFromOpenToHalfOpen(CircuitBreaker circuitBreaker) {
        long currentTime = System.currentTimeMillis();
        long timeDiff = circuitBreaker.getLastRequestServed();
        if (currentTime - timeDiff >= circuitBreaker.getTimeoutForOpenStateInMillis()) {
            circuitBreaker.setCircuitBreakerState(new HalfOpenState());
        }
    }

    @Override
    CircuitState getState() {
        return CircuitState.OPEN;
    }
}
