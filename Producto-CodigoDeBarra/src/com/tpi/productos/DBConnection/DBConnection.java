package com.tpi.productos.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Ajustá estos parámetros según tu XAMPP/phpMyAdmin
    private static final String URL = "jdbc:mysql://localhost:3306/tpi_productos?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // usuario por defecto XAMPP
    private static final String PASS = "";     // contraseña (en XAMPP suele ser vacía)

    static {
        try {
            // No es necesario en JDBC modernos, pero explícito para claridad
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver no encontrado. Asegurate de agregarlo al classpath.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}