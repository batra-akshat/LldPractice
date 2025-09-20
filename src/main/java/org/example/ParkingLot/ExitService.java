package org.example.ParkingLot;

public class ExitService {

    private final ParkingLot parkingLot = ParkingLot.getInstance();

    public synchronized void unParkVehicle(EntryTicket ticket) {
        var slot = ticket.getSlot();
        // all these 3 steps should be a transaction
        parkingLot.getOccupiedSlots().remove(slot.getId());
        slot.setVehicle(null);
        parkingLot.getAvailableSlots().put(ticket.getSlot().getId(), slot);
    }

    public Invoice generateInvoice(EntryTicket ticket, PaymentMethod method) {
        return parkingLot.getInvoiceCalculationStrategy().calculateInvoice(ticket, method);
    }
}
