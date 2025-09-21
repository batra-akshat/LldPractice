package org.example.carRental;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class CheapestBookingStrategy implements BookingStrategy {

    private final CarRentalNetwork network = CarRentalNetwork.getInstance();

    @Override
    public synchronized Optional<Booking> bookVehicle(VehicleType vehicleType, long startTime, long endTime) {
        var branchNameToBranchHashMap = network.getBranchNameToBranchHashMap();
        HashMap<String, Set<Vehicle>> branchToAvailableVehicles =
                getBranchToAvailableVehicles(branchNameToBranchHashMap, vehicleType, startTime, endTime);
        var vehicleOptional = getCheapestAvailableVehicle(branchToAvailableVehicles);
        // remove from available vehicles
        if(vehicleOptional.isEmpty()) {
            return Optional.empty(); // no vehicle available
        }
        var vehicle = vehicleOptional.get();
        var branchOfVehicleOptional = network.getBranch(vehicle.getBranchName());
        if (branchOfVehicleOptional.isEmpty()) {
            throw new IllegalStateException("Branch can't be empty");
        }
        var booking = Booking.builder()
                .vehicle(vehicle)
                .bookingId(UUID.randomUUID().toString())
                .startTimeInMillis(startTime)
                .endTimeInMillis(endTime)
                .build();
        branchOfVehicleOptional.get().getVehicleIdToBookings().putIfAbsent(vehicle.getVehicleId(), new CopyOnWriteArrayList<>());
        branchOfVehicleOptional.get().getVehicleIdToBookings().get(vehicle.getVehicleId()).add(booking);
        return Optional.of(booking);
    }

    private HashMap<String, Set<Vehicle>> getBranchToAvailableVehicles(ConcurrentHashMap<String, Branch> branchNameToBranchHashMap,
                                                                       VehicleType vehicleType, long startTime, long endTime) {
        HashMap<String, Set<Vehicle>> vehicles = new HashMap<>();
        for (var branch : branchNameToBranchHashMap.values()) {
            vehicles.put(
                    branch.getBranchName(), branch.vehicleTypeToVehicles.get(vehicleType)
                            .stream().filter(vehicle -> isVehicleAvailable(vehicle, startTime, endTime))
                            .collect(Collectors.toSet()));
        }
        return vehicles;
    }

    private Optional<Vehicle> getCheapestAvailableVehicle(HashMap<String, Set<Vehicle>> branchToAvailableVehicles) {
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
        if (vehicle == null) {
            return Optional.empty();
        }
        return Optional.of(vehicle);
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
