package org.example.carRental;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Booking {
    private Vehicle vehicle;
    private String bookingId;
    private long startTimeInMillis;
    private long endTimeInMillis;
}
