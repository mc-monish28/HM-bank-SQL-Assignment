package com.order.management.dao;

import com.order.management.entity.Product;
import com.order.management.entity.User;
import com.order.management.exception.OrderNotFoundException;
import com.order.management.exception.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface IOrderManagementRepository {
    void createOrder(User user, List<Product> products) throws UserNotFoundException;
    void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException;
    void createProduct(User user, Product product) throws UserNotFoundException;
    void createUser(User user);
    List<Product> getAllProducts();
    List<Product> getOrderByUser(User user) throws UserNotFoundException;
    boolean isProductExists(int productId) throws SQLException;
} 