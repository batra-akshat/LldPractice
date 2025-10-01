package org.example.OrderProcessingSystem;

import java.util.Optional;

public class EarliestSlotAvailableStrategy implements SlotAssignmentStrategy {
    private OrderProcessingSystem instance = OrderProcessingSystem.getOrderProcessingSystemInstance();

    @Override
    public Optional<Long> getDoctorForProcessingOrder(Order order) {
        var availableSlotsToDoctors = instance.getStartOfTheSlotToAvailableDoctorsAtThatSlot();
        // search for slots with start >= order.timeStamp
        var slot = availableSlotsToDoctors.ceilingKey(order.getOrderTimeStamp());
        if(slot == null) {
            Optional.empty();
        }
        return Optional.of(slot);
    }
}
