package com.ecommerce.dao;

import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.CustomerNotFoundException;
import com.ecommerce.exception.ProductNotFoundException;

import java.util.List;
import java.util.Map;

public interface OrderProcessorRepository {
    boolean createProduct(Product product);
    boolean createCustomer(Customer customer);
    boolean deleteProduct(int productId) throws ProductNotFoundException;
    boolean deleteCustomer(int customerId) throws CustomerNotFoundException;
    boolean addToCart(Customer customer, Product product, int quantity) throws CustomerNotFoundException, ProductNotFoundException;
    boolean removeFromCart(Customer customer, Product product) throws CustomerNotFoundException, ProductNotFoundException;
    List<Product> getAllFromCart(Customer customer) throws CustomerNotFoundException;
    boolean placeOrder(Customer customer, List<Map<Product, Integer>> products, String shippingAddress) throws CustomerNotFoundException, ProductNotFoundException;
    List<Map<Product, Integer>> getOrdersByCustomer(int customerId) throws CustomerNotFoundException;
} 