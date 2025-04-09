package com.order.management;

import com.order.management.dao.IOrderManagementRepository;
import com.order.management.dao.OrderProcessor;
import com.order.management.entity.Clothing;
import com.order.management.entity.Electronics;
import com.order.management.entity.Product;
import com.order.management.entity.User;
import com.order.management.exception.OrderNotFoundException;
import com.order.management.exception.UserNotFoundException;
import com.order.management.util.DBConnUtil;
import com.order.management.util.DBPropertyUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderManagementSystemTest {
    private IOrderManagementRepository orderProcessor;
    private User adminUser;
    private User regularUser;
    private Electronics laptop;
    private Clothing tShirt;
    private Electronics smartphone;
    private Connection connection;

    @Before
    public void setUp() {
        try {
            // Initialize database connection
            String connectionString = DBPropertyUtil.getConnectionString("src/main/resources/db.properties");
            connection = DBConnUtil.getConnection(connectionString);
            
            // Clean up database before each test
            try (Statement statement = connection.createStatement()) {
                statement.execute("SET FOREIGN_KEY_CHECKS = 0");
                statement.execute("DELETE FROM order_items");
                statement.execute("DELETE FROM orders");
                statement.execute("DELETE FROM products");
                statement.execute("DELETE FROM users");
                statement.execute("SET FOREIGN_KEY_CHECKS = 1");
            }
            
            orderProcessor = new OrderProcessor();
            
            // Create test users
            adminUser = new User(1, "admin", "admin123", "Admin");
            regularUser = new User(2, "user", "user123", "User");
            
            // Create test products
            laptop = new Electronics(1, "Laptop", "High performance laptop", 999.99, 10, "Dell", 24);
            tShirt = new Clothing(2, "T-Shirt", "Cotton t-shirt", 29.99, 50, "M", "Blue");
            smartphone = new Electronics(3, "Smartphone", "Latest smartphone", 699.99, 20, "Samsung", 12);

            // Create users in database
            orderProcessor.createUser(adminUser);
            orderProcessor.createUser(regularUser);
        } catch (Exception e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        try {
            if (connection != null) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("SET FOREIGN_KEY_CHECKS = 0");
                    statement.execute("DELETE FROM order_items");
                    statement.execute("DELETE FROM orders");
                    statement.execute("DELETE FROM products");
                    statement.execute("DELETE FROM users");
                    statement.execute("SET FOREIGN_KEY_CHECKS = 1");
                }
                connection.close();
            }
        } catch (Exception e) {
            fail("Failed to clean up database: " + e.getMessage());
        }
    }

    // Database Utility Tests
    @Test
    public void testDatabaseUtilities() {
        try {
            // Test DBPropertyUtil
            String connectionString = DBPropertyUtil.getConnectionString("src/main/resources/db.properties");
            assertNotNull("Connection string should not be null", connectionString);
            assertTrue("Connection string should contain database name", connectionString.contains("order_management"));
            
            // Test DBConnUtil
            Connection testConnection = DBConnUtil.getConnection(connectionString);
            assertNotNull("Connection should not be null", testConnection);
            assertFalse("Connection should not be closed", testConnection.isClosed());
            testConnection.close();
        } catch (Exception e) {
            fail("Database utilities test failed: " + e.getMessage());
        }
    }

    // Data Validation Tests
    @Test
    public void testProductDataValidation() {
        // Test Electronics
        Electronics invalidElectronics = new Electronics(-1, "", "", -1, -1, "", -1);
        assertTrue("Product ID should be positive", invalidElectronics.getProductId() < 0);
        assertTrue("Price should be positive", invalidElectronics.getPrice() < 0);
        assertTrue("Quantity should be positive", invalidElectronics.getQuantityInStock() < 0);
        
        // Test Clothing
        Clothing invalidClothing = new Clothing(-1, "", "", -1, -1, "", "");
        assertTrue("Product ID should be positive", invalidClothing.getProductId() < 0);
        assertTrue("Price should be positive", invalidClothing.getPrice() < 0);
        assertTrue("Quantity should be positive", invalidClothing.getQuantityInStock() < 0);
    }

    @Test
    public void testUserDataValidation() {
        User invalidUser = new User(-1, "", "", "");
        assertTrue("User ID should be positive", invalidUser.getUserId() < 0);
        assertTrue("Username should not be empty", invalidUser.getUsername().isEmpty());
        assertTrue("Password should not be empty", invalidUser.getPassword().isEmpty());
        assertTrue("Role should not be empty", invalidUser.getRole().isEmpty());
    }
} 