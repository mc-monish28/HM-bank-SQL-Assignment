package com.ecommerce.exception;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException() {
        super("Customer not found in the database");
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
} 