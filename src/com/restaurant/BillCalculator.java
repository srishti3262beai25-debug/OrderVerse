package com.restaurant;

public class BillCalculator {
    private static final double TAX_RATE = 0.05;

    public double calculateSubtotal(Cart cart) {
        return cart.getTotal();
    }

    public double calculateDiscount(double subtotal) {
        if (subtotal >= 1000) return subtotal * 0.10;
        if (subtotal >= 500) return subtotal * 0.05;
        return 0;
    }

    public double calculateTax(double taxableAmount) {
        return taxableAmount * TAX_RATE;
    }

    public double calculateFinalAmount(double taxableAmount) {
        return taxableAmount + calculateTax(taxableAmount);
    }

    public void printBill(Cart cart) {
        double subtotal = calculateSubtotal(cart);
        double discount = calculateDiscount(subtotal);
        double taxableAmount = subtotal - discount;
        double tax = calculateTax(taxableAmount);
        double total = calculateFinalAmount(taxableAmount);

        System.out.println("\\n========== BILL ==========");
        System.out.printf("Subtotal: Rs. %.2f%n", subtotal);
        System.out.printf("Discount: Rs. %.2f%n", discount);
        System.out.printf("GST (5%%): Rs. %.2f%n", tax);
        System.out.printf("Final Amount: Rs. %.2f%n", total);
    }
}
