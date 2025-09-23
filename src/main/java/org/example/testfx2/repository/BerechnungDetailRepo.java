package org.example.testfx2.repository;

import org.example.testfx2.db.Database;
import org.example.testfx2.model.BerechnungDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BerechnungDetailRepo {
    public static List<BerechnungDetail> getDetailsByBerechnungId(int berechnungId) {
        List<BerechnungDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM berechnung_details WHERE berechnung_id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, berechnungId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BerechnungDetail detail = new BerechnungDetail(
                    rs.getInt("id"),
                    rs.getInt("berechnung_id"),
                    rs.getString("rechnungsnummer"),
                    rs.getDouble("menge"),
                    rs.getString("mass"),
                    rs.getDouble("kosten"),
                    rs.getDouble("summe"),
                    rs.getDate("created_at")
                );
                list.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}