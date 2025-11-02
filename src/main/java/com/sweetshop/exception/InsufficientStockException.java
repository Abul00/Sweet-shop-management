package com.sweetshop.exception;

/**
 * Custom Exception for Insufficient Stock
 * Thrown when attempting to purchase more sweets than available in stock
 * 
 * @author Sweet Shop Management System
 * @version 1.0
 */
public class InsufficientStockException extends RuntimeException {
    
    /**
     * Constructor with error message
     * @param message detailed error message
     */
    public InsufficientStockException(String message) {
        super(message);
    }
    
    /**
     * Constructor with error message and cause
     * @param message detailed error message
     * @param cause the cause of the exception
     */
    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
}