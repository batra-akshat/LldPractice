package org.example.ParkingLot;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public enum ParkingLot {

    INSTANCE;

    private final ConcurrentHashMap<String, ParkingSlot> availableSlots = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ParkingSlot> occupiedSlots  = new ConcurrentHashMap<>();
    private final List<Entry> entries = new CopyOnWriteArrayList<>();

    private Integer capacity; // null == not initialized yet
    private SlotAllotmentStrategy slotAllotmentStrategy;
    private InvoiceCalculationStrategy invoiceCalculationStrategy;

    /**
     * One-time configuration of the singleton.
     * Safe for concurrent calls; only the first one wins.
     */
    public synchronized void init(ParkingLotConfig config) {
        if (capacity != null) {
            // already initialized; choose: silently return OR throw
            // throw new IllegalStateException("ParkingLot already initialized");
            return;
        }

        this.capacity = config.getCapacity();
        this.slotAllotmentStrategy = config.getSlotAllotmentStrategy();
        this.invoiceCalculationStrategy = config.getInvoiceCalculationStrategy();

        // Pre-create slots
        for (int i = 0; i < capacity; i++) {
            var slotId = UUID.randomUUID().toString();
            var parkingSlot = ParkingSlot.builder()
                    .xCord(i)
                    .yCord(i)
                    .id(slotId)
                    .vehicleType(VehicleType.CAR) // or drive from config if needed
                    .build();
            availableSlots.put(slotId, parkingSlot);
        }

        // Create entries
        for (int i = 0; i < config.getNumberOfEntries(); i++) {
            entries.add(Entry.builder().xCord(i).yCord(i).build());
        }
    }

    /* -------- Optional helpers for smoother migration from your old API -------- */

    public static ParkingLot getInstance() {
        return INSTANCE;
    }

    public static synchronized ParkingLot getInstance(ParkingLotConfig config) {
        INSTANCE.init(config);
        return INSTANCE;
    }
}
