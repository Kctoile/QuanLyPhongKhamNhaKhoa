package com.dentalclinic.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL
            = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=PhongKhamNhaKhoa;"
            + "encrypt=true;trustServerCertificate=true";

    // Nếu không có biến môi trường → dùng mặc định
    private static final String USER
            = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "sa";

    private static final String PASS
            = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "123";

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
