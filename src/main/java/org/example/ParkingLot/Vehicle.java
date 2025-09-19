package org.example.ParkingLot;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Vehicle {
    private String vehicleId;
    private VehicleType vehicleType;
}
