package org.example.ParkingLot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParkingLotExample {

    public static void main(String[] args) {
        System.out.println("=== Parking Lot System Demonstration ===\n");

        // 1. Initialize the parking lot
        initializeParkingLot();

        // 2. Single vehicle parking demo
        singleVehicleParkingDemo();

        // 3. Multiple vehicles demo
        multipleVehiclesDemo();

        // 4. Concurrent parking demo (stress test)
        concurrentParkingDemo();

        // 5. Exit and billing demo
        exitAndBillingDemo();

        // 6. System status demo
        systemStatusDemo();
    }

    private static void initializeParkingLot() {
        System.out.println("1. INITIALIZING PARKING LOT");
        System.out.println("==========================");

        // Create a custom configuration
        ParkingLotConfig config = new ParkingLotConfig(
                20,  // 20 parking slots
                new FindNearestSlot(),  // Strategy: find nearest slot
                new MinuteBasedInvoiceCalculation(),  // $10 per minute
                3   // 3 entry points
        );

        ParkingLot parkingLot = ParkingLot.getInstance(config);

        System.out.println("✅ Parking lot created with:");
        System.out.println("   - Capacity: " + parkingLot.getCapacity() + " slots");
        System.out.println("   - Available slots: " + parkingLot.getAvailableSlots().size());
        System.out.println("   - Occupied slots: " + parkingLot.getOccupiedSlots().size());
        System.out.println("   - Entry points: " + parkingLot.getEntries().size());
        System.out.println("   - Strategy: Nearest slot allocation");
        System.out.println("   - Billing: $10 per minute\n");
    }

    private static void singleVehicleParkingDemo() {
        System.out.println("2. SINGLE VEHICLE PARKING DEMO");
        System.out.println("===============================");

        // Create a vehicle
        Vehicle car1 = Vehicle.builder()
                .vehicleId("CAR-001")
                .vehicleType(VehicleType.CAR)
                .build();

        // Select an entry point
        ParkingLot parkingLot = ParkingLot.getInstance();
        Entry entry1 = parkingLot.getEntries().get(0); // First entry point
        entry1.setEntryId("ENTRY-1");

        System.out.println("🚗 Vehicle: " + car1.getVehicleId() + " (" + car1.getVehicleType() + ")");
        System.out.println("🚪 Entry point: " + entry1.getEntryId() +
                " at coordinates (" + entry1.getXCord() + ", " + entry1.getYCord() + ")");

        // Park the vehicle
        EntryService entryService = new EntryService();
        try {
            EntryTicket ticket = entryService.parkVehicle(car1, entry1);

            System.out.println("✅ Successfully parked!");
            System.out.println("   - Ticket ID: " + ticket.getEntryId());
            System.out.println("   - Slot ID: " + ticket.getSlot().getId());
            System.out.println("   - Slot coordinates: (" +
                    ticket.getSlot().getXCord() + ", " +
                    ticket.getSlot().getYCord() + ")");
            System.out.println("   - Entry time: " + ticket.getEntryTimeInMillis());
            System.out.println("   - Available slots now: " + parkingLot.getAvailableSlots().size());
            System.out.println("   - Occupied slots now: " + parkingLot.getOccupiedSlots().size());
        } catch (Exception e) {
            System.out.println("❌ Failed to park: " + e.getMessage());
        }

        System.out.println();
    }

    private static void multipleVehiclesDemo() {
        System.out.println("3. MULTIPLE VEHICLES DEMO");
        System.out.println("==========================");

        EntryService entryService = new EntryService();
        ParkingLot parkingLot = ParkingLot.getInstance();
        List<EntryTicket> tickets = new ArrayList<>();

        // Create different types of vehicles
        Vehicle[] vehicles = {
                Vehicle.builder().vehicleId("CAR-002").vehicleType(VehicleType.CAR).build(),
                Vehicle.builder().vehicleId("BIKE-001").vehicleType(VehicleType.BIKE).build(),
                Vehicle.builder().vehicleId("TRUCK-001").vehicleType(VehicleType.TRUCK).build(),
                Vehicle.builder().vehicleId("CAR-003").vehicleType(VehicleType.CAR).build(),
                Vehicle.builder().vehicleId("BIKE-002").vehicleType(VehicleType.BIKE).build()
        };

        // Park each vehicle from different entry points
        for (int i = 0; i < vehicles.length; i++) {
            Vehicle vehicle = vehicles[i];
            Entry entry = parkingLot.getEntries().get(i % parkingLot.getEntries().size());
            entry.setEntryId("ENTRY-" + (i + 2));

            System.out.println("🚗 Parking " + vehicle.getVehicleId() +
                    " (" + vehicle.getVehicleType() + ") via " + entry.getEntryId());

            try {
                EntryTicket ticket = entryService.parkVehicle(vehicle, entry);
                tickets.add(ticket);

                System.out.println("   ✅ Parked in slot " + ticket.getSlot().getId() +
                        " at (" + ticket.getSlot().getXCord() +
                        ", " + ticket.getSlot().getYCord() + ")");
            } catch (Exception e) {
                System.out.println("   ❌ Failed: " + e.getMessage());
            }
        }

        System.out.println("\n📊 Current Status:");
        System.out.println("   - Total capacity: " + parkingLot.getCapacity());
        System.out.println("   - Available slots: " + parkingLot.getAvailableSlots().size());
        System.out.println("   - Occupied slots: " + parkingLot.getOccupiedSlots().size());
        System.out.println("   - Utilization: " +
                String.format("%.1f%%",
                        (double) parkingLot.getOccupiedSlots().size() / parkingLot.getCapacity() * 100));
        System.out.println();
    }

    private static void concurrentParkingDemo() {
        System.out.println("4. CONCURRENT PARKING STRESS TEST");
        System.out.println("==================================");

        EntryService entryService = new EntryService();
        ParkingLot parkingLot = ParkingLot.getInstance();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<String>> futures = new ArrayList<>();

        System.out.println("🔄 Attempting to park 15 vehicles concurrently...");

        // Try to park 15 vehicles simultaneously (should hit capacity limits)
        for (int i = 0; i < 15; i++) {
            final int vehicleNum = i;
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                try {
                    Vehicle vehicle = Vehicle.builder()
                            .vehicleId("CONCURRENT-" + vehicleNum)
                            .vehicleType(VehicleType.CAR)
                            .build();

                    Entry entry = parkingLot.getEntries().get(vehicleNum % parkingLot.getEntries().size());
                    entry.setEntryId("ENTRY-CONCURRENT-" + vehicleNum);

                    EntryTicket ticket = entryService.parkVehicle(vehicle, entry);
                    return "✅ " + vehicle.getVehicleId() + " parked in slot " + ticket.getSlot().getId();

                } catch (Exception e) {
                    return "❌ " + "CONCURRENT-" + vehicleNum + " failed: " + e.getMessage();
                }
            }, executor);

            futures.add(future);
        }

        // Wait for all parking attempts to complete
        for (CompletableFuture<String> future : futures) {
            try {
                System.out.println("   " + future.get(5, TimeUnit.SECONDS));
            } catch (Exception e) {
                System.out.println("   ❌ Timeout or error: " + e.getMessage());
            }
        }

        executor.shutdown();

        System.out.println("\n📊 After concurrent test:");
        System.out.println("   - Available slots: " + parkingLot.getAvailableSlots().size());
        System.out.println("   - Occupied slots: " + parkingLot.getOccupiedSlots().size());
        System.out.println();
    }

    private static void exitAndBillingDemo() {
        System.out.println("5. EXIT AND BILLING DEMO");
        System.out.println("=========================");

        ParkingLot parkingLot = ParkingLot.getInstance();
        ExitService exitService = new ExitService();

        // Find an occupied slot to demonstrate exit
        if (!parkingLot.getOccupiedSlots().isEmpty()) {
            ParkingSlot occupiedSlot = parkingLot.getOccupiedSlots().values().iterator().next();

            // Create a ticket for this slot (simulating the customer has their ticket)
            EntryTicket ticket = EntryTicket.builder()
                    .slot(occupiedSlot)
                    .entryId("DEMO-EXIT")
                    .entryTimeInMillis(System.currentTimeMillis() - 180000) // 3 minutes ago
                    .vehicle(occupiedSlot.getVehicle())
                    .build();

            System.out.println("🚗 Vehicle " + ticket.getVehicle().getVehicleId() + " is exiting...");
            System.out.println("   - Slot: " + ticket.getSlot().getId());
            System.out.println("   - Parked duration: ~3 minutes");

            // Generate invoice
            Invoice invoice = exitService.generateInvoice(ticket, PaymentMethod.CARD);
            System.out.println("💳 Invoice generated:");
            System.out.println("   - Amount: $" + (invoice.getAmount() != null ? invoice.getAmount() : "N/A"));
            System.out.println("   - Payment method: " + (invoice.getMethod() != null ? invoice.getMethod() : "N/A"));
            System.out.println("   - Duration: " + (invoice.getDurationOfStayInMiilis() != null ?
                    invoice.getDurationOfStayInMiilis() / 60000 : "N/A") + " minutes");

            // Process exit
            try {
                exitService.unParkVehicle(ticket);
                System.out.println("✅ Vehicle successfully exited!");
                System.out.println("   - Available slots now: " + parkingLot.getAvailableSlots().size());
                System.out.println("   - Occupied slots now: " + parkingLot.getOccupiedSlots().size());
            } catch (Exception e) {
                System.out.println("❌ Exit failed: " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️ No vehicles currently parked for exit demo");
        }

        System.out.println();
    }

    private static void systemStatusDemo() {
        System.out.println("6. FINAL SYSTEM STATUS");
        System.out.println("======================");

        ParkingLot parkingLot = ParkingLot.getInstance();

        System.out.println("📊 Parking Lot Status:");
        System.out.println("   - Total Capacity: " + parkingLot.getCapacity());
        System.out.println("   - Available Slots: " + parkingLot.getAvailableSlots().size());
        System.out.println("   - Occupied Slots: " + parkingLot.getOccupiedSlots().size());
        System.out.println("   - Utilization Rate: " +
                String.format("%.1f%%",
                        (double) parkingLot.getOccupiedSlots().size() / parkingLot.getCapacity() * 100));

        System.out.println("\n🚗 Currently Parked Vehicles:");
        if (parkingLot.getOccupiedSlots().isEmpty()) {
            System.out.println("   - None");
        } else {
            parkingLot.getOccupiedSlots().forEach((slotId, slot) -> {
                if (slot.getVehicle() != null) {
                    System.out.println("   - " + slot.getVehicle().getVehicleId() +
                            " (" + slot.getVehicle().getVehicleType() +
                            ") in slot " + slotId +
                            " at (" + slot.getXCord() + ", " + slot.getYCord() + ")");
                }
            });
        }

        System.out.println("\n✅ Demonstration completed successfully!");
        System.out.println("🔧 Features demonstrated:");
        System.out.println("   ✓ Parking lot initialization and configuration");
        System.out.println("   ✓ Single vehicle parking with nearest slot strategy");
        System.out.println("   ✓ Multiple vehicle types and entry points");
        System.out.println("   ✓ Concurrent parking (thread safety)");
        System.out.println("   ✓ Vehicle exit and billing system");
        System.out.println("   ✓ Real-time availability tracking");
        System.out.println("   ✓ Proper error handling for capacity limits");
    }
}
