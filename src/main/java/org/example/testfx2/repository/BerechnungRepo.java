package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.db.Database;
import org.example.testfx2.model.Berechnung;
import org.example.testfx2.model.BerechnungDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// BerechnungRepo.java
public class BerechnungRepo {
    private static ObservableList<Berechnung> berechnungObservableList;
    
    public static ObservableList<Berechnung> getBerechnungObservableList() throws SQLException {
        if (berechnungObservableList == null) {
            refreshData();
        }
        return berechnungObservableList;
    }
    
    public static void refreshData() throws SQLException {
        List<Berechnung> berechnungen = getAllBerechnungen();
        if (berechnungObservableList == null) {
            berechnungObservableList = FXCollections.observableArrayList(berechnungen);
        } else {
            berechnungObservableList.setAll(berechnungen);
        }
    }
    
    public static List<Berechnung> getAllBerechnungen() {
        List<Berechnung> list = new ArrayList<>();
        String sql = "SELECT * FROM berechnungen";
        
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Berechnung berechnung = new Berechnung(
                    rs.getInt("id"),
                    rs.getString("material"),
                    rs.getString("status"),
                    rs.getDouble("preis_pro_gram"),
                    rs.getDouble("summe"),
                    rs.getDate("created_at")
                );
                list.add(berechnung);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
