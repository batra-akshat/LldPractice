package org.example.ParkingLot;

import lombok.Builder;

@Builder
public class Invoice {
    private Long amount;
    private Long durationOfStayInMiilis;
    private PaymentMethod method;
}
