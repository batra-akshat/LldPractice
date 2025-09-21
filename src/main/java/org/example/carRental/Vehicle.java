package org.example.carRental;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Vehicle {
    private String vehicleId;
    private String branchName;
    private VehicleType vehicleType;
}
