package com.restaurant;

public class PaymentProcessor {

    public boolean processPayment(double amount, String paymentMethod) {

        if (amount <= 0) {
            System.out.println("[Error] Invalid payment amount!");
            return false;
        }

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            System.out.println("[Error] Invalid payment method!");
            return false;
        }

        System.out.println("\nProcessing Payment...");
        System.out.println("Payment Method: " + paymentMethod);
        System.out.printf("Amount: Rs. %.2f\n", amount);

        System.out.println("[Success] Payment completed!");

        return true;
    }
}