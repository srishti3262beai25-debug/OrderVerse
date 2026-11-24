package com.restaurant;

import java.util.HashMap;

public class Menu {

    private static HashMap<Integer, FoodItem> menu = new HashMap<>();

    public static void loadMenu() {

        String[][] menuData = {
            {"101", "Paneer Butter Masala", "Main Course", "260.00"},
            {"102", "Dal Makhani", "Main Course", "220.00"},
            {"103", "Butter Chicken", "Main Course", "320.00"},
            {"104", "Veg Dum Biryani", "Main Course", "210.00"},
            {"105", "Paneer Tikka (6 Pcs)", "Starters", "240.00"},
            {"106", "Crispy Corn & Chilli", "Starters", "180.00"},
            {"107", "Garlic Butter Naan", "Breads", "55.00"},
            {"108", "Butter Roti", "Breads", "25.00"},
            {"109", "Gulab Jamun (2 Pcs)", "Desserts", "80.00"},
            {"110", "Sweet Punjabi Lassi", "Beverages", "70.00"},
            {"111", "Masala Chai", "Beverages", "40.00"},
            {"112", "Cold Coffee", "Beverages", "120.00"}
        };

        for (int i = 0; i < menuData.length; i++) {

            int id = Integer.parseInt(menuData[i][0]);
            String name = menuData[i][1];
            String category = menuData[i][2];
            double price = Double.parseDouble(menuData[i][3]);

            menu.put(id, new FoodItem(id, name, category, price));
        }
    }

    public static void displayMenu() {

        System.out.println("\n========== FOOD MENU ==========");

        System.out.printf(" %-5s | %-26s | %-14s | %-10s\n",
                "ID", "Item Name", "Category", "Price");

        for (FoodItem item : menu.values()) {

            System.out.printf(" %-5d | %-26s | %-14s | Rs. %6.2f\n",
                    item.getId(),
                    item.getName(),
                    item.getCategory(),
                    item.getPrice());
        }
    }

    public static FoodItem getItem(int id) {
        return menu.get(id);
    }
}