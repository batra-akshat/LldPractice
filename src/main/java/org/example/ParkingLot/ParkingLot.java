package org.example.ParkingLot;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ParkingLot {
    private static ParkingLot parkingLot;
    private List<ParkingSlot> slots;
    private Integer capacity;
    private SlotAllotmentStrategy slotAllotmentStrategy;
    private InvoiceCalculationStrategy invoiceCalculationStrategy;

    private ParkingLot(ParkingLotConfig config) {
        this.capacity = config.getCapacity();
        slots = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            slots.add(ParkingSlot.builder()
                    .id(UUID.randomUUID().toString())
                    .build());
        }
        slotAllotmentStrategy = config.getSlotAllotmentStrategy();
        invoiceCalculationStrategy = config.getInvoiceCalculationStrategy();
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
            return getInstance(new ParkingLotConfig(100, new FindNearestSlot(), new MinuteBasedInvoiceCalculation()));
        }
        return parkingLot;
    }
}
