package com.sweetshop.service;

import com.sweetshop.exception.InsufficientStockException;
import com.sweetshop.model.Sweet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 Test Suite for SweetShop Service
 * Tests all CRUD operations, search, purchase, and restock functionality
 * 
 * @author Sweet Shop Management System
 * @version 1.0
 */
class SweetShopTest {
    private SweetShop shop;

    @BeforeEach
    void setUp() {
        shop = new SweetShop();
    }

    // ==================== Add Sweet Tests ====================
    
    @Test
    @DisplayName("Test adding a sweet to shop")
    void testAddSweet() {
        Sweet sweet = new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20);
        shop.addSweet(sweet);
        
        assertEquals(1, shop.getInventorySize());
        assertEquals(sweet, shop.getSweet(1001));
    }

    @Test
    @DisplayName("Test adding sweet with auto-generated ID")
    void testAddSweetWithAutoId() {
        Sweet sweet = shop.addSweet("Gulab Jamun", "Milk-Based", 10.0, 50);
        
        assertNotNull(sweet);
        assertEquals(1, shop.getInventorySize());
        assertTrue(sweet.getId() >= 1001);
    }

    @Test
    @DisplayName("Test adding null sweet throws exception")
    void testAddNullSweetThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> shop.addSweet(null));
    }

    @Test
    @DisplayName("Test adding duplicate ID throws exception")
    void testAddDuplicateIdThrowsException() {
        Sweet sweet1 = new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20);
        Sweet sweet2 = new Sweet(1001, "Different Sweet", "Chocolate", 30.0, 10);
        
        shop.addSweet(sweet1);
        assertThrows(IllegalArgumentException.class, () -> shop.addSweet(sweet2));
    }

    // ==================== Delete Sweet Tests ====================
    
    @Test
    @DisplayName("Test deleting a sweet from shop")
    void testDeleteSweet() {
        Sweet sweet = new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20);
        shop.addSweet(sweet);
        
        assertTrue(shop.deleteSweet(1001));
        assertEquals(0, shop.getInventorySize());
        assertNull(shop.getSweet(1001));
    }

    @Test
    @DisplayName("Test deleting non-existent sweet returns false")
    void testDeleteNonExistentSweet() {
        assertFalse(shop.deleteSweet(9999));
    }

    // ==================== View Sweet Tests ====================
    
    @Test
    @DisplayName("Test viewing all sweets")
    void testGetAllSweets() {
        shop.addSweet(new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20));
        shop.addSweet(new Sweet(1002, "Gulab Jamun", "Milk-Based", 10.0, 50));
        
        List<Sweet> sweets = shop.getAllSweets();
        assertEquals(2, sweets.size());
    }

    @Test
    @DisplayName("Test viewing empty inventory")
    void testGetAllSweetsEmpty() {
        List<Sweet> sweets = shop.getAllSweets();
        assertTrue(sweets.isEmpty());
    }

    // ==================== Search Tests ====================
    
    @Test
    @DisplayName("Test search by name")
    void testSearchByName() {
        shop.addSweet(new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20));
        shop.addSweet(new Sweet(1002, "Kaju Barfi", "Nut-Based", 45.0, 15));
        shop.addSweet(new Sweet(1003, "Gulab Jamun", "Milk-Based", 10.0, 50));
        
        List<Sweet> results = shop.searchByName("Kaju");
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Test search by name case insensitive")
    void testSearchByNameCaseInsensitive() {
        shop.addSweet(new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20));
        
        List<Sweet> results = shop.searchByName("kaju");
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("Test search by category")
    void testSearchByCategory() {
        shop.addSweet(new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20));
        shop.addSweet(new Sweet(1002, "Almond Barfi", "Nut-Based", 60.0, 10));
        shop.addSweet(new Sweet(1003, "Gulab Jamun", "Milk-Based", 10.0, 50));
        
        List<Sweet> results = shop.searchByCategory("Nut-Based");
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Test search by price range")
    void testSearchByPriceRange() {
        shop.addSweet(new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20));
        shop.addSweet(new Sweet(1002, "Gajar Halwa", "Vegetable-Based", 30.0, 15));
        shop.addSweet(new Sweet(1003, "Gulab Jamun", "Milk-Based", 10.0, 50));
        
        List<Sweet> results = shop.searchByPriceRange(20.0, 40.0);
        assertEquals(1, results.size());
        assertEquals("Gajar Halwa", results.get(0).getName());
    }

    @Test
    @DisplayName("Test search by invalid price range throws exception")
    void testSearchByInvalidPriceRange() {
        assertThrows(IllegalArgumentException.class, () -> shop.searchByPriceRange(50.0, 20.0));
    }

    // ==================== Purchase Tests ====================
    
    @Test
    @DisplayName("Test purchasing sweet with sufficient stock")
    void testPurchaseSweetWithSufficientStock() {
        Sweet sweet = new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20);
        shop.addSweet(sweet);
        
        shop.purchaseSweet(1001, 5);
        
        assertEquals(15, shop.getSweet(1001).getQuantity());
    }

    @Test
    @DisplayName("Test purchasing sweet with insufficient stock throws exception")
    void testPurchaseSweetWithInsufficientStock() {
        Sweet sweet = new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 5);
        shop.addSweet(sweet);
        
        assertThrows(InsufficientStockException.class, () -> shop.purchaseSweet(1001, 10));
    }

    @Test
    @DisplayName("Test purchasing entire stock")
    void testPurchaseEntireStock() {
        Sweet sweet = new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20);
        shop.addSweet(sweet);
        
        shop.purchaseSweet(1001, 20);
        
        assertEquals(0, shop.getSweet(1001).getQuantity());
    }

    @Test
    @DisplayName("Test purchasing non-existent sweet throws exception")
    void testPurchaseNonExistentSweet() {
        assertThrows(IllegalArgumentException.class, () -> shop.purchaseSweet(9999, 5));
    }

    @Test
    @DisplayName("Test purchasing zero or negative quantity throws exception")
    void testPurchaseInvalidQuantity() {
        Sweet sweet = new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20);
        shop.addSweet(sweet);
        
        assertThrows(IllegalArgumentException.class, () -> shop.purchaseSweet(1001, 0));
        assertThrows(IllegalArgumentException.class, () -> shop.purchaseSweet(1001, -5));
    }

    // ==================== Restock Tests ====================
    
    @Test
    @DisplayName("Test restocking sweet")
    void testRestockSweet() {
        Sweet sweet = new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 5);
        shop.addSweet(sweet);
        
        shop.restockSweet(1001, 15);
        
        assertEquals(20, shop.getSweet(1001).getQuantity());
    }

    @Test
    @DisplayName("Test restocking non-existent sweet throws exception")
    void testRestockNonExistentSweet() {
        assertThrows(IllegalArgumentException.class, () -> shop.restockSweet(9999, 10));
    }

    @Test
    @DisplayName("Test restocking with invalid quantity throws exception")
    void testRestockInvalidQuantity() {
        Sweet sweet = new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20);
        shop.addSweet(sweet);
        
        assertThrows(IllegalArgumentException.class, () -> shop.restockSweet(1001, 0));
        assertThrows(IllegalArgumentException.class, () -> shop.restockSweet(1001, -5));
    }

    // ==================== Sorting Tests ====================
    
    @Test
    @DisplayName("Test sorting sweets by name")
    void testGetSweetsSortedByName() {
        shop.addSweet(new Sweet(1001, "Zebra Cake", "Pastry", 40.0, 10));
        shop.addSweet(new Sweet(1002, "Apple Pie", "Pastry", 35.0, 8));
        shop.addSweet(new Sweet(1003, "Mango Barfi", "Milk-Based", 45.0, 12));
        
        List<Sweet> sorted = shop.getSweetsSortedByName();
        
        assertEquals("Apple Pie", sorted.get(0).getName());
        assertEquals("Mango Barfi", sorted.get(1).getName());
        assertEquals("Zebra Cake", sorted.get(2).getName());
    }

    @Test
    @DisplayName("Test sorting sweets by price")
    void testGetSweetsSortedByPrice() {
        shop.addSweet(new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20));
        shop.addSweet(new Sweet(1002, "Gajar Halwa", "Vegetable-Based", 30.0, 15));
        shop.addSweet(new Sweet(1003, "Gulab Jamun", "Milk-Based", 10.0, 50));
        
        List<Sweet> sorted = shop.getSweetsSortedByPrice();
        
        assertEquals(10.0, sorted.get(0).getPrice());
        assertEquals(30.0, sorted.get(1).getPrice());
        assertEquals(50.0, sorted.get(2).getPrice());
    }

    // ==================== Sweet Model Tests ====================
    
    @Test
    @DisplayName("Test creating sweet with valid data")
    void testCreateSweet() {
        Sweet sweet = new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, 20);
        
        assertEquals(1001, sweet.getId());
        assertEquals("Kaju Katli", sweet.getName());
        assertEquals("Nut-Based", sweet.getCategory());
        assertEquals(50.0, sweet.getPrice());
        assertEquals(20, sweet.getQuantity());
    }

    @Test
    @DisplayName("Test creating sweet with invalid data throws exception")
    void testCreateSweetWithInvalidData() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Sweet(1001, "", "Nut-Based", 50.0, 20));
        assertThrows(IllegalArgumentException.class, 
            () -> new Sweet(1001, null, "Nut-Based", 50.0, 20));
        assertThrows(IllegalArgumentException.class, 
            () -> new Sweet(1001, "Kaju Katli", "Nut-Based", -10.0, 20));
        assertThrows(IllegalArgumentException.class, 
            () -> new Sweet(1001, "Kaju Katli", "Nut-Based", 50.0, -5));
    }
}