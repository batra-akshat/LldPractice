package org.example.carRental;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class CheapestBookingStrategy implements BookingStrategy {

    private final CarRentalNetwork network = CarRentalNetwork.getInstance();
    private final InventoryService inventoryService = new InventoryService();

    @Override
    public synchronized Optional<Booking> bookVehicle(VehicleType vehicleType, long startTime, long endTime) {
        var branchNameToBranchHashMap = network.getBranchNameToBranchHashMap();
        HashMap<String, Set<Vehicle>> branchToAvailableVehicles =
                inventoryService.getBranchToAvailableVehicles(branchNameToBranchHashMap, vehicleType, startTime, endTime);
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


}
