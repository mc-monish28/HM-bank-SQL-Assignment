package com.order.management.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {
    public static Connection getConnection(String connectionString) throws SQLException {
        return DriverManager.getConnection(connectionString);
    }
} 