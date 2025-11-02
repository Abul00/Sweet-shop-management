package com.sweetshop;

import com.sweetshop.model.Sweet;
import com.sweetshop.service.SweetShop;
import com.sweetshop.exception.InsufficientStockException;

import java.util.List;
import java.util.Scanner;

/**
 * Main class for console-based Sweet Shop demonstration
 * Provides an interactive command-line interface
 * 
 * @author Sweet Shop Management System
 * @version 1.0
 */
public class Main {
    private static final SweetShop shop = new SweetShop();
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  🍬 Sweet Shop Management System 🍬   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        
        // Initialize with sample data
        initializeSampleData();
        
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            System.out.println();
            
            switch (choice) {
                case 1:
                    viewAllSweets();
                    break;
                case 2:
                    addSweet();
                    break;
                case 3:
                    deleteSweet();
                    break;
                case 4:
                    searchSweets();
                    break;
                case 5:
                    purchaseSweet();
                    break;
                case 6:
                    restockSweet();
                    break;
                case 7:
                    sortSweets();
                    break;
                case 8:
                    displayStatistics();
                    break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using Sweet Shop Management System!");
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    /**
     * Display main menu
     */
    private static void displayMenu() {
        System.out.println("\n┌─────────────── MAIN MENU ───────────────┐");
        System.out.println("│ 1. View All Sweets                      │");
        System.out.println("│ 2. Add New Sweet                        │");
        System.out.println("│ 3. Delete Sweet                         │");
        System.out.println("│ 4. Search Sweets                        │");
        System.out.println("│ 5. Purchase Sweet                       │");
        System.out.println("│ 6. Restock Sweet                        │");
        System.out.println("│ 7. Sort Sweets                          │");
        System.out.println("│ 8. Display Statistics                   │");
        System.out.println("│ 0. Exit                                 │");
        System.out.println("└─────────────────────────────────────────┘");
    }
    
    /**
     * Initialize shop with sample data
     */
    private static void initializeSampleData() {
        shop.addSweet(new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20));
        shop.addSweet(new Sweet(1002, "Gajar Halwa", "Vegetable-Based", 30.0, 15));
        shop.addSweet(new Sweet(1003, "Gulab Jamun", "Milk-Based", 10.0, 50));
        shop.addSweet(new Sweet(1004, "Rasgulla", "Milk-Based", 12.0, 40));
        shop.addSweet(new Sweet(1005, "Jalebi", "Syrup-Based", 8.0, 60));
    }
    
    /**
     * View all sweets in inventory
     */
    private static void viewAllSweets() {
        System.out.println("📋 All Sweets in Inventory:");
        System.out.println("─".repeat(80));
        
        List<Sweet> sweets = shop.getAllSweets();
        if (sweets.isEmpty()) {
            System.out.println("No sweets available in inventory.");
            return;
        }
        
        System.out.printf("%-6s %-20s %-18s %10s %10s%n", 
            "ID", "Name", "Category", "Price (₹)", "Quantity");
        System.out.println("─".repeat(80));
        
        for (Sweet sweet : sweets) {
            String stockStatus = sweet.getQuantity() < 10 ? " ⚠️" : "";
            System.out.printf("%-6d %-20s %-18s %10.2f %10d%s%n",
                sweet.getId(), 
                sweet.getName(), 
                sweet.getCategory(),
                sweet.getPrice(), 
                sweet.getQuantity(),
                stockStatus);
        }
        
        System.out.println("─".repeat(80));
        System.out.println("Total items: " + sweets.size());
    }
    
    /**
     * Add a new sweet
     */
    private static void addSweet() {
        System.out.println("➕ Add New Sweet");
        System.out.println("─".repeat(40));
        
        String name = getStringInput("Enter sweet name: ");
        String category = getStringInput("Enter category: ");
        double price = getDoubleInput("Enter price (₹): ");
        int quantity = getIntInput("Enter quantity: ");
        
        try {
            Sweet sweet = shop.addSweet(name, category, price, quantity);
            System.out.println("\n✅ Sweet added successfully!");
            System.out.println(sweet);
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
    
    /**
     * Delete a sweet
     */
    private static void deleteSweet() {
        System.out.println("🗑️  Delete Sweet");
        System.out.println("─".repeat(40));
        
        int id = getIntInput("Enter sweet ID to delete: ");
        
        Sweet sweet = shop.getSweet(id);
        if (sweet == null) {
            System.out.println("❌ Sweet with ID " + id + " not found.");
            return;
        }
        
        System.out.println("Sweet to delete: " + sweet.getName());
        String confirm = getStringInput("Are you sure? (yes/no): ");
        
        if (confirm.equalsIgnoreCase("yes")) {
            if (shop.deleteSweet(id)) {
                System.out.println("✅ Sweet deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete sweet.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    /**
     * Search sweets
     */
    private static void searchSweets() {
        System.out.println("🔍 Search Sweets");
        System.out.println("─".repeat(40));
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Category");
        System.out.println("3. Search by Price Range");
        
        int choice = getIntInput("Enter choice: ");
        List<Sweet> results = null;
        
        switch (choice) {
            case 1:
                String name = getStringInput("Enter name to search: ");
                results = shop.searchByName(name);
                break;
            case 2:
                String category = getStringInput("Enter category: ");
                results = shop.searchByCategory(category);
                break;
            case 3:
                double minPrice = getDoubleInput("Enter minimum price: ");
                double maxPrice = getDoubleInput("Enter maximum price: ");
                try {
                    results = shop.searchByPriceRange(minPrice, maxPrice);
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ Error: " + e.getMessage());
                    return;
                }
                break;
            default:
                System.out.println("❌ Invalid choice.");
                return;
        }
        
        System.out.println("\n📋 Search Results:");
        System.out.println("─".repeat(80));
        
        if (results.isEmpty()) {
            System.out.println("No sweets found matching your criteria.");
        } else {
            System.out.printf("%-6s %-20s %-18s %10s %10s%n", 
                "ID", "Name", "Category", "Price (₹)", "Quantity");
            System.out.println("─".repeat(80));
            
            for (Sweet sweet : results) {
                System.out.printf("%-6d %-20s %-18s %10.2f %10d%n",
                    sweet.getId(), 
                    sweet.getName(), 
                    sweet.getCategory(),
                    sweet.getPrice(), 
                    sweet.getQuantity());
            }
            
            System.out.println("─".repeat(80));
            System.out.println("Found: " + results.size() + " item(s)");
        }
    }
    
    /**
     * Purchase sweet
     */
    private static void purchaseSweet() {
        System.out.println("🛒 Purchase Sweet");
        System.out.println("─".repeat(40));
        
        int id = getIntInput("Enter sweet ID: ");
        Sweet sweet = shop.getSweet(id);
        
        if (sweet == null) {
            System.out.println("❌ Sweet with ID " + id + " not found.");
            return;
        }
        
        System.out.println("Sweet: " + sweet.getName());
        System.out.println("Available quantity: " + sweet.getQuantity());
        
        int quantity = getIntInput("Enter quantity to purchase: ");
        
        try {
            shop.purchaseSweet(id, quantity);
            System.out.println("✅ Purchased " + quantity + " " + sweet.getName() + "(s) successfully!");
            System.out.println("Remaining stock: " + sweet.getQuantity());
        } catch (InsufficientStockException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
    
    /**
     * Restock sweet
     */
    private static void restockSweet() {
        System.out.println("📦 Restock Sweet");
        System.out.println("─".repeat(40));
        
        int id = getIntInput("Enter sweet ID: ");
        Sweet sweet = shop.getSweet(id);
        
        if (sweet == null) {
            System.out.println("❌ Sweet with ID " + id + " not found.");
            return;
        }
        
        System.out.println("Sweet: " + sweet.getName());
        System.out.println("Current quantity: " + sweet.getQuantity());
        
        int quantity = getIntInput("Enter quantity to add: ");
        
        try {
            shop.restockSweet(id, quantity);
            System.out.println("✅ Restocked " + quantity + " " + sweet.getName() + "(s) successfully!");
            System.out.println("New stock: " + sweet.getQuantity());
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
    
    /**
     * Sort and display sweets
     */
    private static void sortSweets() {
        System.out.println("📊 Sort Sweets");
        System.out.println("─".repeat(40));
        System.out.println("1. Sort by Name");
        System.out.println("2. Sort by Price");
        
        int choice = getIntInput("Enter choice: ");
        List<Sweet> sorted = null;
        
        switch (choice) {
            case 1:
                sorted = shop.getSweetsSortedByName();
                System.out.println("\n📋 Sweets sorted by Name:");
                break;
            case 2:
                sorted = shop.getSweetsSortedByPrice();
                System.out.println("\n📋 Sweets sorted by Price:");
                break;
            default:
                System.out.println("❌ Invalid choice.");
                return;
        }
        
        System.out.println("─".repeat(80));
        System.out.printf("%-6s %-20s %-18s %10s %10s%n", 
            "ID", "Name", "Category", "Price (₹)", "Quantity");
        System.out.println("─".repeat(80));
        
        for (Sweet sweet : sorted) {
            System.out.printf("%-6d %-20s %-18s %10.2f %10d%n",
                sweet.getId(), 
                sweet.getName(), 
                sweet.getCategory(),
                sweet.getPrice(), 
                sweet.getQuantity());
        }
        
        System.out.println("─".repeat(80));
    }
    
    /**
     * Display inventory statistics
     */
    private static void displayStatistics() {
        List<Sweet> sweets = shop.getAllSweets();
        
        System.out.println("📊 Inventory Statistics");
        System.out.println("─".repeat(40));
        
        int totalQuantity = sweets.stream().mapToInt(Sweet::getQuantity).sum();
        double totalValue = sweets.stream().mapToDouble(s -> s.getPrice() * s.getQuantity()).sum();
        long lowStockCount = sweets.stream().filter(s -> s.getQuantity() < 10).count();
        double avgPrice = sweets.stream().mapToDouble(Sweet::getPrice).average().orElse(0.0);
        
        System.out.println("Total Items: " + sweets.size());
        System.out.println("Total Quantity: " + totalQuantity);
        System.out.printf("Total Value: ₹%.2f%n", totalValue);
        System.out.printf("Average Price: ₹%.2f%n", avgPrice);
        System.out.println("Low Stock Items: " + lowStockCount);
        
        System.out.println("\n📦 Stock Status:");
        for (Sweet sweet : sweets) {
            if (sweet.getQuantity() < 10) {
                System.out.printf("  ⚠️  %s - Only %d left%n", sweet.getName(), sweet.getQuantity());
            }
        }
    }
    
    // ==================== Helper Methods for Input ====================
    
    /**
     * Get string input from user
     */
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Get integer input from user with validation
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }
    
    /**
     * Get double input from user with validation
     */
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }
}