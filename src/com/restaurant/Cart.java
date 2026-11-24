package com.restaurant;

import java.util.ArrayList;
import java.util.Iterator;

public class Cart {
    private ArrayList<FoodItem> cartItems = new ArrayList<>();

    public void addToCart(FoodItem item, int quantity) {
        for (FoodItem cartItem : cartItems) {
            if (cartItem.getId() == item.getId()) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                System.out.println("[Success] Cart quantity updated!");
                return;
            }
        }
        cartItems.add(new FoodItem(item.getId(), item.getName(),
                item.getCategory(), item.getPrice(), quantity));
        System.out.println("[Success] Food item added to cart!");
    }
    public void displayCart() {
        System.out.println("\\n========================================================");
        System.out.println("                   YOUR CURRENT CART ");
        System.out.println("========================================================");
        if (cartItems.isEmpty()) {
            System.out.println(" Your cart is currently empty.");
            System.out.println("========================================================");
            return;
        }
        System.out.printf(" %-5s | %-24s | %-5s | %-10s%n",
                "ID", "Item Name", "Qty", "Total (Rs)");
        System.out.println("--------------------------------------------------------");
        double subtotal = getTotal();
        for (FoodItem item : cartItems) {
            System.out.printf(" %-5d | %-24s | %-5d | Rs. %7.2f%n",
                    item.getId(), item.getName(), item.getQuantity(), item.getTotalPrice());
        }
        BillCalculator calculator = new BillCalculator();
        double discount = calculator.calculateDiscount(subtotal);
        double tax = calculator.calculateTax(subtotal - discount);
        double total = calculator.calculateFinalAmount(subtotal - discount);
        System.out.println("--------------------------------------------------------");
        System.out.printf(" Subtotal                   : Rs. %8.2f%n", subtotal);
        System.out.printf(" Discount                   : -Rs.%8.2f%n", discount);
        System.out.printf(" GST (5%%)                   : +Rs.%8.2f%n", tax);
        System.out.println("========================================================");
        System.out.printf(" FINAL ESTIMATED AMOUNT     : Rs. %8.2f%n", total);
        System.out.println("========================================================");
    }
    public double getTotal() {
        double total = 0;
        for (FoodItem item : cartItems) total += item.getTotalPrice();
        return total;
    }
    public boolean isEmpty() { return cartItems.isEmpty(); }

    public boolean updateQuantity(int id, int quantity) {
        for (FoodItem item : cartItems) {
            if (item.getId() == id) {
                item.setQuantity(quantity);
                return true;
            }
        }
        return false;
    }
    public boolean removeItem(int id) {
        Iterator<FoodItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == id) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    public ArrayList<FoodItem> getItems() {
        ArrayList<FoodItem> copy = new ArrayList<>();
        for (FoodItem item : cartItems) {
            copy.add(new FoodItem(item.getId(), item.getName(),
                    item.getCategory(), item.getPrice(), item.getQuantity()));
        }
        return copy;
    }
    public void clear() { cartItems.clear(); 
    }
}