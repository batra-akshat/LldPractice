package org.example.carRental;

import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class BranchService {
    private CarRentalNetwork network = CarRentalNetwork.getInstance();
    private Random random = new Random();

    Branch addBranch(String branchName) {
        var branch = Branch.builder()
                .branchName(branchName)
                .vehicleTypeToPriceMap(new ConcurrentHashMap<>())
                .vehicleTypeToAvailableVehicles(new ConcurrentHashMap<>())
                .bookings(new TreeSet<>())
                .build();
        network.addBranch(branch);
        return branch;
    }
}
