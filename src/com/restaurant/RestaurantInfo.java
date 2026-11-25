package com.restaurant;

public class RestaurantInfo {

    private String restaurantName;
    private String address;
    private String contactNumber;

    public RestaurantInfo(String restaurantName, String address, String contactNumber) {
        this.restaurantName = restaurantName;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public void displayInfo() {
        System.out.println("\n===== RESTAURANT INFORMATION =====");
        System.out.println("Name: " + restaurantName);
        System.out.println("Address: " + address);
        System.out.println("Contact: " + contactNumber);
    }

    public String getRestaurantName() {
        return restaurantName;
    }
}