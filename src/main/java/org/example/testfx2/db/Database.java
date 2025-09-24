package org.example.testfx2.db;


import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static final String URL = "jdbc:sqlite:inventur.db";
    public static Connection connect() {
        Connection conn = null;
        try {
           Class.forName("org.sqlite.JDBC"); // Driver'ı yükle
            conn = DriverManager.getConnection(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

}
