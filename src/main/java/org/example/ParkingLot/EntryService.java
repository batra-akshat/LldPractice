package org.example.ParkingLot;

import java.util.Optional;

public class EntryService {

    private final ParkingLot parkingLot = ParkingLot.getInstance();


    public Optional<ParkingSlot> findAvailableSlot(Entry entry) {
        return parkingLot.getSlotAllotmentStrategy().getSlot(parkingLot, entry);
    }

    public EntryTicket parkVehicle(Vehicle vehicle, Entry entry) {
        Optional<ParkingSlot> availableSlot = findAvailableSlot(entry);

        if (availableSlot.isEmpty()) {
            throw new IllegalArgumentException("No available slots");
        }

        ParkingSlot slot = availableSlot.get();
        slot.setVehicle(vehicle);

        return EntryTicket.builder()
                .slotId(slot.getId())
                .entryId(entry.getEntryId())
                .entryTimeInMillis(System.currentTimeMillis())
                .vehicle(vehicle)
                .build();
    }
}
