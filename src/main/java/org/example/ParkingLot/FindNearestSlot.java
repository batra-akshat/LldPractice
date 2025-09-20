package org.example.ParkingLot;

import java.util.Map;
import java.util.Optional;

public class FindNearestSlot implements SlotAllotmentStrategy {

    @Override
    public Optional<ParkingSlot> getSlot(ParkingLot parkingLot, Entry entry,
                                         VehicleType vehicleType) {

        return findNearestSlotToEntry(parkingLot.getSlots(), entry, vehicleType);
    }

    private Optional<ParkingSlot> findNearestSlotToEntry(Map<String, ParkingSlot> availableSlots, Entry entry,
                                                         VehicleType vehicleType) {
        if (availableSlots.isEmpty()) {
            return Optional.empty();
        }

        ParkingSlot nearestSlot = null;
        int minDistance = Integer.MAX_VALUE;

        for (ParkingSlot slot : availableSlots.values()) {
            if (slot.getVehicleType().equals(vehicleType)) {
                int distance = calculateManhattanDistance(slot, entry);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestSlot = slot;
                }
            }
        }
        return Optional.ofNullable(nearestSlot);
    }

    private int calculateManhattanDistance(ParkingSlot slot, Entry entry) {
        return Math.abs(slot.getXCord() - entry.getXCord()) +
                Math.abs(slot.getYCord() - entry.getYCord());
    }
}