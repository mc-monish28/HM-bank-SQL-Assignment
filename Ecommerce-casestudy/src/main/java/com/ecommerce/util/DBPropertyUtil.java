package com.ecommerce.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getPropertyString(String propertyFileName) {
        Properties properties = new Properties();
        String connectionString = null;

        try (FileInputStream input = new FileInputStream(propertyFileName)) {
            properties.load(input);

            String hostname = properties.getProperty("db.hostname");
            String port = properties.getProperty("db.port");
            String dbname = properties.getProperty("db.name");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            connectionString = String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s",
                    hostname, port, dbname, username, password);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return connectionString;
    }
}