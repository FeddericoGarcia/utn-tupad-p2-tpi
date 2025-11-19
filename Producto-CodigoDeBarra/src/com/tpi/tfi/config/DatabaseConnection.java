package com.tpi.tfi.config;

import com.tpi.tfi.exceptions.DataAccessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String USER = "root";
    private static final String PASS = "";
    private static final String NAME_DB = "tpi_db";
    private static final String URL = "jdbc:mysql://localhost:3306/"+NAME_DB+"?useSSL=false&serverTimezone=UTC";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo establecer la conexión a la base de datos", e);
        }
    }
    
    //TODO
    /*
    public static setAutoCommit(){
        
    }
    public static commit(){
        
    }
*/
    
}
