package com.ecommerce.test;

import com.ecommerce.dao.OrderProcessorRepository;
import com.ecommerce.dao.OrderProcessorRepositoryImpl;
import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.CustomerNotFoundException;
import com.ecommerce.exception.ProductNotFoundException;
import org.junit.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class EcommerceTest {
    private static OrderProcessorRepository repository;
    private static Customer testCustomer;
    private static Product testProduct;
    private static Connection connection;

    @BeforeClass
    public static void setUpClass() {
        repository = new OrderProcessorRepositoryImpl();
        connection = repository.getConnection();
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        // Generate unique test data
        String uniqueEmail = "test" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        String uniqueProductName = "TestProduct" + UUID.randomUUID().toString().substring(0, 8);

        // Clean up any existing test data
        try {
            // Delete in correct order to handle foreign key constraints
            String[] cleanupQueries = {
                "DELETE FROM order_items WHERE order_id IN (SELECT order_id FROM orders WHERE customer_id IN (SELECT customer_id FROM customers WHERE email LIKE 'test%@example.com'))",
                "DELETE FROM orders WHERE customer_id IN (SELECT customer_id FROM customers WHERE email LIKE 'test%@example.com')",
                "DELETE FROM cart WHERE customer_id IN (SELECT customer_id FROM customers WHERE email LIKE 'test%@example.com')",
                "DELETE FROM customers WHERE email LIKE 'test%@example.com'",
                "DELETE FROM products WHERE name LIKE 'TestProduct%'"
            };

            for (String query : cleanupQueries) {
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create new test customer
        testCustomer = new Customer();
        testCustomer.setName("Test User");
        testCustomer.setEmail(uniqueEmail);
        testCustomer.setPassword("password123");

        // Create new test product
        testProduct = new Product();
        testProduct.setName(uniqueProductName);
        testProduct.setPrice(100.0);
        testProduct.setDescription("Test Product Description");
        testProduct.setStockQuantity(10);

        // Create customer and product
        assertTrue("Customer creation failed", repository.createCustomer(testCustomer));
        assertTrue("Product creation failed", repository.createProduct(testProduct));
        
        // Verify IDs are set
        assertTrue("Customer ID not set", testCustomer.getCustomerId() > 0);
        assertTrue("Product ID not set", testProduct.getProductId() > 0);
    }

    @After
    public void tearDown() {
        // Clean up test data
        try {
            if (testCustomer != null && testCustomer.getCustomerId() > 0) {
                // Delete in correct order to handle foreign key constraints
                String[] cleanupQueries = {
                    "DELETE FROM order_items WHERE order_id IN (SELECT order_id FROM orders WHERE customer_id = ?)",
                    "DELETE FROM orders WHERE customer_id = ?",
                    "DELETE FROM cart WHERE customer_id = ?",
                    "DELETE FROM customers WHERE customer_id = ?",
                    "DELETE FROM products WHERE product_id = ?"
                };

                for (String query : cleanupQueries) {
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        if (query.contains("customer_id")) {
                            stmt.setInt(1, testCustomer.getCustomerId());
                        } else if (query.contains("product_id")) {
                            stmt.setInt(1, testProduct.getProductId());
                        }
                        stmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateProduct() {
        Product newProduct = new Product();
        newProduct.setName("New Test Product");
        newProduct.setPrice(50.0);
        newProduct.setDescription("New Test Product Description");
        newProduct.setStockQuantity(5);

        assertTrue(repository.createProduct(newProduct));
        assertTrue(newProduct.getProductId() > 0);
    }

    @Test
    public void testAddToCart() {
        try {
            assertTrue(repository.addToCart(testCustomer, testProduct, 2));
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testPlaceOrder() {
        try {
            List<Map<Product, Integer>> products = new ArrayList<>();
            Map<Product, Integer> productMap = new HashMap<>();
            productMap.put(testProduct, 1);
            products.add(productMap);

            assertTrue(repository.placeOrder(testCustomer, products, "Test Address"));
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetOrdersByCustomer() {
        try {
            List<Map<Product, Integer>> orders = repository.getOrdersByCustomer(testCustomer.getCustomerId());
            assertNotNull(orders);
        } catch (CustomerNotFoundException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveFromCart() {
        try {
            repository.addToCart(testCustomer, testProduct, 1);
            assertTrue(repository.removeFromCart(testCustomer, testProduct));
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllFromCart() {
        try {
            repository.addToCart(testCustomer, testProduct, 1);
            List<Product> cartProducts = repository.getAllFromCart(testCustomer);
            assertNotNull(cartProducts);
            assertFalse(cartProducts.isEmpty());
        } catch (CustomerNotFoundException | ProductNotFoundException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteProduct() {
        try {
            assertTrue(repository.deleteProduct(testProduct.getProductId()));
        } catch (ProductNotFoundException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteCustomer() {
        try {
            assertTrue(repository.deleteCustomer(testCustomer.getCustomerId()));
        } catch (CustomerNotFoundException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}