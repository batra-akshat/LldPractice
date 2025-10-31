package org.example.circuitBreaker;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Callable;

/**
 * Circuit Breaker Design - Low Level Design Problem
 * Problem Statement
 * Design and implement a Circuit Breaker pattern that can be used to wrap remote service calls and prevent cascading failures in a distributed system.
 * Background
 * In microservices architecture, when a service becomes unavailable or experiences high latency, continuous attempts to call that service can exhaust resources (threads, connections) and cause cascading failures. A circuit breaker monitors for failures and temporarily blocks requests to failing services, giving them time to recover.
 * Functional Requirements
 * 1. Circuit States
 * Implement three states for the circuit breaker:
 *
 * CLOSED: Normal operation, requests pass through. Track failures.
 * OPEN: Service is failing, requests are blocked immediately without calling the service.
 * HALF_OPEN: Testing if service has recovered, allow limited requests through.
 *
 * 2. State Transitions
 *
 * CLOSED → OPEN: When failure threshold is exceeded within a time window
 * OPEN → HALF_OPEN: After a configurable timeout period
 * HALF_OPEN → CLOSED: When a certain number of consecutive successful requests occur
 * HALF_OPEN → OPEN: If any request fails during the test period
 *
 * 3. Configuration
 * Support the following configurable parameters:
 *
 * Failure threshold (e.g., 5 failures)
 * Failure rate threshold (e.g., 50% failure rate)
 * Timeout duration for OPEN state (e.g., 60 seconds)
 * Time window for counting failures (e.g., last 10 seconds)
 * Number of permitted calls in HALF_OPEN state
 *
 * 4. Core Operations
 *
 * execute(operation): Execute the operation with circuit breaker protection
 * getState(): Get current state of the circuit breaker
 * reset(): Manually reset the circuit breaker to CLOSED state
 *
 * Non-Functional Requirements
 *
 * Thread Safety: The circuit breaker should work correctly in multi-threaded environments
 * Performance: Minimal overhead when circuit is CLOSED
 * Monitoring: Provide metrics/callbacks for state changes and failures
 * Extensibility: Support custom failure detection logic (e.g., specific exceptions, response codes)
 *
 * Expected Deliverables
 *
 * Class Diagram: Show main classes, interfaces, and their relationships
 * Implementation: Core classes with detailed method signatures
 * Example Usage: Demonstrate how to use the circuit breaker
 * Test Scenarios: Describe key test cases
 */
@Getter
public class CircuitBreaker {
    private final Integer failureRate;
    private final Long timeoutForOpenStateInMillis; // OPEN -> HALF_OPEN
    private final Integer timeWindowForCountingFailureInSeconds;
    private final Integer noOfHalfOpenCalls;
    private CircuitBreakerState circuitBreakerState;
    private final String serviceName;
    private Long lastRequestServed;
    private final CircuitBreakerStateFactory stateFactory;


    public CircuitBreaker(String serviceName, CircuitBreakerConfig config) {
        this.serviceName = serviceName;
        this.failureRate = config.getFailureRate();
        this.timeoutForOpenStateInMillis = config.getTimeoutForOpenStateInMillis();
        this.noOfHalfOpenCalls = config.getNoOfHalfOpenCalls();
        this.timeWindowForCountingFailureInSeconds = config.getTimeWindowForCountingFailureInSeconds();
        this.circuitBreakerState = new ClosedState();
        this.stateFactory = new CircuitBreakerStateFactory();
    }

    synchronized CircuitBreakerResponse<String> execute(Callable callable) {
        String response = "success";
        if (circuitBreakerState instanceof ClosedState) {
            circuitBreakerState.openCircuit(this, callable);
            lastRequestServed = System.currentTimeMillis();
        } else if (circuitBreakerState instanceof OpenState) {
            circuitBreakerState.moveFromOpenToHalfOpen(this);

            // Still open? Reject immediately
            if (circuitBreakerState instanceof OpenState) {
                return CircuitBreakerResponse.<String>builder()
                        .isAllowed(false)
                        .circuitBreakerState("OPEN")
                        .apiResponse("Short circuit")
                        .build();
            }
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
    synchronized void reset() {
        circuitBreakerState = stateFactory.getCircuitBreakerStateManager(CircuitState.CLOSED);
    }

    void transitionToState(CircuitState newState) {
        this.circuitBreakerState = stateFactory.getCircuitBreakerStateManager(newState);
    }
}
