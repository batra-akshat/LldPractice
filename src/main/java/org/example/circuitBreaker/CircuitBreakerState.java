package org.example.circuitBreaker;

import java.util.concurrent.Callable;

public abstract class CircuitBreakerState {

    void openCircuit(CircuitBreaker circuitBreaker, Callable runnable) {
        System.out.println("OOPS!! Something went wrong");
    }

    void moveFromOpenToHalfOpen(CircuitBreaker circuitBreaker) {
        System.out.println("OOPS!! Something went wrong");
    }

    void moveFromHalfOpenToClosedOrOpen(CircuitBreaker circuitBreaker, Callable runnable) {
        System.out.println("OOPS!! Something went wrong");
    }

    abstract CircuitState getState();
}
