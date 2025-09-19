package org.example.ParkingLot;

import java.util.Optional;

public class EntryService {

    private final ParkingLot parkingLot = ParkingLot.getInstance();


    public Optional<ParkingSlot> findAvailableSlot() {
        return parkingLot.getSlotAllotmentStrategy().getSlot(parkingLot);
    }

    public EntryTicket parkVehicle(Vehicle vehicle) {
        Optional<ParkingSlot> availableSlot = findAvailableSlot();

        if (availableSlot.isEmpty()) {
            throw new IllegalArgumentException("No available slots");
        }

        ParkingSlot slot = availableSlot.get();
        slot.setVehicle(vehicle);

        return EntryTicket.builder()
                .slotId(slot.getId())
                .entryTimeInMillis(System.currentTimeMillis())
                .vehicle(vehicle)
                .build();
    }
}
