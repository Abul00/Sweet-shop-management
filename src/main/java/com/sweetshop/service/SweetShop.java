package com.sweetshop.service;

import com.sweetshop.model.Sweet;
import com.sweetshop.exception.InsufficientStockException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Sweet Shop Service Class
 * Manages the inventory of sweets and provides operations for
 * adding, deleting, searching, purchasing, and restocking sweets
 * 
 * @author Sweet Shop Management System
 * @version 1.0
 */
public class SweetShop {
    private final Map<Integer, Sweet> inventory;
    private int nextId;

    /**
     * Constructor - initializes empty inventory
     */
    public SweetShop() {
        this.inventory = new HashMap<>();
        this.nextId = 1001;
    }

    /**
     * Adds a new sweet to the shop inventory
     * 
     * @param sweet the sweet to add
     * @throws IllegalArgumentException if sweet is null or ID already exists
     */
    public void addSweet(Sweet sweet) {
        if (sweet == null) {
            throw new IllegalArgumentException("Sweet cannot be null");
        }
        if (inventory.containsKey(sweet.getId())) {
            throw new IllegalArgumentException("Sweet with ID " + sweet.getId() + " already exists");
        }
        inventory.put(sweet.getId(), sweet);
    }

    /**
     * Adds a sweet with auto-generated ID
     * 
     * @param name name of the sweet
     * @param category category of the sweet
     * @param price price of the sweet
     * @param quantity quantity in stock
     * @return the created Sweet object
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Sweet addSweet(String name, String category, double price, int quantity) {
        Sweet sweet = new Sweet(nextId++, name, category, price, quantity);
        inventory.put(sweet.getId(), sweet);
        return sweet;
    }

    /**
     * Deletes a sweet from inventory by ID
     * 
     * @param id the ID of the sweet to delete
     * @return true if deleted successfully, false if not found
     */
    public boolean deleteSweet(int id) {
        return inventory.remove(id) != null;
    }

    /**
     * Gets a sweet by ID
     * 
     * @param id the ID of the sweet
     * @return the Sweet object or null if not found
     */
    public Sweet getSweet(int id) {
        return inventory.get(id);
    }

    /**
     * Returns all sweets in inventory
     * 
     * @return list of all sweets
     */
    public List<Sweet> getAllSweets() {
        return new ArrayList<>(inventory.values());
    }

    /**
     * Searches sweets by name (case-insensitive partial match)
     * 
     * @param name the name to search for
     * @return list of matching sweets
     */
    public List<Sweet> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllSweets();
        }
        
        String searchTerm = name.toLowerCase();
        return inventory.values().stream()
                .filter(sweet -> sweet.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Searches sweets by category (exact match, case-insensitive)
     * 
     * @param category the category to search for
     * @return list of matching sweets
     */
    public List<Sweet> searchByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return getAllSweets();
        }
        
        return inventory.values().stream()
                .filter(sweet -> sweet.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Searches sweets within a price range
     * 
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return list of sweets within the price range
     * @throws IllegalArgumentException if price range is invalid
     */
    public List<Sweet> searchByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new IllegalArgumentException("Invalid price range");
        }
        
        return inventory.values().stream()
                .filter(sweet -> sweet.getPrice() >= minPrice && sweet.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Purchases sweets, decreasing quantity
     * 
     * @param id the ID of the sweet to purchase
     * @param quantity the quantity to purchase
     * @throws IllegalArgumentException if sweet not found or quantity invalid
     * @throws InsufficientStockException if not enough stock available
     */
    public void purchaseSweet(int id, int quantity) {
        Sweet sweet = inventory.get(id);
        if (sweet == null) {
            throw new IllegalArgumentException("Sweet with ID " + id + " not found");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Purchase quantity must be positive");
        }
        
        sweet.decreaseQuantity(quantity);
    }

    /**
     * Restocks sweets, increasing quantity
     * 
     * @param id the ID of the sweet to restock
     * @param quantity the quantity to add
     * @throws IllegalArgumentException if sweet not found or quantity invalid
     */
    public void restockSweet(int id, int quantity) {
        Sweet sweet = inventory.get(id);
        if (sweet == null) {
            throw new IllegalArgumentException("Sweet with ID " + id + " not found");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be positive");
        }
        
        sweet.increaseQuantity(quantity);
    }

    /**
     * Returns sweets sorted by name (alphabetically)
     * 
     * @return sorted list of sweets
     */
    public List<Sweet> getSweetsSortedByName() {
        return inventory.values().stream()
                .sorted(Comparator.comparing(Sweet::getName))
                .collect(Collectors.toList());
    }

    /**
     * Returns sweets sorted by price (ascending)
     * 
     * @return sorted list of sweets
     */
    public List<Sweet> getSweetsSortedByPrice() {
        return inventory.values().stream()
                .sorted(Comparator.comparing(Sweet::getPrice))
                .collect(Collectors.toList());
    }

    /**
     * Returns the total number of sweets in inventory
     * 
     * @return inventory size
     */
    public int getInventorySize() {
        return inventory.size();
    }

    /**
     * Clears all inventory (useful for testing)
     */
    public void clearInventory() {
        inventory.clear();
    }
}