package org.example.circuitBreaker;

public abstract class CircuitBreakerState {

    void openCircuit(CircuitBreaker circuitBreaker, Runnable runnable) {
        System.out.println("OOPS!! Something went wrong");
    }

    void moveFromOpenToHalfOpen(CircuitBreaker circuitBreaker) {
        System.out.println("OOPS!! Something went wrong");
    }

    void moveFromHalfOpenToClosedOrOpen(CircuitBreaker circuitBreaker, Runnable runnable) {
        System.out.println("OOPS!! Something went wrong");
    }

}
