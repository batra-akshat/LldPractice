package org.example.ParkingLot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class Invoice {
    private Long amount;
    private Long durationOfStayInMiilis;
    private PaymentMethod method;
}
