package org.example.carRental;

import com.sun.source.tree.Tree;
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
    ConcurrentHashMap<VehicleType, HashSet<Vehicle>> vehicleTypeToAvailableVehicles;
    ConcurrentHashMap<VehicleType, Integer> vehicleTypeToPriceMap;
    TreeSet<Booking> bookings;

}
