package org.example.OrderProcessingSystem;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Order {
    private String orderId;
    private Integer orderPriority;
    private Integer orderValue;
    private OrderStatus orderStatus;
    private Long orderTimeStamp;
}
