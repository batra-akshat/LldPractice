package org.example.ParkingLot;

import java.util.Optional;

public interface SlotAllotmentStrategy {
    Optional<ParkingSlot> getSlot(ParkingLot parkingLot);
}
