package org.example.ParkingLot;

import java.util.List;
import java.util.Optional;

public class FindNearestSlot implements SlotAllotmentStrategy {

    @Override
    public Optional<ParkingSlot> getSlot(ParkingLot parkingLot, Entry entry) {
        var availableSlots = parkingLot.getSlots().stream()
                .filter(slot -> slot.getVehicle() == null)
                .toList();

        return findNearestSlotToEntry(availableSlots, entry);
    }

    private Optional<ParkingSlot> findNearestSlotToEntry(List<ParkingSlot> availableSlots, Entry entry) {
        if (availableSlots.isEmpty()) {
            return Optional.empty();
        }

        ParkingSlot nearestSlot = null;
        int minDistance = Integer.MAX_VALUE;

        for (ParkingSlot slot : availableSlots) {
            int distance = calculateManhattanDistance(slot, entry);
            if (distance < minDistance) {
                minDistance = distance;
                nearestSlot = slot;
            }
        }

        return Optional.ofNullable(nearestSlot);
    }

    private int calculateManhattanDistance(ParkingSlot slot, Entry entry) {
        return Math.abs(slot.getXCord() - entry.getXCord()) +
                Math.abs(slot.getYCord() - entry.getYCord());
    }
}