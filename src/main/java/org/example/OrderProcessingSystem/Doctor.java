package org.example.OrderProcessingSystem;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class Doctor {
    private String doctorId;
    private Long startTime; // shift start time
    private Long endTime; // shift end time
}
