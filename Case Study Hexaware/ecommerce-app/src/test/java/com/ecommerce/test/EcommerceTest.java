package com.ecommerce.test;

import com.ecommerce.dao.OrderProcessorRepository;
import com.ecommerce.dao.OrderProcessorRepositoryImpl;
import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.CustomerNotFoundException;
import com.ecommerce.exception.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class EcommerceTest {
    private OrderProcessorRepository repository;
    private Customer testCustomer;
    private Product testProduct;

    @Before
    public void setUp() {
        repository = new OrderProcessorRepositoryImpl();
        
        // Create test customer
        testCustomer = new Customer();
        testCustomer.setName("Test Customer");
        testCustomer.setEmail("test@example.com");
        testCustomer.setPassword("password");
        repository.createCustomer(testCustomer);
        
        // Create test product
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(99.99);
        testProduct.setDescription("Test Description");
        testProduct.setStockQuantity(10);
        repository.createProduct(testProduct);
    }

    @Test
    public void testCreateProduct() {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(49.99);
        newProduct.setDescription("New Description");
        newProduct.setStockQuantity(5);
        
        assertTrue("Product should be created successfully", repository.createProduct(newProduct));
    }

    @Test
    public void testAddToCart() {
        try {
            assertTrue("Product should be added to cart successfully",
                    repository.addToCart(testCustomer, testProduct, 2));
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testPlaceOrder() {
        try {
            List<Map<Product, Integer>> products = new ArrayList<>();
            Map<Product, Integer> productMap = new HashMap<>();
            productMap.put(testProduct, 1);
            products.add(productMap);
            
            assertTrue("Order should be placed successfully",
                    repository.placeOrder(testCustomer, products, "Test Address"));
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testCustomerNotFoundException() throws CustomerNotFoundException, ProductNotFoundException {
        Customer invalidCustomer = new Customer();
        invalidCustomer.setCustomerId(9999);
        repository.addToCart(invalidCustomer, testProduct, 1);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testProductNotFoundException() throws CustomerNotFoundException, ProductNotFoundException {
        Product invalidProduct = new Product();
        invalidProduct.setProductId(9999);
        repository.addToCart(testCustomer, invalidProduct, 1);
    }

    @Test
    public void testGetOrdersByCustomer() {
        try {
            List<Map<Product, Integer>> orders = repository.getOrdersByCustomer(testCustomer.getCustomerId());
            assertNotNull("Orders list should not be null", orders);
        } catch (CustomerNotFoundException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteProduct() {
        try {
            assertTrue("Product should be deleted successfully",
                    repository.deleteProduct(testProduct.getProductId()));
        } catch (ProductNotFoundException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteCustomer() {
        try {
            assertTrue("Customer should be deleted successfully",
                    repository.deleteCustomer(testCustomer.getCustomerId()));
        } catch (CustomerNotFoundException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
} 