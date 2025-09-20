package org.example.ParkingLot;

import java.util.Optional;

public class EntryService {

    private final ParkingLot parkingLot = ParkingLot.getInstance();


    public Optional<ParkingSlot> findAvailableSlot(Entry entry, VehicleType vehicleType) {
        return parkingLot.getSlotAllotmentStrategy().getSlot(parkingLot, entry, vehicleType);
    }

    public synchronized EntryTicket parkVehicle(Vehicle vehicle, Entry entry) {
        Optional<ParkingSlot> availableSlot = findAvailableSlot(entry, vehicle.getVehicleType());

        if (availableSlot.isEmpty()) {
            throw new IllegalArgumentException("No available slots");
        }

        ParkingSlot slot = availableSlot.get();
        // these 3 should be done in a single transaction and should be thread safe
        slot.setVehicle(vehicle);
        parkingLot.getAvailableSlots().remove(slot.getId());
        parkingLot.getOccupiedSlots().put(slot.getId(), slot);
        return EntryTicket.builder()
                .slot(slot)
                .entryId(entry.getEntryId())
                .entryTimeInMillis(System.currentTimeMillis())
                .vehicle(vehicle)
                .build();
    }
}
