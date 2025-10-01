package org.example.OrderProcessingSystem;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class OrderProcessingSystem {

    private static final OrderProcessingSystem orderProcessingSystemInstance = new OrderProcessingSystem();
    private List<Doctor> doctors;
    private PriorityQueue<Order> orderQueue;
    private TreeMap<Long, List<Doctor>> startOfTheSlotToAvailableDoctorsAtThatSlot; // start of the slot
    private ReentrantLock lock = new ReentrantLock();

    private OrderProcessingSystem() {
        doctors = new ArrayList<>(10);
        orderQueue = new PriorityQueue<>(Comparator.comparingInt(Order::getOrderPriority));
        startOfTheSlotToAvailableDoctorsAtThatSlot = new TreeMap<>();
        for(var doctor : doctors) {
            var slots = calculateSlots(doctor);
            for(var slot : slots) {
                startOfTheSlotToAvailableDoctorsAtThatSlot.putIfAbsent(slot, new ArrayList<>());
                startOfTheSlotToAvailableDoctorsAtThatSlot.get(slot).add(doctor);
            }
        }
    }

    public static OrderProcessingSystem getOrderProcessingSystemInstance() {
        return orderProcessingSystemInstance;
    }

    private List<Long> calculateSlots(Doctor doctor) {
        // get the startTime, endTime
        // List(startTime, startTime + 15, startTime + 30 , .... endTime)
        return new ArrayList<>();
    }

    public synchronized void addToOrderQueue(Order order) {
        orderQueue.add(order);
    }

    public void bookSlot(Integer startTimeOfTheSlot) {
        lock.lock();
        try {
            var doctors = startOfTheSlotToAvailableDoctorsAtThatSlot.get(startTimeOfTheSlot);
            doctors.remove(doctors.size()-1);
        } catch (Exception e) {

        } finally {
            lock.unlock();
        }
    }

}


// Store an slot beginning as the key
// all the doctors with available slot as the value
// TreeMap
// TreeMap -> initialise this tree map in order system
// if the doctor is found then remove from the map


