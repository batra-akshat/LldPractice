package org.example.circuitBreaker;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CircuitBreakerConfig {
    private Integer failureRate;
    private Long timeoutForOpenStateInMillis; // OPEN -> HALF_OPEN
    private Integer timeWindowForCountingFailureInSeconds;
    private Integer noOfHalfOpenCalls;
}
