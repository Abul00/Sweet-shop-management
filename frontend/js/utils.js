const Utils = (function() {
    'use strict';
    
    /**
     * Filter sweets based on search criteria
     * @param {Array} sweets - Array of sweets to filter
     * @param {Object} filters - Filter criteria
     * @returns {Array} Filtered sweets
     */
    function filterSweets(sweets, filters) {
        return sweets.filter(sweet => {
            // Name search (case-insensitive)
            const matchesName = sweet.name
                .toLowerCase()
                .includes(filters.searchTerm.toLowerCase());
            
            // Category filter
            const matchesCategory = 
                filters.category === 'all' || 
                sweet.category === filters.category;
            
            // Price range filter
            const minPrice = filters.minPrice !== '' ? parseFloat(filters.minPrice) : 0;
            const maxPrice = filters.maxPrice !== '' ? parseFloat(filters.maxPrice) : Infinity;
            const matchesPrice = sweet.price >= minPrice && sweet.price <= maxPrice;
            
            return matchesName && matchesCategory && matchesPrice;
        });
    }
    
    /**
     * Sort sweets by given criteria
     * @param {Array} sweets - Array of sweets to sort
     * @param {string} sortBy - Sort criteria (name, price, quantity)
     * @param {string} order - Sort order (asc, desc)
     * @returns {Array} Sorted sweets
     */
    function sortSweets(sweets, sortBy, order = 'asc') {
        const sorted = [...sweets].sort((a, b) => {
            let comparison = 0;
            
            switch (sortBy) {
                case 'name':
                    comparison = a.name.localeCompare(b.name);
                    break;
                case 'price':
                    comparison = a.price - b.price;
                    break;
                case 'quantity':
                    comparison = a.quantity - b.quantity;
                    break;
                case 'category':
                    comparison = a.category.localeCompare(b.category);
                    break;
                default:
                    comparison = 0;
            }
            
            return order === 'asc' ? comparison : -comparison;
        });
        
        return sorted;
    }
    
    /**
     * Format currency value
     * @param {number} value - Numeric value
     * @returns {string} Formatted currency string
     */
    function formatCurrency(value) {
        return `₹${parseFloat(value).toFixed(2)}`;
    }
    
    /**
     * Validate sweet input data
     * @param {Object} sweet - Sweet object to validate
     * @returns {Object} Validation result with isValid and errors
     */
    function validateSweet(sweet) {
        const errors = [];
        
        if (!sweet.name || sweet.name.trim() === '') {
            errors.push('Sweet name is required');
        }
        
        if (!sweet.category || sweet.category.trim() === '') {
            errors.push('Category is required');
        }
        
        if (!sweet.price || isNaN(sweet.price) || parseFloat(sweet.price) < 0) {
            errors.push('Valid price is required');
        }
        
        if (!sweet.quantity || isNaN(sweet.quantity) || parseInt(sweet.quantity, 10) < 0) {
            errors.push('Valid quantity is required');
        }
        
        return {
            isValid: errors.length === 0,
            errors: errors
        };
    }
    
    /**
     * Debounce function to limit execution rate
     * @param {Function} func - Function to debounce
     * @param {number} wait - Wait time in milliseconds
     * @returns {Function} Debounced function
     */
    function debounce(func, wait = 300) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }
    
    /**
     * Get stock status color class
     * @param {number} quantity - Stock quantity
     * @returns {string} CSS class name
     */
    function getStockStatusClass(quantity) {
        if (quantity < 10) return 'stock-low';
        return 'stock-ok';
    }
    
    /**
     * Escape HTML to prevent XSS
     * @param {string} text - Text to escape
     * @returns {string} Escaped text
     */
    function escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
    
    /**
     * Generate unique ID
     * @returns {string} Unique ID
     */
    function generateId() {
        return Date.now() + Math.random().toString(36).substr(2, 9);
    }
    
    /**
     * Check if value is empty
     * @param {*} value - Value to check
     * @returns {boolean} Is empty
     */
    function isEmpty(value) {
        return value === null || 
               value === undefined || 
               value === '' || 
               (Array.isArray(value) && value.length === 0);
    }
    
    /**
     * Deep clone an object
     * @param {Object} obj - Object to clone
     * @returns {Object} Cloned object
     */
    function deepClone(obj) {
        return JSON.parse(JSON.stringify(obj));
    }
    
    /**
     * Calculate total inventory value
     * @param {Array} sweets - Array of sweets
     * @returns {number} Total value
     */
    function calculateTotalValue(sweets) {
        return sweets.reduce((total, sweet) => {
            return total + (sweet.price * sweet.quantity);
        }, 0);
    }
    
    /**
     * Get statistics about inventory
     * @param {Array} sweets - Array of sweets
     * @returns {Object} Statistics object
     */
    function getInventoryStats(sweets) {
        return {
            totalItems: sweets.length,
            totalQuantity: sweets.reduce((sum, s) => sum + s.quantity, 0),
            totalValue: calculateTotalValue(sweets),
            lowStockCount: sweets.filter(s => s.quantity < 10).length,
            categories: [...new Set(sweets.map(s => s.category))].length
        };
    }
    
    /**
     * Format date to readable string
     * @param {Date} date - Date object
     * @returns {string} Formatted date
     */
    function formatDate(date) {
        return new Intl.DateTimeFormat('en-IN', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        }).format(date);
    }
    
    // Public API
    return {
        filterSweets,
        sortSweets,
        formatCurrency,
        validateSweet,
        debounce,
        getStockStatusClass,
        escapeHtml,
        generateId,
        isEmpty,
        deepClone,
        calculateTotalValue,
        getInventoryStats,
        formatDate
    };
})();