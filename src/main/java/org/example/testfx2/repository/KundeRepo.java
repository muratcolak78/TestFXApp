package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.db.Database;
import org.example.testfx2.model.Kunde;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// KundeRepo.java
public class KundeRepo {
    private static ObservableList<Kunde> kundenObservableList;
    
    public static ObservableList<Kunde> getKundenObservableList() throws SQLException {
        if (kundenObservableList == null) {
            refreshData();
        }
        return kundenObservableList;
    }
    
    public static void refreshData() throws SQLException {
        List<Kunde> kunden = getAllKunden();
        if (kundenObservableList == null) {
            kundenObservableList = FXCollections.observableArrayList(kunden);
        } else {
            kundenObservableList.setAll(kunden);
        }
    }
    
    public static List<Kunde> getAllKunden() {
        List<Kunde> list = new ArrayList<>();
        String sql = "SELECT * FROM kunden ORDER BY name";
        
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Kunde kunde = new Kunde(
                    rs.getInt("id"),
                    rs.getString("kunden_nr"),
                    rs.getString("name"),
                    rs.getDate("created_at")
                );
                list.add(kunde);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

