package org.example.ParkingLot;

public interface InvoiceCalculationStrategy {
    Invoice calculateInvoice(EntryTicket ticket, PaymentMethod paymentMethod);
}
