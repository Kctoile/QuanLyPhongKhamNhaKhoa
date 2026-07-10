package com.dentalclinic.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public final class DBConnection {

    private static final String URL = System.getenv("DB_URL") != null
            ? System.getenv("DB_URL")
            : "jdbc:postgresql://localhost:5432/Dental";
    private static final String USER = System.getenv("DB_USER") != null
            ? System.getenv("DB_USER") : "postgres";
    private static final String PASS = System.getenv("DB_PASS") != null
            ? System.getenv("DB_PASS") : "postgres";

    private DBConnection() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
