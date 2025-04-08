package com.ecommerce.exception;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException() {
        super("Product not found in the database");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}