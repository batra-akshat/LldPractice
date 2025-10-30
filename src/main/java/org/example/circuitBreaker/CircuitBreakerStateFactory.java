package org.example.circuitBreaker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircuitBreakerStateFactory {
    private final Map<CircuitState, CircuitBreakerState> circuitBreakerStateMap;

    public CircuitBreakerStateFactory() {
        circuitBreakerStateMap = new HashMap<>();
        // Initialize all state instances
        circuitBreakerStateMap.put(CircuitState.CLOSED, new ClosedState());
        circuitBreakerStateMap.put(CircuitState.OPEN, new OpenState());
        circuitBreakerStateMap.put(CircuitState.HALF_OPEN, new HalfOpenState());
    }

    public CircuitBreakerState getCircuitBreakerStateManager(CircuitState state) {
        return circuitBreakerStateMap.get(state);
    }
}

