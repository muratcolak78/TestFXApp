package org.example.testfx2.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:sqlite:mydatabase.db";
    public static Connection connect() {
        Connection conn = null;
        try {
           Class.forName("org.sqlite.JDBC"); // Driver'ı yükle
            conn = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
            System.out.println("SQLite-Verbindung erfolgreich!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

}
