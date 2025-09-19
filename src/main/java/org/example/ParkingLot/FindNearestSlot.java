package org.example.ParkingLot;

import java.util.Optional;

public class FindNearestSlot implements SlotAllotmentStrategy{
    @Override
    public Optional<ParkingSlot> getSlot(ParkingLot parkingLot) {
        return parkingLot.getSlots().stream().filter(slot ->
                slot.getVehicle() == null).findFirst();
    }
}
