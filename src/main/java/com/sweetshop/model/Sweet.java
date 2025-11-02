package com.sweetshop.model;

import com.sweetshop.exception.InsufficientStockException;
import java.util.Objects;

/**
 * Sweet Model Class
 * Represents a sweet item in the shop inventory
 * 
 * @author Sweet Shop Management System
 * @version 1.0
 */
public class Sweet {
    private final int id;
    private String name;
    private String category;
    private double price;
    private int quantity;

    /**
     * Constructor to create a new Sweet
     * 
     * @param id Unique identifier for the sweet
     * @param name Name of the sweet
     * @param category Category (e.g., Chocolate, Candy, Nut-Based)
     * @param price Price of the sweet in rupees
     * @param quantity Quantity in stock
     * @throws IllegalArgumentException if any validation fails
     */
    public Sweet(int id, String name, String category, double price, int quantity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Get the unique ID of the sweet
     * @return sweet ID
     */
    public int getId() {
        return id;
    }

    /**
     * Get the name of the sweet
     * @return sweet name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the sweet
     * @param name new name
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    /**
     * Get the category of the sweet
     * @return sweet category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category of the sweet
     * @param category new category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the price of the sweet
     * @return sweet price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Set the price of the sweet
     * @param price new price
     * @throws IllegalArgumentException if price is negative
     */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    /**
     * Get the quantity in stock
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Set the quantity in stock
     * @param quantity new quantity
     * @throws IllegalArgumentException if quantity is negative
     */
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    /**
     * Decrease the quantity by the specified amount
     * Used when purchasing sweets
     * 
     * @param amount amount to decrease
     * @throws InsufficientStockException if requested amount exceeds available stock
     */
    public void decreaseQuantity(int amount) {
        if (amount > quantity) {
            throw new InsufficientStockException(
                String.format("Insufficient stock. Available: %d, Requested: %d", quantity, amount)
            );
        }
        this.quantity -= amount;
    }

    /**
     * Increase the quantity by the specified amount
     * Used when restocking sweets
     * 
     * @param amount amount to increase
     * @throws IllegalArgumentException if amount is negative
     */
    public void increaseQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.quantity += amount;
    }

    /**
     * Check if two sweets are equal based on ID
     * @param o object to compare
     * @return true if IDs match
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sweet sweet = (Sweet) o;
        return id == sweet.id;
    }

    /**
     * Generate hash code based on ID
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * String representation of the sweet
     * @return formatted string with sweet details
     */
    @Override
    public String toString() {
        return String.format("Sweet[id=%d, name='%s', category='%s', price=%.2f, quantity=%d]",
                id, name, category, price, quantity);
    }
}