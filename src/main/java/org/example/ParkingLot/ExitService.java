package org.example.ParkingLot;

public class ExitService {

    private final ParkingLot parkingLot = ParkingLot.getInstance();

    public void unParkVehicle(EntryTicket ticket) {
        var slot = ticket.getSlot();
        slot.setVehicle(null);
        parkingLot.getSlots().put(ticket.getSlot().getId(), slot);
    }

    public Invoice generateInvoice(EntryTicket ticket, PaymentMethod method) {
        return parkingLot.getInvoiceCalculationStrategy().calculateInvoice(ticket, method);
    }
}
