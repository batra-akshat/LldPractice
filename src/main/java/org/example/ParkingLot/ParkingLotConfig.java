package org.example.ParkingLot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ParkingLotConfig {
    private Integer capacity = 100;// default
    private SlotAllotmentStrategy slotAllotmentStrategy;
    private InvoiceCalculationStrategy invoiceCalculationStrategy;

    // Add this constructor to ParkingLotConfig
    public ParkingLotConfig(Integer capacity, SlotAllotmentStrategy slotAllotmentStrategy,
                            InvoiceCalculationStrategy invoiceCalculationStrategy) {
        this.capacity = capacity;
        this.slotAllotmentStrategy = slotAllotmentStrategy;
        this.invoiceCalculationStrategy = invoiceCalculationStrategy;
    }
}
