package org.example.carRental;

import java.util.Optional;

public interface BookingStrategy {
    public Optional<Booking> bookVehicle(VehicleType vehicleType, long startTime, long endTime);
}
