const Storage = (function() {
    'use strict';
    
    const STORAGE_KEY = 'sweetshop_inventory';
    
    /**
     * Get all sweets from localStorage
     * @returns {Array} Array of sweet objects
     */
    function getSweets() {
        try {
            const data = localStorage.getItem(STORAGE_KEY);
            return data ? JSON.parse(data) : getInitialData();
        } catch (error) {
            console.error('Error loading sweets:', error);
            return getInitialData();
        }
    }
    
    /**
     * Save sweets to localStorage
     * @param {Array} sweets - Array of sweet objects to save
     * @returns {boolean} Success status
     */
    function saveSweets(sweets) {
        try {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(sweets));
            return true;
        } catch (error) {
            console.error('Error saving sweets:', error);
            return false;
        }
    }
    
    /**
     * Get initial sample data
     * @returns {Array} Initial sweet data
     */
    function getInitialData() {
        const initialData = [
            {
                id: 1001,
                name: 'Kaju Katli',
                category: 'Nut-Based',
                price: 50,
                quantity: 20
            },
            {
                id: 1002,
                name: 'Gajar Halwa',
                category: 'Vegetable-Based',
                price: 30,
                quantity: 15
            },
            {
                id: 1003,
                name: 'Gulab Jamun',
                category: 'Milk-Based',
                price: 10,
                quantity: 50
            }
        ];
        
        saveSweets(initialData);
        return initialData;
    }
    
    /**
     * Add a new sweet
     * @param {Object} sweet - Sweet object to add
     * @returns {Object|null} Added sweet or null if failed
     */
    function addSweet(sweet) {
        try {
            const sweets = getSweets();
            const newSweet = {
                id: getNextId(sweets),
                name: sweet.name,
                category: sweet.category,
                price: parseFloat(sweet.price),
                quantity: parseInt(sweet.quantity, 10)
            };
            
            sweets.push(newSweet);
            saveSweets(sweets);
            return newSweet;
        } catch (error) {
            console.error('Error adding sweet:', error);
            return null;
        }
    }
    
    /**
     * Delete a sweet by ID
     * @param {number} id - Sweet ID to delete
     * @returns {boolean} Success status
     */
    function deleteSweet(id) {
        try {
            let sweets = getSweets();
            sweets = sweets.filter(sweet => sweet.id !== id);
            return saveSweets(sweets);
        } catch (error) {
            console.error('Error deleting sweet:', error);
            return false;
        }
    }
    
    /**
     * Purchase sweet (decrease quantity)
     * @param {number} id - Sweet ID
     * @param {number} quantity - Quantity to purchase
     * @returns {Object} Result object with success status and message
     */
    function purchaseSweet(id, quantity) {
        try {
            const sweets = getSweets();
            const sweet = sweets.find(s => s.id === id);
            
            if (!sweet) {
                return { success: false, message: 'Sweet not found' };
            }
            
            if (sweet.quantity < quantity) {
                return { 
                    success: false, 
                    message: `Insufficient stock! Only ${sweet.quantity} available` 
                };
            }
            
            sweet.quantity -= quantity;
            saveSweets(sweets);
            return { 
                success: true, 
                message: `Purchased ${quantity} ${sweet.name}(s)` 
            };
        } catch (error) {
            console.error('Error purchasing sweet:', error);
            return { success: false, message: 'Error processing purchase' };
        }
    }
    
    /**
     * Restock sweet (increase quantity)
     * @param {number} id - Sweet ID
     * @param {number} quantity - Quantity to add
     * @returns {Object} Result object with success status and message
     */
    function restockSweet(id, quantity) {
        try {
            const sweets = getSweets();
            const sweet = sweets.find(s => s.id === id);
            
            if (!sweet) {
                return { success: false, message: 'Sweet not found' };
            }
            
            sweet.quantity += quantity;
            saveSweets(sweets);
            return { 
                success: true, 
                message: 'Stock updated successfully!' 
            };
        } catch (error) {
            console.error('Error restocking sweet:', error);
            return { success: false, message: 'Error updating stock' };
        }
    }
    
    /**
     * Get next available ID
     * @param {Array} sweets - Current sweets array
     * @returns {number} Next ID
     */
    function getNextId(sweets) {
        if (sweets.length === 0) return 1001;
        const maxId = Math.max(...sweets.map(s => s.id));
        return maxId + 1;
    }
    
    /**
     * Get all unique categories
     * @returns {Array} Array of category names
     */
    function getCategories() {
        const sweets = getSweets();
        const categories = [...new Set(sweets.map(s => s.category))];
        return categories.sort();
    }
    
    /**
     * Clear all data (useful for testing)
     * @returns {boolean} Success status
     */
    function clearAll() {
        try {
            localStorage.removeItem(STORAGE_KEY);
            return true;
        } catch (error) {
            console.error('Error clearing storage:', error);
            return false;
        }
    }
    
    // Public API
    return {
        getSweets,
        saveSweets,
        addSweet,
        deleteSweet,
        purchaseSweet,
        restockSweet,
        getCategories,
        clearAll
    };
})();