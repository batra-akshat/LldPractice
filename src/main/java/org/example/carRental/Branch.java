package org.example.carRental;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Builder
public class Branch {
    private String branchName;
    ConcurrentHashMap<VehicleType, HashSet<Vehicle>> vehicleTypeToVehicles;
    ConcurrentHashMap<VehicleType, Integer> vehicleTypeToPriceMap;
    HashMap<String, List<Booking>> vehicleIdToBookings;

}
