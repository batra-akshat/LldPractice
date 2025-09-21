package org.example.carRental;

import java.util.HashSet;

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
        branchVal.vehicleTypeToVehicles.putIfAbsent(vehicleType, new HashSet<>());
        branchVal.vehicleTypeToVehicles.get(vehicleType).add(vehicle);
    }
}
