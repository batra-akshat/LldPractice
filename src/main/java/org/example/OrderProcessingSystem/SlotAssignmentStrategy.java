package org.example.OrderProcessingSystem;

import java.util.Optional;

public interface SlotAssignmentStrategy {

    Optional<Long> getDoctorForProcessingOrder(Order order);
}
