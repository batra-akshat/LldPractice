package org.example.ParkingLot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Entry {
    private String entryId;
    private Integer xCord;
    private Integer yCord;
}
