package org.example.elevator.system;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Elevator {
    private String elevatorId;
    private ElevatorDirection state;
    private Integer capacity;
    private Integer currentFloor;

}
