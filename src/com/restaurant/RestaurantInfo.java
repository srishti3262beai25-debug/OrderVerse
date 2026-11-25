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
        System.out.println("\n========================================");
        System.out.println("         RESTAURANT INFORMATION");
        System.out.println("========================================");
        System.out.println("Restaurant Name : " + restaurantName);
        System.out.println("Address         : " + address);
        System.out.println("Contact         : " + contactNumber);
        System.out.println("Opening Time    : 10:00 AM");
        System.out.println("Closing Time    : 11:00 PM");
        System.out.println("Cuisine         : Indian, Chinese, Italian");
        System.out.println("========================================");
    }

    public String getRestaurantName() {
        return restaurantName;
    }
}
