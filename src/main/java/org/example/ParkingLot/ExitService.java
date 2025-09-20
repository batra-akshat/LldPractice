package org.example.ParkingLot;

public class ExitService {

    private final ParkingLot parkingLot = ParkingLot.getInstance();

    public void unParkVehicle(EntryTicket ticket) {
        var slot = ticket.getSlot();
        parkingLot.getOccupiedSlots().remove(slot);
        parkingLot.getAvailableSlots().put(ticket.getSlot().getId(), slot);
    }

    public Invoice generateInvoice(EntryTicket ticket, PaymentMethod method) {
        return parkingLot.getInvoiceCalculationStrategy().calculateInvoice(ticket, method);
    }
}
