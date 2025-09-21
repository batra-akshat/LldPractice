package org.example.carRental;

public class PriceService {

    CarRentalNetwork network = CarRentalNetwork.getInstance();
    public void allocatePrice(String branchName, VehicleType vehicleType, Integer price) {
        var branch = network.getBranch(branchName);
        if(branch.isEmpty()) {
            throw new IllegalStateException("Trying to allocate price to a branch which does not exist");
        }
        var branchVal = branch.get();
        branchVal.vehicleTypeToPriceMap.put(vehicleType, price);
    }
}
