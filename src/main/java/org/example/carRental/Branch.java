package org.example.carRental;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
@Builder
public class Branch {
    private String branchName;
    ConcurrentHashMap<VehicleType, HashSet<Vehicle>> vehicleTypeToVehicles;
    ConcurrentHashMap<VehicleType, Integer> vehicleTypeToPriceMap;
    ConcurrentHashMap<String, CopyOnWriteArrayList<Booking>> vehicleIdToBookings;

}
