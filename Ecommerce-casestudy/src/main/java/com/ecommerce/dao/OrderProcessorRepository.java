package com.ecommerce.dao;

import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.CustomerNotFoundException;
import com.ecommerce.exception.ProductNotFoundException;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface OrderProcessorRepository {
    // Product Management
    boolean createProduct(Product product);
    boolean deleteProduct(int productId) throws ProductNotFoundException;
    
    // Customer Management
    boolean createCustomer(Customer customer);
    boolean deleteCustomer(int customerId) throws CustomerNotFoundException;
    boolean deleteCustomerByName(String name) throws CustomerNotFoundException;
    
    // Cart Management
    boolean addToCart(Customer customer, Product product, int quantity) throws CustomerNotFoundException, ProductNotFoundException;
    boolean removeFromCart(Customer customer, Product product) throws CustomerNotFoundException, ProductNotFoundException;
    List<Product> getAllFromCart(Customer customer) throws CustomerNotFoundException;
    
    // Order Management
    boolean placeOrder(Customer customer, List<Map<Product, Integer>> products, String shippingAddress) 
            throws CustomerNotFoundException, ProductNotFoundException;
    List<Map<Product, Integer>> getOrdersByCustomer(int customerId) throws CustomerNotFoundException;
    
    // Utility Methods
    boolean verifyCustomerExists(int customerId) throws CustomerNotFoundException;
    boolean verifyProductExists(int productId) throws ProductNotFoundException;
    Connection getConnection();
} 