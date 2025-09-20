package org.example.ParkingLot;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Builder
public class ParkingLot {
    private static ParkingLot parkingLot;
    private ConcurrentHashMap<String, ParkingSlot> availableSlots;
    private ConcurrentHashMap<String, ParkingSlot> occupiedSlots; // Use same structure
    private List<Entry> entries;
    private Integer capacity;
    private SlotAllotmentStrategy slotAllotmentStrategy;
    private InvoiceCalculationStrategy invoiceCalculationStrategy;

    private ParkingLot(ParkingLotConfig config) {
        this.capacity = config.getCapacity();
        availableSlots = new ConcurrentHashMap<>();
        occupiedSlots = new ConcurrentHashMap<>();
        for (int i = 0; i < capacity; i++) {
            var slotId = UUID.randomUUID().toString();
            var parkingSlot = ParkingSlot.builder()
                    .xCord(i)
                    .yCord(i)
                    .id(slotId)
                    .vehicleType(VehicleType.CAR) // can be inserted dynamically via config
                    .build();
            availableSlots.put(slotId, parkingSlot);
        }
        slotAllotmentStrategy = config.getSlotAllotmentStrategy();
        invoiceCalculationStrategy = config.getInvoiceCalculationStrategy();
        entries = new ArrayList<>();
        for (int i = 0; i < config.getNumberOfEntries(); i++) {
            entries.add(Entry.builder()
                    .yCord(i)
                    .xCord(i)
                    .build());
        }
    }

    public static synchronized ParkingLot getInstance(ParkingLotConfig config) {
        if (parkingLot == null) {
            parkingLot = new ParkingLot(config);
        }
        return parkingLot;
    }

    public static ParkingLot getInstance() {
        if (parkingLot == null) {
            // Use default config
            return getInstance(new ParkingLotConfig(100, new FindNearestSlot(),
                    new MinuteBasedInvoiceCalculation(), 2));
        }
        return parkingLot;
    }
}
