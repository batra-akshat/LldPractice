package org.example.carRental;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CheapestBookingStrategy implements BookingStrategy {

    private final CarRentalNetwork network = CarRentalNetwork.getInstance();

    @Override
    public synchronized Optional<Booking> bookVehicle(VehicleType vehicleType, long startTime, long endTime) {
        var branchNameToBranchHashMap = network.getBranchNameToBranchHashMap();
        HashMap<String, HashSet<Vehicle>> branchToAvailableVehicles =
                getBranchToAvailableVehicles(branchNameToBranchHashMap, vehicleType);
        var vehicle = getCheapestAvailableVehicle(branchToAvailableVehicles);
        // remove from available vehicles
        var branchOfVehicleOptional = network.getBranch(vehicle.getBranchName());
        if (branchOfVehicleOptional.isEmpty()) {
            throw new IllegalStateException("Branch can't be empty");
        }
        try {
            branchOfVehicleOptional.get().vehicleTypeToAvailableVehicles
                    .get(vehicle.getVehicleType()).remove(vehicle);
            var booking = Booking.builder()
                    .vehicle(vehicle)
                    .bookingId(UUID.randomUUID().toString())
                    .startTimeInMillis(startTime)
                    .endTimeInMillis(endTime)
                    .build();
            branchOfVehicleOptional.get().getBookings().add(booking);
            return Optional.of(booking);
        } catch (Exception e) {
            // some rollback logic since above 2 steps should be a transaction.
        }
        // this means booking was not successful.
        return Optional.empty();
    }

    private HashMap<String, HashSet<Vehicle>> getBranchToAvailableVehicles(ConcurrentHashMap<String, Branch> branchNameToBranchHashMap, VehicleType vehicleType) {
        HashMap<String, HashSet<Vehicle>> vehicles = new HashMap<>();
        for (var branch : branchNameToBranchHashMap.values()) {
            vehicles.put(branch.getBranchName(), branch.vehicleTypeToAvailableVehicles.get(vehicleType));
        }
        return vehicles;
    }

    private Vehicle getCheapestAvailableVehicle(HashMap<String, HashSet<Vehicle>> branchToAvailableVehicles) {
        Vehicle vehicle = null;
        int price = Integer.MAX_VALUE;
        for (var entry : branchToAvailableVehicles.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            var branchOfTheVehicle = network.getBranch(entry.getKey());
            if (branchOfTheVehicle.isEmpty()) {
                throw new IllegalStateException("Branch can't be not present");
            }
            var priceOfTheVehicle = branchOfTheVehicle.get().vehicleTypeToPriceMap.get(entry.getValue().iterator().next().getVehicleType());
            if (price > priceOfTheVehicle) {
                price = priceOfTheVehicle;
                vehicle = entry.getValue().iterator().next();
            }
        }
        return vehicle;
    }

}
