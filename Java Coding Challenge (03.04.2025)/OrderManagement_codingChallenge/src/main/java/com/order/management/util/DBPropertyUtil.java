package com.order.management.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getConnectionString(String propertyFileName) throws IOException {
        Properties properties = new Properties();
        FileInputStream input = new FileInputStream(propertyFileName);
        properties.load(input);
        
        String host = properties.getProperty("db.host");
        String port = properties.getProperty("db.port");
        String database = properties.getProperty("db.database");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        
        return String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s", 
                           host, port, database, username, password);
    }
} 