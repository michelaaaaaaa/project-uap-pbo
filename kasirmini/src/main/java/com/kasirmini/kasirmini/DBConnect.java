package com.kasirmini.kasirmini;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static final String URL = "jdbc:mysql://localhost:3306/kasir_mini";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getKoneksi() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Koneksi Gagal: " + e.getMessage());
            return null;
        }
    }
}
