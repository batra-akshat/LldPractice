package org.example.carRental;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

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
        branchVal.vehicleTypeToVehicles.computeIfAbsent(
                vehicleType,
                k -> ConcurrentHashMap.newKeySet()
        );
        branchVal.vehicleTypeToVehicles.get(vehicleType).add(vehicle);
    }
}
