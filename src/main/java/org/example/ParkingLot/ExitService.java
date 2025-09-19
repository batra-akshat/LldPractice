package org.example.ParkingLot;

public class ExitService {

    private final ParkingLot parkingLot = ParkingLot.getInstance();

    public void unParkVehicle(EntryTicket ticket) {
        var slot = parkingLot.getSlots().stream()
                .filter(s -> s.getId().equals(ticket.getSlotId()))
                .findFirst();

        if (slot.isEmpty() || slot.get().getVehicle() == null) {
            throw new IllegalArgumentException("Invalid ticket or slot already empty");
        }

        // Verify the vehicle matches
        if (!slot.get().getVehicle().equals(ticket.getVehicle())) {
            throw new IllegalArgumentException("Vehicle mismatch");
        }

        slot.get().setVehicle(null);
    }

    public Invoice generateInvoice(EntryTicket ticket, PaymentMethod method) {
        return parkingLot.getInvoiceCalculationStrategy().calculateInvoice(ticket, method);
    }
}
