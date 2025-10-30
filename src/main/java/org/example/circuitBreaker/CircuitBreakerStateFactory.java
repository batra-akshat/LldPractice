package org.example.circuitBreaker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircuitBreakerStateFactory {
    Map<CircuitState, CircuitBreakerState> circuitBreakerStateMap;

    CircuitBreakerStateFactory(List<CircuitBreakerState> circuitBreakerStateList) {
        circuitBreakerStateMap = new HashMap<>();
        for (var circuitBreakerState : circuitBreakerStateList) {
            circuitBreakerStateMap.putIfAbsent(circuitBreakerState.getState(), circuitBreakerState);
        }
    }

    public CircuitBreakerState getCircuitBreakerStateManager(CircuitState state) {
        return circuitBreakerStateMap.get(state);
    }
}
