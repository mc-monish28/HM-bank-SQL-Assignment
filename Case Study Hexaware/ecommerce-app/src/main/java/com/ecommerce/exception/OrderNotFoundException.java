package com.ecommerce.exception;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException() {
        super("Order not found in the database");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
} 