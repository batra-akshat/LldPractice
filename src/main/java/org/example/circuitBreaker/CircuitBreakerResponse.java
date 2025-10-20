package org.example.circuitBreaker;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CircuitBreakerResponse<T> {
    private Boolean isAllowed;
    private String circuitBreakerState;
    private T apiResponse;
}
