package com.restaurant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Cart cart = new Cart();
    private static BillCalculator billCalculator = new BillCalculator();
    private static ArrayList<Order> orderHistory = new ArrayList<>();
    private static HashSet<String> orderIdSet = new HashSet<>();
    private static int orderCounter = 1001;

    public static void main(String[] args) {
        Menu.loadMenu();

        System.out.println("=============================================================================");
        System.out.println("                 WELCOME TO OrderVerse (ORDERING SYSTEM)");
        System.out.println("=============================================================================");
        System.out.println("   [Offers] 5% OFF on orders above Rs. 500 | 10% OFF above Rs. 1000!");
        System.out.println("=============================================================================");

        boolean running = true;

        while (running) {
            displayMenuOptions();
            int choice = readInt("Enter your choice (1-9): ");

            switch (choice) {
                case 1: Menu.displayMenu(); break;
                case 2: addToCart(); break;
                case 3: viewCart(); break;
                case 4: modifyCart(); break;
                case 5: removeFromCart(); break;
                case 6: checkout(); break;
                case 7: viewOrderHistory(); break;
                case 8: searchOrder(); break;
                case 9:
                    System.out.println("\nThank you for visiting OrderVerse! Have a great day!");
                    running = false;
                    break;
                default:
                    System.out.println("[Error] Please enter a valid choice between 1 and 9.");
            }

            if (running) {
                System.out.print("\nPress ENTER to continue...");
                scanner.nextLine();
            }
        }
    }


    private static void displayMenuOptions() {
        System.out.println("\n========================================");
        System.out.println("       RESTAURANT ORDERING SIMULATOR");
        System.out.println("========================================");
        System.out.println("  1. View Food Menu");
        System.out.println("  2. Add Food to Cart");
        System.out.println("  3. View Cart");
        System.out.println("  4. Modify Cart");
        System.out.println("  5. Remove Food from Cart");
        System.out.println("  6. Checkout & Pay");
        System.out.println("  7. View Order History");
        System.out.println("  8. Search Order");
        System.out.println("  9. Exit");
        System.out.println("========================================");
    }


    private static void addToCart() {
        Menu.displayMenu();

        int id = readInt("\nEnter Food Item ID to add (or 0 to cancel): ");
        if (id == 0) return;

        FoodItem item = Menu.getItem(id);
        if (item == null) {
            System.out.println("[Error] Item ID not found in menu.");
            return;
        }

        int qty = readInt("Enter quantity for " + item.getName() + ": ");
        if (qty <= 0) {
            System.out.println("[Error] Quantity must be at least 1.");
            return;
        }

        cart.addToCart(item, qty);
    }

    private static void viewCart() {
        cart.displayCart();
    }

    private static void modifyCart() {
        viewCart();
        if (cart.isEmpty()) return;

        int id = readInt("\nEnter Item ID to modify: ");
        int newQty = readInt("Enter new quantity: ");

        if (newQty <= 0) {
            System.out.println("[Notice] Use option 5 to remove an item.");
            return;
        }

        if (!cart.updateQuantity(id, newQty)) {
            System.out.println("[Error] Item ID not found in your cart.");
        } else {
            System.out.println("[Success] Quantity updated successfully!");
        }
    }

    private static void removeFromCart() {
        viewCart();
        if (cart.isEmpty()) return;

        int id = readInt("\nEnter Item ID to remove: ");
        if (cart.removeItem(id)) {
            System.out.println("[Success] Item removed from your cart.");
        } else {
            System.out.println("[Error] Item ID not found in your cart.");
        }
    }

    private static void checkout() {
        if (cart.isEmpty()) {
            System.out.println("[Notice] Your cart is empty! Add items first.");
            return;
        }

        viewCart();

        double subtotal = billCalculator.calculateSubtotal(cart);
        double discount = billCalculator.calculateDiscount(subtotal);
        double tax = billCalculator.calculateTax(subtotal - discount);
        double finalAmount = billCalculator.calculateFinalAmount(subtotal - discount);

        System.out.println("\nSelect Payment Method:");
        System.out.println("  1. Cash");
        System.out.println("  2. UPI (GPay/PhonePe)");
        System.out.println("  3. Card (Debit/Credit)");
        System.out.println("  0. Cancel");

        int payChoice = readInt("Choose payment option (0-3): ");
        Payment payment;

        if (payChoice == 1) {
            double cash = readDouble("Enter cash received (Rs.): ");
            payment = new CashPayment(finalAmount, cash);
        } else if (payChoice == 2) {
            System.out.print("Enter UPI ID (e.g., student@upi): ");
            String upi = scanner.nextLine().trim();
            payment = new UpiPayment(finalAmount, upi);
        } else if (payChoice == 3) {
            System.out.print("Enter 16-digit Card Number: ");
            String card = scanner.nextLine().trim();
            System.out.print("Enter Cardholder Name: ");
            String name = scanner.nextLine().trim();
            payment = new CardPayment(finalAmount, card, name);
        } else {
            System.out.println("[Notice] Checkout cancelled.");
            return;
        }

        if (payment.processPayment()) {
            String orderId;
            do {
                orderId = "ORD-" + (orderCounter++);
            } while (orderIdSet.contains(orderId));

            orderIdSet.add(orderId);

            final Order order = new Order(
                    orderId, cart.getItems(), subtotal, discount, tax, finalAmount, payment);
            orderHistory.add(order);

            Thread kitchenThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.printf(
                                "\n[Kitchen Thread] Order %s sent to chef! Preparing ingredients...%n> ",
                                order.getOrderId());
                        Thread.sleep(3000);

                        order.setStatus("COOKING IN PROGRESS");
                        System.out.printf(
                                "\n[Kitchen Thread] Order %s is being cooked on stove!%n> ",
                                order.getOrderId());
                        Thread.sleep(3000);

                        order.setStatus("READY FOR PICKUP");
                        System.out.printf(
                                "\n[Kitchen Thread] *** Ding! Order %s is READY FOR PICKUP! ***%n> ",
                                order.getOrderId());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });

            kitchenThread.start();
            saveOrderToDB(order);

            cart.clear();
            System.out.println("\n[SUCCESS] Payment verified and Order placed successfully!");
            System.out.println("\n" + order.generateReceipt());
        } else {
            System.out.println(
                    "\n[Failed] Payment could not be processed (insufficient cash or invalid details).");
        }
    }


    private static void saveOrderToDB(Order order) {
        String url = "jdbc:mysql://localhost:3306/restaurant_db?useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "YOUR_MYSQL_PASSWORD"; // Enter your local MySQL password here.

        String sql = "INSERT INTO orders (order_id, final_amount, status, order_time) "
                   + "VALUES (?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, order.getOrderId());
                stmt.setDouble(2, order.getFinalAmount());
                stmt.setString(3, order.getStatus());
                stmt.setString(4, order.getDateTime());
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("[Database Note] Order saved in memory.");
        }
    }

    private static void viewOrderHistory() {
        System.out.println("\n--- ORDER HISTORY ---");
        System.out.println(" 1. Sort by Date & Time (Latest First - Comparable)");
        System.out.println(" 2. Sort by Bill Amount (Highest First - Comparator)");

        int opt = readInt("Choose sorting (1-2): ");

        ArrayList<Order> list = new ArrayList<>(orderHistory);
        if (list.isEmpty()) {
            System.out.println("[Notice] No orders placed yet.");
            return;
        }

        if (opt == 1) {
            Collections.sort(list);
        } else if (opt == 2) {
            Collections.sort(list, new Comparator<Order>() {
                @Override
                public int compare(Order o1, Order o2) {
                    return Double.compare(o2.getFinalAmount(), o1.getFinalAmount());
                }
            });
        } else {
            System.out.println("[Error] Please choose 1 or 2.");
            return;
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.printf(" %-10s | %-18s | %-18s | %-10s%n",
                "Order ID", "Date", "Status", "Amount");
        System.out.println("----------------------------------------------------------------------");

        for (int i = 0; i < list.size(); i++) {
            Order order = list.get(i);
            System.out.printf(" %-10s | %-18s | %-18s | Rs. %7.2f%n",
                    order.getOrderId(), order.getDateTime(),
                    order.getStatus(), order.getFinalAmount());
        }

        System.out.println("----------------------------------------------------------------------");
    }

    private static void searchOrder() {
        System.out.print("\nEnter Order ID to search (e.g., ORD-1001): ");
        String id = scanner.nextLine().trim();

        for (int i = 0; i < orderHistory.size(); i++) {
            if (orderHistory.get(i).getOrderId().equalsIgnoreCase(id)) {
                System.out.println("\n" + orderHistory.get(i).generateReceipt());
                return;
            }
        }

        System.out.println("[Notice] Order not found with ID: " + id);
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[Error] Please enter a valid decimal number.");
            }
        }
    }
}
