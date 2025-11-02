const UI = (function() {
    'use strict';
    
    /**
     * Show notification message with icon
     */
    function showNotification(message, type = 'success') {
        const notification = document.getElementById('notification');
        notification.className = `notification ${type}`;
        
        // Create icon SVG
        const iconSVG = type === 'success' 
            ? `<svg class="notification-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                 <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                 <polyline points="22 4 12 14.01 9 11.01"></polyline>
               </svg>`
            : `<svg class="notification-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                 <circle cx="12" cy="12" r="10"></circle>
                 <line x1="12" y1="8" x2="12" y2="12"></line>
                 <line x1="12" y1="16" x2="12.01" y2="16"></line>
               </svg>`;
        
        notification.innerHTML = iconSVG + `<span>${Utils.escapeHtml(message)}</span>`;
        notification.classList.remove('hidden');
        
        // Auto hide after 3 seconds
        setTimeout(() => {
            notification.classList.add('hidden');
        }, 3000);
    }
    
    /**
     * Toggle form visibility
     */
    function toggleAddForm(show) {
        const form = document.getElementById('addSweetForm');
        if (show) {
            form.classList.remove('hidden');
            document.getElementById('sweetName').focus();
        } else {
            form.classList.add('hidden');
            clearForm();
        }
    }
    
    /**
     * Clear form inputs
     */
    function clearForm() {
        document.getElementById('sweetName').value = '';
        document.getElementById('sweetCategory').value = 'Chocolate';
        document.getElementById('sweetPrice').value = '';
        document.getElementById('sweetQuantity').value = '';
    }
    
    /**
     * Get form data
     */
    function getFormData() {
        return {
            name: document.getElementById('sweetName').value.trim(),
            category: document.getElementById('sweetCategory').value,
            price: document.getElementById('sweetPrice').value,
            quantity: document.getElementById('sweetQuantity').value
        };
    }
    
    /**
     * Render all sweets in the grid
     */
    function renderSweets(sweets) {
        const grid = document.getElementById('sweetsGrid');
        const emptyState = document.getElementById('emptyState');
        
        if (sweets.length === 0) {
            grid.innerHTML = '';
            emptyState.classList.remove('hidden');
            return;
        }
        
        emptyState.classList.add('hidden');
        grid.innerHTML = sweets.map(sweet => createSweetCard(sweet)).join('');
    }
    
    /**
     * Create HTML for a single sweet card matching React design
     */
    function createSweetCard(sweet) {
        const stockClass = sweet.quantity < 10 ? 'stock-low' : 'stock-ok';
        const escapedName = Utils.escapeHtml(sweet.name);
        const escapedCategory = Utils.escapeHtml(sweet.category);
        
        return `
            <div class="sweet-card" data-id="${sweet.id}">
                <div class="card-header">
                    <h3 class="card-title">${escapedName}</h3>
                    <span class="card-badge">${escapedCategory}</span>
                </div>
                
                <div class="card-body">
                    <div class="card-info">
                        <div class="info-left">
                            <p class="info-label">Price</p>
                            <p class="info-value">₹${sweet.price}</p>
                        </div>
                        <div class="info-right">
                            <p class="info-label">Stock</p>
                            <p class="info-value ${stockClass}">${sweet.quantity}</p>
                        </div>
                    </div>
                    
                    <div class="card-actions">
                        <div class="action-row">
                            <input type="number" 
                                   class="qty-input purchase-qty" 
                                   value="1" 
                                   min="1" 
                                   data-id="${sweet.id}">
                            <button class="btn btn-purchase purchase-btn" data-id="${sweet.id}">
                                <svg class="icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <circle cx="9" cy="21" r="1"></circle>
                                    <circle cx="20" cy="21" r="1"></circle>
                                    <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
                                </svg>
                                Purchase
                            </button>
                        </div>
                        
                        <div class="action-row">
                            <input type="number" 
                                   class="qty-input restock-qty" 
                                   value="10" 
                                   min="1" 
                                   data-id="${sweet.id}">
                            <button class="btn btn-restock restock-btn" data-id="${sweet.id}">
                                <svg class="icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <line x1="16.5" y1="9.4" x2="7.5" y2="4.21"></line>
                                    <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path>
                                    <polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline>
                                    <line x1="12" y1="22.08" x2="12" y2="12"></line>
                                </svg>
                                Restock
                            </button>
                        </div>
                        
                        <button class="btn btn-delete delete-btn" data-id="${sweet.id}">
                            <svg class="icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <polyline points="3 6 5 6 21 6"></polyline>
                                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                            </svg>
                            Delete
                        </button>
                    </div>
                </div>
                
                <div class="card-footer">
                    ID: ${sweet.id}
                </div>
            </div>
        `;
    }
    
    /**
     * Update category filter dropdown
     */
    function updateCategoryFilter(categories) {
        const filter = document.getElementById('categoryFilter');
        const currentValue = filter.value;
        
        filter.innerHTML = '<option value="all">All Categories</option>' +
            categories.map(cat => 
                `<option value="${Utils.escapeHtml(cat)}">${Utils.escapeHtml(cat)}</option>`
            ).join('');
        
        // Restore previous selection if still valid
        if (categories.includes(currentValue)) {
            filter.value = currentValue;
        }
    }
    
    /**
     * Get current filter values
     */
    function getFilterValues() {
        return {
            searchTerm: document.getElementById('searchInput').value,
            category: document.getElementById('categoryFilter').value,
            minPrice: document.getElementById('minPrice').value,
            maxPrice: document.getElementById('maxPrice').value
        };
    }
    
    /**
     * Get purchase quantity for a sweet
     */
    function getPurchaseQuantity(id) {
        const input = document.querySelector(`.purchase-qty[data-id="${id}"]`);
        return parseInt(input.value, 10) || 1;
    }
    
    /**
     * Get restock quantity for a sweet
     */
    function getRestockQuantity(id) {
        const input = document.querySelector(`.restock-qty[data-id="${id}"]`);
        return parseInt(input.value, 10) || 1;
    }
    
    /**
     * Confirm deletion
     */
    function confirmDelete(sweetName) {
        return confirm(`Are you sure you want to delete "${sweetName}"?`);
    }
    
    /**
     * Display error messages
     */
    function displayErrors(errors) {
        const errorMessage = errors.join(', ');
        showNotification(errorMessage, 'error');
    }
    
    // Public API
    return {
        showNotification,
        toggleAddForm,
        clearForm,
        getFormData,
        renderSweets,
        updateCategoryFilter,
        getFilterValues,
        getPurchaseQuantity,
        getRestockQuantity,
        confirmDelete,
        displayErrors
    };
})();