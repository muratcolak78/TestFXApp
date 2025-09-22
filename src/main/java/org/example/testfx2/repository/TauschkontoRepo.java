package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.db.Database;
import org.example.testfx2.model.KundeSaldo;
import org.example.testfx2.model.Tauschkonto;
import org.example.testfx2.model.TauschkontoUebersicht;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TauschkontoRepo {
    private static ObservableList<Tauschkonto> tauschkontenObservableList;
    
    public static ObservableList<Tauschkonto> getTauschkontenObservableList() throws SQLException {
        if (tauschkontenObservableList == null) {
            refreshData();
        }
        return tauschkontenObservableList;
    }
    
    public static void refreshData() throws SQLException {
        List<Tauschkonto> tauschkonten = getAllTauschkonten();
        if (tauschkontenObservableList == null) {
            tauschkontenObservableList = FXCollections.observableArrayList(tauschkonten);
        } else {
            tauschkontenObservableList.setAll(tauschkonten);
        }
    }
    
    public static List<Tauschkonto> getAllTauschkonten() {
        List<Tauschkonto> list = new ArrayList<>();
        String sql = "SELECT * FROM tauschkonten";
        
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Tauschkonto tauschkonto = new Tauschkonto(
                    rs.getInt("id"),
                    rs.getInt("kunde_id"),
                    rs.getInt("art_id"),
                    rs.getInt("saldo"),
                    rs.getString("status"),
                    rs.getDate("created_at")
                );
                list.add(tauschkonto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    

    // Kunden bazlı detaylar
    public static List<Tauschkonto> getTauschkontenByKunde(int kundeId) {
        List<Tauschkonto> list = new ArrayList<>();
        String sql = "SELECT * FROM tauschkonten WHERE kunde_id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, kundeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Tauschkonto tauschkonto = new Tauschkonto(
                    rs.getInt("id"),
                    rs.getInt("kunde_id"),
                    rs.getInt("art_id"),
                    rs.getInt("saldo"),
                    rs.getString("status"),
                    rs.getDate("created_at")
                );
                list.add(tauschkonto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static List<KundeSaldo> getKundenMitSaldos() {
        List<KundeSaldo> list = new ArrayList<>();
        String sql = """
        SELECT k.name as kunde_name, 
               SUM(tk.saldo) as gesamt_saldo,
               MAX(tk.status) as kunde_status
        FROM tauschkonten tk
        JOIN kunden k ON tk.kunde_id = k.id
        GROUP BY k.id, k.name
        ORDER BY k.name
        """;

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                KundeSaldo kundeSaldo = new KundeSaldo(
                        rs.getString("kunde_name"),
                        rs.getInt("gesamt_saldo"),
                        rs.getString("kunde_status")
                );
                list.add(kundeSaldo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tauschkonto özeti için güncellenmiş metot
    public static List<TauschkontoUebersicht> getTauschkontoUebersicht() {
        List<TauschkontoUebersicht> list = new ArrayList<>();
        String sql = """
        SELECT ta.art_name as art, 
               SUM(tk.saldo) as saldo,
               SUM(tk.saldo * 12.0) as summe  -- Örnek hesaplama
        FROM tauschkonten tk
        JOIN tauschkonto_arten ta ON tk.art_id = ta.id
        GROUP BY ta.art_name
        ORDER BY ta.art_name
        """;

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TauschkontoUebersicht uebersicht = new TauschkontoUebersicht(
                        rs.getString("art"),
                        rs.getInt("saldo"),
                        rs.getDouble("summe")
                );
                list.add(uebersicht);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}