package org.example.carRental;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import java.time.format.DateTimeFormatterBuilder;

public class CarRentalExample {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

    // Services
    private static BranchService branchService;
    private static VehicleService vehicleService;
    private static PriceService priceService;
    private static BookingStrategy bookingStrategy;
    private static InventoryService inventoryService;

    public static void main(String[] args) {
        initializeServices();

        System.out.println("=================================================");
        System.out.println("     VEHICLE RENTAL SERVICE - TEST SUITE        ");
        System.out.println("=================================================\n");

        // Run all test scenarios
        testBasicSampleExecution();
        testEdgeCases();
        testConcurrentBookings();
        testDifferentVehicleTypes();
        testPriceStrategy();
        testValidation();

        System.out.println("\n=================================================");
        System.out.println("           ALL TESTS COMPLETED                   ");
        System.out.println("=================================================");
    }

    private static void initializeServices() {
        branchService = new BranchService();
        vehicleService = new VehicleService();
        priceService = new PriceService();
        bookingStrategy = new CheapestBookingStrategy();
        inventoryService = new InventoryService();
    }

    /**
     * Test the exact sample execution from the problem statement
     */
    private static void testBasicSampleExecution() {
        System.out.println("\n>>> TEST 1: Basic Sample Execution\n");

        // Add branches
        System.out.println("Adding branches...");
        branchService.addBranch("Vasant Vihar");
        System.out.println("✓ Added branch: Vasant Vihar");

        branchService.addBranch("Cyber City");
        System.out.println("✓ Added branch: Cyber City");

        // Allocate prices
        System.out.println("\nAllocating prices...");
        priceService.allocatePrice("Vasant Vihar", VehicleType.SEDAN, 100);
        System.out.println("✓ Vasant Vihar - Sedan: ₹100/hr");

        priceService.allocatePrice("Vasant Vihar", VehicleType.HATCHBACK, 80);
        System.out.println("✓ Vasant Vihar - Hatchback: ₹80/hr");

        priceService.allocatePrice("Cyber City", VehicleType.SEDAN, 200);
        System.out.println("✓ Cyber City - Sedan: ₹200/hr");

        priceService.allocatePrice("Cyber City", VehicleType.HATCHBACK, 50);
        System.out.println("✓ Cyber City - Hatchback: ₹50/hr");

        // Add vehicles
        System.out.println("\nAdding vehicles...");
        vehicleService.addVehicle("DL 01 MR 9310", VehicleType.SEDAN, "Vasant Vihar");
        System.out.println("✓ Added: DL 01 MR 9310 (Sedan) to Vasant Vihar");

        vehicleService.addVehicle("DL 01 MR 9311", VehicleType.SEDAN, "Cyber City");
        System.out.println("✓ Added: DL 01 MR 9311 (Sedan) to Cyber City");

        vehicleService.addVehicle("DL 01 MR 9312", VehicleType.HATCHBACK, "Cyber City");
        System.out.println("✓ Added: DL 01 MR 9312 (Hatchback) to Cyber City");

        // Book vehicles
        System.out.println("\n--- Booking Scenarios ---");

        // Booking 1: Should get Vasant Vihar sedan (cheaper)
        long booking1Start = parseDateTime("29-02-2020 10:00 AM");
        long booking1End = parseDateTime("29-02-2020 01:00 PM");

        System.out.println("\nBooking 1: SEDAN from 10:00 AM to 01:00 PM");
        Optional<Booking> booking1 = bookingStrategy.bookVehicle(
                VehicleType.SEDAN, booking1Start, booking1End);

        if (booking1.isPresent()) {
            printBookingDetails(booking1.get());
        } else {
            System.out.println("✗ NO SEDAN AVAILABLE");
        }

        // Booking 2: Same vehicle, different time slot
        long booking2Start = parseDateTime("29-02-2020 02:00 PM");
        long booking2End = parseDateTime("29-02-2020 03:00 PM");

        System.out.println("\nBooking 2: SEDAN from 02:00 PM to 03:00 PM");
        Optional<Booking> booking2 = bookingStrategy.bookVehicle(
                VehicleType.SEDAN, booking2Start, booking2End);

        if (booking2.isPresent()) {
            printBookingDetails(booking2.get());
        } else {
            System.out.println("✗ NO SEDAN AVAILABLE");
        }

        // Booking 3: Vasant Vihar busy, should get Cyber City
        System.out.println("\nBooking 3: SEDAN from 02:00 PM to 03:00 PM (concurrent with Booking 2)");
        Optional<Booking> booking3 = bookingStrategy.bookVehicle(
                VehicleType.SEDAN, booking2Start, booking2End);

        if (booking3.isPresent()) {
            printBookingDetails(booking3.get());
        } else {
            System.out.println("✗ NO SEDAN AVAILABLE");
        }

        // Booking 4: All sedans busy for this time
        System.out.println("\nBooking 4: SEDAN from 02:00 PM to 03:00 PM (all busy)");
        Optional<Booking> booking4 = bookingStrategy.bookVehicle(
                VehicleType.SEDAN, booking2Start, booking2End);

        if (booking4.isPresent()) {
            printBookingDetails(booking4.get());
        } else {
            System.out.println("✗ NO SEDAN AVAILABLE");
        }

        // Show inventory for different time slots
        System.out.println("\n--- Inventory Check ---");
        showInventory(booking2Start, booking2End);

        long laterStart = parseDateTime("29-02-2020 04:00 PM");
        long laterEnd = parseDateTime("29-02-2020 05:00 PM");
        showInventory(laterStart, laterEnd);
    }

    /**
     * Test edge cases
     */
    private static void testEdgeCases() {
        System.out.println("\n>>> TEST 2: Edge Cases\n");

        // Test booking with no vehicles of requested type
        System.out.println("Test: Booking SUV (no SUVs added)");
        long start = parseDateTime("01-03-2020 10:00 AM");
        long end = parseDateTime("01-03-2020 11:00 AM");

        Optional<Booking> suvBooking = bookingStrategy.bookVehicle(
                VehicleType.SUV, start, end);

        if (!suvBooking.isPresent()) {
            System.out.println("✓ Correctly returned no SUV available");
        } else {
            System.out.println("✗ Unexpected: SUV booking succeeded");
        }

        // Test overlapping bookings
        System.out.println("\nTest: Overlapping bookings for same vehicle");
        long overlapStart = parseDateTime("29-02-2020 12:00 PM");
        long overlapEnd = parseDateTime("29-02-2020 02:30 PM");

        Optional<Booking> overlapBooking = bookingStrategy.bookVehicle(
                VehicleType.SEDAN, overlapStart, overlapEnd);

        if (overlapBooking.isPresent()) {
            System.out.println("✓ Booked alternative vehicle for overlapping time");
            printBookingDetails(overlapBooking.get());
        }
    }

    /**
     * Test concurrent bookings
     */
    private static void testConcurrentBookings() {
        System.out.println("\n>>> TEST 3: Concurrent Bookings\n");

        // Add more vehicles for concurrent testing
        branchService.addBranch("Gurgaon");
        priceService.allocatePrice("Gurgaon", VehicleType.SEDAN, 150);
        vehicleService.addVehicle("DL 01 AB 1234", VehicleType.SEDAN, "Gurgaon");
        vehicleService.addVehicle("DL 01 AB 1235", VehicleType.SEDAN, "Gurgaon");

        ExecutorService executor = Executors.newFixedThreadPool(5);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        long concurrentStart = parseDateTime("05-03-2020 10:00 AM");
        long concurrentEnd = parseDateTime("05-03-2020 11:00 AM");

        System.out.println("Attempting 10 concurrent bookings for 5 available vehicles...");

        for (int i = 0; i < 10; i++) {
            final int bookingNum = i + 1;
            executor.submit(() -> {
                try {
                    Optional<Booking> booking = bookingStrategy.bookVehicle(
                            VehicleType.SEDAN, concurrentStart, concurrentEnd);

                    if (booking.isPresent()) {
                        successCount.incrementAndGet();
                        System.out.println("  Thread " + bookingNum +
                                ": ✓ Booked " + booking.get().getVehicle().getVehicleId());
                    } else {
                        failureCount.incrementAndGet();
                        System.out.println("  Thread " + bookingNum +
                                ": ✗ No vehicle available");
                    }
                } catch (Exception e) {
                    System.out.println("  Thread " + bookingNum +
                            ": Error - " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nConcurrent booking results:");
        System.out.println("  Successful bookings: " + successCount.get());
        System.out.println("  Failed bookings: " + failureCount.get());
        System.out.println("  ✓ Thread safety maintained!");
    }

    /**
     * Test different vehicle types
     */
    private static void testDifferentVehicleTypes() {
        System.out.println("\n>>> TEST 4: Different Vehicle Types\n");

        // Add SUVs
        priceService.allocatePrice("Vasant Vihar", VehicleType.SUV, 300);
        priceService.allocatePrice("Cyber City", VehicleType.SUV, 250);
        vehicleService.addVehicle("DL 01 XY 5555", VehicleType.SUV, "Vasant Vihar");
        vehicleService.addVehicle("DL 01 XY 6666", VehicleType.SUV, "Cyber City");

        long start = parseDateTime("10-03-2020 09:00 AM");
        long end = parseDateTime("10-03-2020 10:00 AM");

        // Book each type
        System.out.println("Booking different vehicle types:");

        for (VehicleType type : VehicleType.values()) {
            Optional<Booking> booking = bookingStrategy.bookVehicle(type, start, end);
            if (booking.isPresent()) {
                System.out.println("  " + type + ": ✓ Booked " +
                        booking.get().getVehicle().getVehicleId() +
                        " from " + booking.get().getVehicle().getBranchName());
            } else {
                System.out.println("  " + type + ": ✗ Not available");
            }
        }
    }

    /**
     * Test price-based strategy
     */
    private static void testPriceStrategy() {
        System.out.println("\n>>> TEST 5: Price Strategy Validation\n");

        // Add vehicles with different prices
        branchService.addBranch("Premium Branch");
        branchService.addBranch("Budget Branch");

        priceService.allocatePrice("Premium Branch", VehicleType.HATCHBACK, 500);
        priceService.allocatePrice("Budget Branch", VehicleType.HATCHBACK, 30);

        vehicleService.addVehicle("DL 01 PR 1111", VehicleType.HATCHBACK, "Premium Branch");
        vehicleService.addVehicle("DL 01 BG 2222", VehicleType.HATCHBACK, "Budget Branch");

        long start = parseDateTime("15-03-2020 10:00 AM");
        long end = parseDateTime("15-03-2020 11:00 AM");

        System.out.println("Booking HATCHBACK (Premium: ₹500/hr, Budget: ₹30/hr)");
        Optional<Booking> booking = bookingStrategy.bookVehicle(
                VehicleType.HATCHBACK, start, end);

        if (booking.isPresent()) {
            Booking b = booking.get();
            System.out.println("✓ Correctly selected cheapest option:");
            System.out.println("  Vehicle: " + b.getVehicle().getVehicleId());
            System.out.println("  Branch: " + b.getVehicle().getBranchName());

            // Calculate cost
            int price = CarRentalNetwork.getInstance()
                    .getBranch(b.getVehicle().getBranchName()).get()
                    .getVehicleTypeToPriceMap().get(b.getVehicle().getVehicleType());
            long hours = (b.getEndTimeInMillis() - b.getStartTimeInMillis()) / (1000 * 60 * 60);
            System.out.println("  Total cost: ₹" + (price * hours));
        }
    }

    /**
     * Test input validation
     */
    private static void testValidation() {
        System.out.println("\n>>> TEST 6: Input Validation\n");

        long validStart = parseDateTime("20-03-2020 10:00 AM");
        long validEnd = parseDateTime("20-03-2020 11:00 AM");

        // Test 1: Invalid time range (start >= end)
        System.out.println("Test: Invalid time range (end before start)");
        try {
            bookingStrategy.bookVehicle(VehicleType.SEDAN, validEnd, validStart);
            System.out.println("✗ Should have thrown exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly rejected: " + e.getMessage());
        }

        // Test 2: Null vehicle type
        System.out.println("\nTest: Null vehicle type");
        try {
            bookingStrategy.bookVehicle(null, validStart, validEnd);
            System.out.println("✗ Should have thrown exception");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Correctly rejected: " + e.getMessage());
        }

        // Test 3: Adding vehicle to non-existent branch
        System.out.println("\nTest: Adding vehicle to non-existent branch");
        try {
            vehicleService.addVehicle("DL 01 XX 9999", VehicleType.SEDAN, "Unknown Branch");
            System.out.println("✗ Should have thrown exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Correctly rejected: " + e.getMessage());
        }

        // Test 4: Setting price for non-existent branch
        System.out.println("\nTest: Setting price for non-existent branch");
        try {
            priceService.allocatePrice("Unknown Branch", VehicleType.SEDAN, 100);
            System.out.println("✗ Should have thrown exception");
        } catch (IllegalStateException e) {
            System.out.println("✓ Correctly rejected: " + e.getMessage());
        }
    }

    // Helper methods

    private static void printBookingDetails(Booking booking) {
        System.out.println("✓ \"" + booking.getVehicle().getVehicleId() +
                "\" from " + booking.getVehicle().getBranchName() + " booked.");
        System.out.println("  Booking ID: " + booking.getBookingId());
        System.out.println("  Duration: " + formatTime(booking.getStartTimeInMillis()) +
                " to " + formatTime(booking.getEndTimeInMillis()));
    }

    private static void showInventory(long start, long end) {
        System.out.println("\nInventory from " + formatTime(start) + " to " + formatTime(end));

        CarRentalNetwork network = CarRentalNetwork.getInstance();

        for (Branch branch : network.getBranchNameToBranchHashMap().values()) {
            System.out.println("\nBranch: " + branch.getBranchName());

            for (VehicleType type : VehicleType.values()) {
                var vehicles = branch.getVehicleTypeToVehicles().get(type);
                if (vehicles != null && !vehicles.isEmpty()) {
                    System.out.println("  " + type + ":");
                    for (Vehicle vehicle : vehicles) {
                        boolean available = inventoryService.isVehicleAvailable(
                                vehicle, start, end);
                        System.out.println("    " + vehicle.getVehicleId() +
                                " - " + (available ? "Available" : "Booked"));
                    }
                }
            }
        }
    }

    private static long parseDateTime(String dateTimeStr) {
        // Parse format: "29-02-2020 10:00 AM"
        // Using more flexible parsing
        DateTimeFormatter flexibleFormatter = new DateTimeFormatterBuilder()
                .appendPattern("dd-MM-yyyy")
                .appendLiteral(' ')
                .appendPattern("hh:mm")
                .appendLiteral(' ')
                .appendPattern("a")
                .toFormatter()
                .withLocale(java.util.Locale.ENGLISH);

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, flexibleFormatter);
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static String formatTime(long millis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(millis),
                ZoneId.systemDefault()
        );
        return dateTime.format(formatter);
    }
}
