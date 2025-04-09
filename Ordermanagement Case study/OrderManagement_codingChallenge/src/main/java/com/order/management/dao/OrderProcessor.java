package com.order.management.dao;

import com.order.management.entity.Product;
import com.order.management.entity.User;
import com.order.management.entity.Electronics;
import com.order.management.entity.Clothing;
import com.order.management.exception.OrderNotFoundException;
import com.order.management.exception.UserNotFoundException;
import com.order.management.util.DBConnUtil;
import com.order.management.util.DBPropertyUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderProcessor implements IOrderManagementRepository {
    private Connection connection;

    public OrderProcessor() {
        try {
            String connectionString = DBPropertyUtil.getConnectionString("src/main/resources/db.properties");
            this.connection = DBConnUtil.getConnection(connectionString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createOrder(User user, List<Product> products) throws UserNotFoundException {
        try {
            // Check if user exists
            if (!isUserExists(user.getUserId())) {
                throw new UserNotFoundException("User not found");
            }

            // Validate all products exist
            for (Product product : products) {
                if (!isProductExists(product.getProductId())) {
                    throw new IllegalArgumentException("Product with ID " + product.getProductId() + " does not exist");
                }
            }

            // Create order
            String orderQuery = "INSERT INTO orders (user_id) VALUES (?)";
            try (PreparedStatement orderStmt = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, user.getUserId());
                orderStmt.executeUpdate();

                ResultSet rs = orderStmt.getGeneratedKeys();
                if (rs.next()) {
                    int orderId = rs.getInt(1);

                    // Add order items
                    String orderItemQuery = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement orderItemStmt = connection.prepareStatement(orderItemQuery)) {
                        for (Product product : products) {
                            orderItemStmt.setInt(1, orderId);
                            orderItemStmt.setInt(2, product.getProductId());
                            orderItemStmt.setInt(3, 1); // Default quantity
                            orderItemStmt.addBatch();
                        }
                        orderItemStmt.executeBatch();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }

    @Override
    public void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException {
        try {
            if (!isUserExists(userId)) {
                throw new UserNotFoundException("User not found");
            }

            if (!isOrderExists(orderId)) {
                throw new OrderNotFoundException("Order not found");
            }

            // First delete all order items
            String deleteOrderItemsQuery = "DELETE FROM order_items WHERE order_id = ?";
            try (PreparedStatement deleteOrderItemsStmt = connection.prepareStatement(deleteOrderItemsQuery)) {
                deleteOrderItemsStmt.setInt(1, orderId);
                deleteOrderItemsStmt.executeUpdate();
            }

            // Then delete the order
            String deleteOrderQuery = "DELETE FROM orders WHERE order_id = ? AND user_id = ?";
            try (PreparedStatement deleteOrderStmt = connection.prepareStatement(deleteOrderQuery)) {
                deleteOrderStmt.setInt(1, orderId);
                deleteOrderStmt.setInt(2, userId);
                int rowsAffected = deleteOrderStmt.executeUpdate();
                
                if (rowsAffected == 0) {
                    throw new OrderNotFoundException("Order not found for the specified user");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error cancelling order: " + e.getMessage());
        }
    }

    @Override
    public void createProduct(User user, Product product) throws UserNotFoundException {
        try {
            if (!isUserExists(user.getUserId()) || !user.getRole().equals("Admin")) {
                throw new UserNotFoundException("Admin user not found");
            }

            String query = "INSERT INTO products (product_name, description, price, quantity_in_stock, type, " +
                         "brand, warranty_period, size, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, product.getProductName());
                stmt.setString(2, product.getDescription());
                stmt.setDouble(3, product.getPrice());
                stmt.setInt(4, product.getQuantityInStock());
                stmt.setString(5, product.getType());
                
                if (product instanceof Electronics) {
                    Electronics electronics = (Electronics) product;
                    stmt.setString(6, electronics.getBrand());
                    stmt.setInt(7, electronics.getWarrantyPeriod());
                    stmt.setNull(8, Types.VARCHAR);
                    stmt.setNull(9, Types.VARCHAR);
                } else if (product instanceof Clothing) {
                    Clothing clothing = (Clothing) product;
                    stmt.setNull(6, Types.VARCHAR);
                    stmt.setNull(7, Types.INTEGER);
                    stmt.setString(8, clothing.getSize());
                    stmt.setString(9, clothing.getColor());
                }
                
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createUser(User user) {
        try {
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getRole());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try {
            String query = "SELECT * FROM products";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    Product product;
                    String type = rs.getString("type");
                    
                    if ("Electronics".equals(type)) {
                        Electronics electronics = new Electronics();
                        electronics.setBrand(rs.getString("brand"));
                        electronics.setWarrantyPeriod(rs.getInt("warranty_period"));
                        product = electronics;
                    } else {
                        Clothing clothing = new Clothing();
                        clothing.setSize(rs.getString("size"));
                        clothing.setColor(rs.getString("color"));
                        product = clothing;
                    }
                    
                    product.setProductId(rs.getInt("product_id"));
                    product.setProductName(rs.getString("product_name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setQuantityInStock(rs.getInt("quantity_in_stock"));
                    product.setType(type);
                    
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> getOrderByUser(User user) throws UserNotFoundException {
        List<Product> products = new ArrayList<>();
        try {
            if (!isUserExists(user.getUserId())) {
                throw new UserNotFoundException("User not found");
            }

            String query = "SELECT p.* FROM products p " +
                         "JOIN order_items oi ON p.product_id = oi.product_id " +
                         "JOIN orders o ON oi.order_id = o.order_id " +
                         "WHERE o.user_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, user.getUserId());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Product product;
                        String type = rs.getString("type");
                        
                        if ("Electronics".equals(type)) {
                            Electronics electronics = new Electronics();
                            electronics.setBrand(rs.getString("brand"));
                            electronics.setWarrantyPeriod(rs.getInt("warranty_period"));
                            product = electronics;
                        } else {
                            Clothing clothing = new Clothing();
                            clothing.setSize(rs.getString("size"));
                            clothing.setColor(rs.getString("color"));
                            product = clothing;
                        }
                        
                        product.setProductId(rs.getInt("product_id"));
                        product.setProductName(rs.getString("product_name"));
                        product.setDescription(rs.getString("description"));
                        product.setPrice(rs.getDouble("price"));
                        product.setQuantityInStock(rs.getInt("quantity_in_stock"));
                        product.setType(type);
                        
                        products.add(product);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private boolean isUserExists(int userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private boolean isOrderExists(int orderId) throws SQLException {
        String query = "SELECT COUNT(*) FROM orders WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean isProductExists(int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
} 