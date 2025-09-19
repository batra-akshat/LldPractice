package org.example.ParkingLot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class EntryTicket {
    private Vehicle vehicle;
    private Long entryTimeInMillis;
    private String slotId;
}
