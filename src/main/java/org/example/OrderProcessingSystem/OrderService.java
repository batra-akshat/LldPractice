package org.example.OrderProcessingSystem;

public class OrderService {
    private OrderProcessingSystem instance = OrderProcessingSystem.getOrderProcessingSystemInstance();
    void assignOrder(Order order) {
        instance.addToOrderQueue(order);
    }
}


// Add to our queue