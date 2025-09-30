package org.example.carRental;

import lombok.Getter;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class CarRentalNetwork {
    private static final CarRentalNetwork carRentalNetwork = new CarRentalNetwork();
    private final ConcurrentHashMap<String, Branch> branchNameToBranchHashMap;

    private CarRentalNetwork() {
        this.branchNameToBranchHashMap = new ConcurrentHashMap<>();
    }

    public static CarRentalNetwork getInstance() {
        return carRentalNetwork;
    }

    public void addBranch(Branch branch) {
        branchNameToBranchHashMap.putIfAbsent(branch.getBranchName(), branch);
    }

    public Optional<Branch> getBranch(String branchName) {
        if (!branchNameToBranchHashMap.containsKey(branchName)) {
            return Optional.empty();
        } else {
            return Optional.of(branchNameToBranchHashMap.get(branchName));
        }
    }
}
