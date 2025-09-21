package org.example.carRental;

import java.util.ArrayList;

public class VehicleService {
    CarRentalNetwork network = CarRentalNetwork.getInstance();

    public void addVehicle(String vehicleId, VehicleType vehicleType, String branchName) {
        var branch = network.getBranch(branchName);
        if (branch.isEmpty()) {
            throw new IllegalStateException("Trying to allocate vehicle to a branch which does not exist");
        }
        var branchVal = branch.get();
        var vehicle = Vehicle.builder()
                .vehicleId(vehicleId)
                .branchName(branchName)
                .vehicleType(vehicleType)
                .build();
        branchVal.vehicleTypeToAvailableVehicles.putIfAbsent(vehicleType, new ArrayList<>());
        branchVal.vehicleTypeToAvailableVehicles.get(vehicleType).add(vehicle);
    }
}
