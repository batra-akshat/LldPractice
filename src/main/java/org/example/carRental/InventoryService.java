package org.example.carRental;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InventoryService {

    private final CarRentalNetwork network = CarRentalNetwork.getInstance();

    public HashMap<String, Set<Vehicle>> getBranchToAvailableVehicles(ConcurrentHashMap<String, Branch> branchNameToBranchHashMap,
                                                                  VehicleType vehicleType, long startTime, long endTime) {
        HashMap<String, Set<Vehicle>> branchToAvailableVehicles = new HashMap<>();
        for (var branch : branchNameToBranchHashMap.values()) {
            Set<Vehicle> vehicles = branch.vehicleTypeToVehicles.get(vehicleType);
            if (vehicles == null || vehicles.isEmpty()) {
                continue; // or return empty set
            }
            branchToAvailableVehicles.put(
                    branch.getBranchName(), branch.vehicleTypeToVehicles.get(vehicleType)
                            .stream().filter(vehicle -> isVehicleAvailable(vehicle, startTime, endTime))
                            .collect(Collectors.toSet()));
        }
        return branchToAvailableVehicles;
    }


    public boolean isVehicleAvailable(Vehicle vehicle, long startTime, long endTime) {

        var branchOptional = network.getBranch(vehicle.getBranchName());
        if (branchOptional.isEmpty()) {
            throw new IllegalStateException("There can't be a vehicle whose branch is not present");
        }
        var branch = branchOptional.get();
        List<Booking> bookings = branch.getVehicleIdToBookings().get(vehicle.getVehicleId());
        if (bookings == null || bookings.isEmpty()) {
            return true; // No bookings mean available
        }
        // Check all bookings for this vehicle
        for (Booking booking : bookings) {
            // Check for time overlap
            if (!(endTime <= booking.getStartTimeInMillis() ||
                    startTime >= booking.getEndTimeInMillis())) {
                return false;
            }
        }
        return true;
    }
}
