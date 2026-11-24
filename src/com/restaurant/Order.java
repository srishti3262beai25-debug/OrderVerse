package com.restaurant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Order implements Comparable<Order> {
    private String orderId;
    private ArrayList<FoodItem> items;
    private double subtotal;
    private double discount;
    private double tax;
    private double finalAmount;
    private Payment payment;
    private String status;
    private String dateTime;
    private long timestamp;

    public Order(String orderId, ArrayList<FoodItem> cartItems, double subtotal,
                 double discount, double tax, double finalAmount, Payment payment) {
        this.orderId = orderId;
        this.items = new ArrayList<FoodItem>(cartItems);
        this.subtotal = subtotal;
        this.discount = discount;
        this.tax = tax;
        this.finalAmount = finalAmount;
        this.payment = payment;
        this.status = "ORDER PLACED";
        this.timestamp = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        this.dateTime = sdf.format(new Date(this.timestamp));
    }

    public synchronized void setStatus(String status) { this.status = status; }
    public synchronized String getStatus() { return this.status; }
    public String getOrderId() { return orderId; }
    public ArrayList<FoodItem> getItems() { return items; }
    public double getFinalAmount() { return finalAmount; }
    public String getDateTime() { return dateTime; }

    public String generateReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================\n");
        sb.append("                      OrderVerse                 \n");
        sb.append("                        Rajpura                 \n");
        sb.append("========================================================\n");
        sb.append(" Order ID   : ").append(orderId).append("\n");
        sb.append(" Date & Time: ").append(dateTime).append("\n");
        sb.append(" Status     : ").append(getStatus()).append("\n");
        sb.append("--------------------------------------------------------\n");
        sb.append(String.format(" %-4s %-22s %-5s %-8s %-8s\n", "No.", "Item Name", "Qty", "Price", "Total"));
        sb.append("--------------------------------------------------------\n");

        for (int i = 0; i < items.size(); i++) {
            FoodItem f = items.get(i);
            sb.append(String.format(" %-4d %-22s %-5d Rs.%-5.2f Rs.%-6.2f\n",
                (i + 1), f.getName(), f.getQuantity(), f.getPrice(), f.getTotalPrice()));
        }

        sb.append("--------------------------------------------------------\n");
        sb.append(String.format(" Subtotal                     : Rs. %8.2f\n", subtotal));
        sb.append(String.format(" Discount Applied             : -Rs.%8.2f\n", discount));
        sb.append(String.format(" GST (5%%)                     : +Rs.%8.2f\n", tax));
        sb.append("========================================================\n");
        sb.append(String.format(" GRAND TOTAL                  : Rs. %8.2f\n", finalAmount));
        sb.append("========================================================\n");
        if (payment != null) {
            sb.append(" Payment Mode : ").append(payment.getMethod()).append("\n");
            sb.append(" Txn ID       : ").append(payment.getTransactionId()).append("\n");
            sb.append(" Details      : ").append(payment.getDetails()).append("\n");
        }
        sb.append("========================================================\n");
        sb.append("          Thank you for dining with us!                \n");
        sb.append("========================================================\n");
        return sb.toString();
    }

    @Override
    public int compareTo(Order other) {
        if (this.timestamp < other.timestamp) return 1;
        if (this.timestamp > other.timestamp) return -1;
        return 0;
    }
}
