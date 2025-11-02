(function() {
    'use strict';
    
    // Application state
    let isFormOpen = false;
    
    /**
     * Initialize the application
     */
    function init() {
        setupEventListeners();
        loadAndRenderSweets();
        updateCategoryDropdown();
    }
    
    /**
     * Setup all event listeners
     */
    function setupEventListeners() {
        // Add Sweet Button
        document.getElementById('addSweetBtn').addEventListener('click', handleAddButtonClick);
        
        // Form Submit
        document.getElementById('submitSweet').addEventListener('click', handleSubmitSweet);
        
        // Form Cancel
        document.getElementById('cancelAdd').addEventListener('click', handleCancelAdd);
        
        // Search Input (with debounce)
        document.getElementById('searchInput').addEventListener('input', 
            Utils.debounce(handleFilterChange, 300));
        
        // Category Filter
        document.getElementById('categoryFilter').addEventListener('change', handleFilterChange);
        
        // Price Filters (with debounce)
        document.getElementById('minPrice').addEventListener('input', 
            Utils.debounce(handleFilterChange, 300));
        document.getElementById('maxPrice').addEventListener('input', 
            Utils.debounce(handleFilterChange, 300));
        
        // Event delegation for sweet card buttons
        document.getElementById('sweetsGrid').addEventListener('click', handleCardActions);
        
        // Keyboard shortcuts
        document.addEventListener('keydown', handleKeyboardShortcuts);
    }
    
    /**
     * Handle Add Sweet button click
     */
    function handleAddButtonClick() {
        isFormOpen = !isFormOpen;
        UI.toggleAddForm(isFormOpen);
        UI.updateAddButtonText(isFormOpen);
        
        if (isFormOpen) {
            document.getElementById('sweetName').focus();
            UI.scrollToTop();
        }
    }
    
    /**
     * Handle form submission
     */
    function handleSubmitSweet() {
        const formData = UI.getFormData();
        
        // Validate input
        const validation = Utils.validateSweet(formData);
        if (!validation.isValid) {
            UI.displayErrors(validation.errors);
            return;
        }
        
        // Add sweet to storage
        const newSweet = Storage.addSweet(formData);
        
        if (newSweet) {
            UI.showNotification(`${newSweet.name} added successfully!`, 'success');
            isFormOpen = false;
            UI.toggleAddForm(false);
            UI.updateAddButtonText(false);
            loadAndRenderSweets();
            updateCategoryDropdown();
        } else {
            UI.showNotification('Failed to add sweet. Please try again.', 'error');
        }
    }
    
    /**
     * Handle form cancel
     */
    function handleCancelAdd() {
        isFormOpen = false;
        UI.toggleAddForm(false);
        UI.updateAddButtonText(false);
    }
    
    /**
     * Handle filter changes
     */
    function handleFilterChange() {
        loadAndRenderSweets();
    }
    
    /**
     * Handle card action buttons (purchase, restock, delete)
     */
    function handleCardActions(event) {
        const target = event.target.closest('button');
        if (!target) return;
        
        const sweetId = parseInt(target.dataset.id, 10);
        
        if (target.classList.contains('purchase-btn')) {
            handlePurchase(sweetId);
        } else if (target.classList.contains('restock-btn')) {
            handleRestock(sweetId);
        } else if (target.classList.contains('delete-btn')) {
            handleDelete(sweetId);
        }
    }
    
    /**
     * Handle purchase action
     * @param {number} id - Sweet ID
     */
    function handlePurchase(id) {
        const quantity = UI.getPurchaseQuantity(id);
        const result = Storage.purchaseSweet(id, quantity);
        
        if (result.success) {
            UI.showNotification(result.message, 'success');
            loadAndRenderSweets();
        } else {
            UI.showNotification(result.message, 'error');
        }
    }
    
    /**
     * Handle restock action
     * @param {number} id - Sweet ID
     */
    function handleRestock(id) {
        const quantity = UI.getRestockQuantity(id);
        const result = Storage.restockSweet(id, quantity);
        
        if (result.success) {
            UI.showNotification(result.message, 'success');
            loadAndRenderSweets();
        } else {
            UI.showNotification(result.message, 'error');
        }
    }
    
    /**
     * Handle delete action
     * @param {number} id - Sweet ID
     */
    function handleDelete(id) {
        const sweets = Storage.getSweets();
        const sweet = sweets.find(s => s.id === id);
        
        if (!sweet) return;
        
        if (UI.confirmDelete(sweet.name)) {
            const success = Storage.deleteSweet(id);
            
            if (success) {
                UI.showNotification(`${sweet.name} deleted successfully!`, 'success');
                loadAndRenderSweets();
                updateCategoryDropdown();
            } else {
                UI.showNotification('Failed to delete sweet. Please try again.', 'error');
            }
        }
    }
    
    /**
     * Load sweets and render with current filters
     */
    function loadAndRenderSweets() {
        const allSweets = Storage.getSweets();
        const filters = UI.getFilterValues();
        const filteredSweets = Utils.filterSweets(allSweets, filters);
        const sortedSweets = Utils.sortSweets(filteredSweets, 'name', 'asc');
        
        UI.renderSweets(sortedSweets);
    }
    
    /**
     * Update category dropdown with current categories
     */
    function updateCategoryDropdown() {
        const categories = Storage.getCategories();
        UI.updateCategoryFilter(categories);
    }
    
    /**
     * Handle keyboard shortcuts
     * @param {KeyboardEvent} event - Keyboard event
     */
    function handleKeyboardShortcuts(event) {
        // Ctrl/Cmd + K: Focus search
        if ((event.ctrlKey || event.metaKey) && event.key === 'k') {
            event.preventDefault();
            document.getElementById('searchInput').focus();
        }
        
        // Ctrl/Cmd + N: Toggle add form
        if ((event.ctrlKey || event.metaKey) && event.key === 'n') {
            event.preventDefault();
            handleAddButtonClick();
        }
        
        // Escape: Close form
        if (event.key === 'Escape' && isFormOpen) {
            handleCancelAdd();
        }
    }
    
    /**
     * Log inventory statistics (for debugging)
     */
    function logInventoryStats() {
        const sweets = Storage.getSweets();
        const stats = Utils.getInventoryStats(sweets);
        
        console.log('=== Inventory Statistics ===');
        console.log(`Total Items: ${stats.totalItems}`);
        console.log(`Total Quantity: ${stats.totalQuantity}`);
        console.log(`Total Value: ${Utils.formatCurrency(stats.totalValue)}`);
        console.log(`Low Stock Count: ${stats.lowStockCount}`);
        console.log(`Categories: ${stats.categories}`);
        console.log('===========================');
    }
    
    /**
     * Export data (for backup)
     */
    function exportData() {
        const sweets = Storage.getSweets();
        const dataStr = JSON.stringify(sweets, null, 2);
        const dataBlob = new Blob([dataStr], { type: 'application/json' });
        const url = URL.createObjectURL(dataBlob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `sweetshop-backup-${Date.now()}.json`;
        link.click();
        URL.revokeObjectURL(url);
    }
    
    // Expose utility functions to window for console access
    window.SweetShop = {
        stats: logInventoryStats,
        export: exportData,
        clear: Storage.clearAll,
        version: '1.0.0'
    };
    
    // Initialize app when DOM is ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
    
    // Log welcome message
    console.log('%c🍬 Sweet Shop Management System', 
        'font-size: 20px; font-weight: bold; color: #ec4899;');
    console.log('Version 1.0.0');
    console.log('Type SweetShop.stats() to see inventory statistics');
    console.log('Type SweetShop.export() to export data');
    console.log('Keyboard shortcuts:');
    console.log('  Ctrl/Cmd + K: Focus search');
    console.log('  Ctrl/Cmd + N: Toggle add form');
    console.log('  Escape: Close form');
})();