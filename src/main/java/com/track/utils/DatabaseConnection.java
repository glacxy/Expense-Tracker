package com.track.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Correct database name with underscore
    private static final String URL = "jdbc:mysql://localhost:3306/expense_tracker?autoReconnect=true&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Nevilraj@16";

    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ MySQL driver loaded");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL driver not found: " + e.getMessage());
        }
    }

    // Get database connection
    public static Connection getDBConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✓ Connected to database");
            return conn;
        } catch (SQLException e) {
            System.err.println("✗ Failed to connect to database: " + e.getMessage());
            throw e;
        }
    }

    // Test connection utility
    public static boolean testConnection() {
        try (Connection c = getDBConnection()) {
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Test connection failed: " + e.getMessage());
            return false;
        }
    }
}
