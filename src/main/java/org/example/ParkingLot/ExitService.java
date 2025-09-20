package org.example.ParkingLot;

public class ExitService {

    private final ParkingLot parkingLot = ParkingLot.getInstance();

    public synchronized void unParkVehicle(EntryTicket ticket) {
        if (ticket == null || ticket.getSlot() == null) {
            throw new IllegalArgumentException("Invalid ticket");
        }

        var slot = ticket.getSlot();
        String slotId = slot.getId();

        if (!parkingLot.getOccupiedSlots().containsKey(slotId)) {
            throw new IllegalStateException("Slot not found in occupied slots");
        }

        // Atomic transaction
        parkingLot.getOccupiedSlots().remove(slotId);
        slot.setVehicle(null);
        parkingLot.getAvailableSlots().put(slotId, slot);
    }
}
