package com.restaurant;

public class OrderValidator {

    public boolean isValidQuantity(int quantity) {

        if (quantity <= 0) {
            System.out.println("[Error] Quantity must be greater than zero!");
            return false;
        }

        return true;
    }

    public boolean isValidOrder(Cart cart) {

        if (cart == null || cart.getTotal() <= 0) {
            System.out.println("[Error] Cannot place an empty order!");
            return false;
        }

        return true;
    }

    public boolean isValidCustomer(Customer customer) {

        if (customer == null) {
            System.out.println("[Error] Customer details are missing!");
            return false;
        }

        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            System.out.println("[Error] Customer name is required!");
            return false;
        }

        if (customer.getPhone() == null || !customer.getPhone().matches("\\d{10}")) {
            System.out.println("[Error] Enter a valid 10-digit phone number!");
            return false;
        }

        return true;
    }
}
