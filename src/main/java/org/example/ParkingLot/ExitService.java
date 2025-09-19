package org.example.ParkingLot;

public class ExitService {

    private final ParkingLot parkingLot = ParkingLot.getInstance();


    public void unParkVehicle(Vehicle vehicle) {
        var slotParkedIn = parkingLot.getSlots().stream()
                .filter(slot -> slot.getVehicle().equals(vehicle)).findFirst();
        if(slotParkedIn.isEmpty()) {
            throw new IllegalArgumentException("No as such vehicle parked");
        }
        slotParkedIn.get().setVehicle(null); //unpark
    }

    public Invoice generateInvoice(EntryTicket ticket, PaymentMethod method) {
        return parkingLot.getInvoiceCalculationStrategy().calculateInvoice(ticket, method);
    }
}
