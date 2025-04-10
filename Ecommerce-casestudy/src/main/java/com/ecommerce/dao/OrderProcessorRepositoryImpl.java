package com.ecommerce.dao;

import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.CustomerNotFoundException;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.util.DBConnUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {
    private static final String DB_PROPERTIES_FILE = "src/main/resources/db.properties";
    private Connection connection;

    public OrderProcessorRepositoryImpl() {
        this.connection = DBConnUtil.getConnection(DB_PROPERTIES_FILE);
    }

    @Override
    public boolean createProduct(Product product) {
        String sql = "INSERT INTO products (name, price, description, stock_quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setString(3, product.getDescription());
            statement.setInt(4, product.getStockQuantity());
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setProductId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createCustomer(Customer customer) {
        String sql = "INSERT INTO customers (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPassword());
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        customer.setCustomerId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(int customerId) throws CustomerNotFoundException {
        try {
            connection.setAutoCommit(false);
            
            // First verify customer exists
            verifyCustomerExists(customerId);
            
            // Delete in correct order to handle foreign key constraints
            String[] deleteQueries = {
                "DELETE FROM order_items WHERE order_id IN (SELECT order_id FROM orders WHERE customer_id = ?)",
                "DELETE FROM orders WHERE customer_id = ?",
                "DELETE FROM cart WHERE customer_id = ?",
                "DELETE FROM customers WHERE customer_id = ?"
            };
            
            for (String query : deleteQueries) {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, customerId);
                    statement.executeUpdate();
                }
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean deleteProduct(int productId) throws ProductNotFoundException {
        try {
            connection.setAutoCommit(false);
            
            // First verify product exists
            verifyProductExists(productId);
            
            // Delete in correct order to handle foreign key constraints
            String[] deleteQueries = {
                "DELETE FROM order_items WHERE product_id = ?",
                "DELETE FROM cart WHERE product_id = ?",
                "DELETE FROM products WHERE product_id = ?"
            };
            
            for (String query : deleteQueries) {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, productId);
                    statement.executeUpdate();
                }
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean addToCart(Customer customer, Product product, int quantity) throws CustomerNotFoundException, ProductNotFoundException {
        try {
            connection.setAutoCommit(false);
            
            // Verify customer and product exist
            verifyCustomerExists(customer.getCustomerId());
            verifyProductExists(product.getProductId());
            
            // Check if product is already in cart
            String checkSql = "SELECT quantity FROM cart WHERE customer_id = ? AND product_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, customer.getCustomerId());
                checkStmt.setInt(2, product.getProductId());
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    // Update existing quantity
                    int currentQuantity = rs.getInt("quantity");
                    String updateSql = "UPDATE cart SET quantity = ? WHERE customer_id = ? AND product_id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, currentQuantity + quantity);
                        updateStmt.setInt(2, customer.getCustomerId());
                        updateStmt.setInt(3, product.getProductId());
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Insert new cart item
                    String insertSql = "INSERT INTO cart (customer_id, product_id, quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, customer.getCustomerId());
                        insertStmt.setInt(2, product.getProductId());
                        insertStmt.setInt(3, quantity);
                        insertStmt.executeUpdate();
                    }
                }
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean removeFromCart(Customer customer, Product product) throws CustomerNotFoundException, ProductNotFoundException {
        try {
            connection.setAutoCommit(false);
            
            // Verify customer and product exist
            verifyCustomerExists(customer.getCustomerId());
            verifyProductExists(product.getProductId());
            
            // Check if item exists in cart
            String checkSql = "SELECT quantity FROM cart WHERE customer_id = ? AND product_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, customer.getCustomerId());
                checkStmt.setInt(2, product.getProductId());
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    throw new ProductNotFoundException("Product not found in cart");
                }
            }
            
            // Remove from cart
            String deleteSql = "DELETE FROM cart WHERE customer_id = ? AND product_id = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, customer.getCustomerId());
                deleteStmt.setInt(2, product.getProductId());
                int rowsAffected = deleteStmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    connection.commit();
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Product> getAllFromCart(Customer customer) throws CustomerNotFoundException {
        verifyCustomerExists(customer.getCustomerId());
        
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.quantity FROM products p JOIN cart c ON p.product_id = c.product_id WHERE c.customer_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customer.getCustomerId());
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setDescription(rs.getString("description"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }

    @Override
    public boolean placeOrder(Customer customer, List<Map<Product, Integer>> products, String shippingAddress)
            throws CustomerNotFoundException, ProductNotFoundException {
        try {
            connection.setAutoCommit(false);
            
            // Verify customer exists
            verifyCustomerExists(customer.getCustomerId());
            
            // Create order
            String orderSql = "INSERT INTO orders (customer_id, order_date, total_price, shipping_address) VALUES (?, NOW(), ?, ?)";
            try (PreparedStatement orderStmt = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, customer.getCustomerId());
                orderStmt.setDouble(2, calculateTotalPrice(products));
                orderStmt.setString(3, shippingAddress);
                orderStmt.executeUpdate();
                
                ResultSet rs = orderStmt.getGeneratedKeys();
                int orderId = 0;
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
                
                // Create order items
                String orderItemSql = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement orderItemStmt = connection.prepareStatement(orderItemSql)) {
                    for (Map<Product, Integer> productMap : products) {
                        for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
                            Product product = entry.getKey();
                            int quantity = entry.getValue();
                            
                            // Verify product exists
                            verifyProductExists(product.getProductId());
                            
                            orderItemStmt.setInt(1, orderId);
                            orderItemStmt.setInt(2, product.getProductId());
                            orderItemStmt.setInt(3, quantity);
                            orderItemStmt.addBatch();
                        }
                    }
                    orderItemStmt.executeBatch();
                }
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Map<Product, Integer>> getOrdersByCustomer(int customerId) throws CustomerNotFoundException {
        verifyCustomerExists(customerId);
        
        List<Map<Product, Integer>> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, p.*, oi.quantity " +
                    "FROM orders o " +
                    "JOIN order_items oi ON o.order_id = oi.order_id " +
                    "JOIN products p ON oi.product_id = p.product_id " +
                    "WHERE o.customer_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            ResultSet rs = statement.executeQuery();
            
            Map<Integer, Map<Product, Integer>> orderMap = new HashMap<>();
            
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setDescription(rs.getString("description"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                
                int quantity = rs.getInt("quantity");
                
                orderMap.computeIfAbsent(orderId, k -> new HashMap<>())
                        .put(product, quantity);
            }
            
            orders.addAll(orderMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return orders;
    }

    @Override
    public boolean verifyCustomerExists(int customerId) throws CustomerNotFoundException {
        String sql = "SELECT COUNT(*) FROM customers WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            ResultSet rs = statement.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new CustomerNotFoundException("Customer with ID " + customerId + " not found");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean verifyProductExists(int productId) throws ProductNotFoundException {
        String sql = "SELECT COUNT(*) FROM products WHERE product_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            ResultSet rs = statement.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new ProductNotFoundException("Product with ID " + productId + " not found");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    private double calculateTotalPrice(List<Map<Product, Integer>> products) {
        double total = 0.0;
        for (Map<Product, Integer> productMap : products) {
            for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
                total += entry.getKey().getPrice() * entry.getValue();
            }
        }
        return total;
    }

    @Override
    public boolean deleteCustomerByName(String name) throws CustomerNotFoundException {
        try {
            connection.setAutoCommit(false);
            
            // First get customer ID by name
            String getCustomerIdSql = "SELECT customer_id FROM customers WHERE name = ?";
            int customerId = -1;
            
            try (PreparedStatement getStmt = connection.prepareStatement(getCustomerIdSql)) {
                getStmt.setString(1, name);
                ResultSet rs = getStmt.executeQuery();
                if (!rs.next()) {
                    throw new CustomerNotFoundException("Customer with name '" + name + "' not found");
                }
                customerId = rs.getInt("customer_id");
            }
            
            // Delete in correct order to handle foreign key constraints
            String[] deleteQueries = {
                "DELETE FROM order_items WHERE order_id IN (SELECT order_id FROM orders WHERE customer_id = ?)",
                "DELETE FROM orders WHERE customer_id = ?",
                "DELETE FROM cart WHERE customer_id = ?",
                "DELETE FROM customers WHERE customer_id = ?"
            };
            
            for (String query : deleteQueries) {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, customerId);
                    statement.executeUpdate();
                }
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}