package org.example.ParkingLot;

public class MinuteBasedInvoiceCalculation implements InvoiceCalculationStrategy{
    @Override
    public Invoice calculateInvoice(EntryTicket ticket, PaymentMethod paymentMethod) {
        var durationInMillis = (System.currentTimeMillis() - ticket.getEntryTimeInMillis());
        var timeInMinutes = durationInMillis/60000;
        return Invoice.builder()
                .amount(timeInMinutes*10)
                .durationOfStayInMiilis(durationInMillis)
                .method(paymentMethod)
                .build();
    }
}
