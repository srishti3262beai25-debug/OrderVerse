package com.restaurant;

import java.util.HashMap;

public class OrderTracker {

    private HashMap<String, String> orderStatuses = new HashMap<>();

    public void addOrder(String orderId) {
        orderStatuses.put(orderId, "Order Placed");
    }

    public void updateStatus(String orderId, String status) {

        if (orderStatuses.containsKey(orderId)) {
            orderStatuses.put(orderId, status);
            System.out.println("[Success] Order status updated!");
        } else {
            System.out.println("[Error] Order not found!");
        }
    }

    public void trackOrder(String orderId) {

        String status = orderStatuses.get(orderId);

        if (status != null) {
            System.out.println("Order ID: " + orderId);
            System.out.println("Current Status: " + status);
        } else {
            System.out.println("[Error] Order not found!");
        }
    }
}