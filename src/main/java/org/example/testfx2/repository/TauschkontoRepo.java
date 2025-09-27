package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.bean.SessionData;
import org.example.testfx2.db.Database;
import org.example.testfx2.model2.KundeSaldo;
import org.example.testfx2.model.Tauschkonto;
import org.example.testfx2.model2.TauschkontoUebersicht;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TauschkontoRepo {

    private static ObservableList<Tauschkonto> tauschkontenObservableList;
    private static Connection conn=Database.connect();

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
                SELECT
                    k.name AS Kunde,
                    SUM(tk.menge) AS Saldo
                FROM TAUSCHKONTEN tk
                JOIN kunde k ON k.kundenId = tk.kundenId
                WHERE tk.quartalId = ?
                GROUP BY k.name;
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, SessionData.getInstance().getSelectedQuartalId());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                KundeSaldo kundeSaldo = new KundeSaldo(
                        rs.getString("Kunde"),
                        rs.getInt("Saldo"),
                        "Test Status"
                );
                list.add(kundeSaldo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static ObservableList<KundeSaldo> getKundeTauschkontoList(){
        return FXCollections.observableArrayList(getKundenMitSaldos());
    }

    private  static List<TauschkontoUebersicht> getTauschkontoUebersicht() {

        List<TauschkontoUebersicht> list = new ArrayList<>();
        String sql = """
                SELECT
                                        t.name AS ART,
                                        SUM(tk.menge) AS Saldo,
                                        SUM(tk.menge * ap.verkaufspreis) AS Summe
                                    FROM TAUSCHKONTEN tk
                                    JOIN ARTIKEL a ON tk.artikelId = a.artikelId
                                    JOIN ARTIKELGROUP ag ON a.artikelGroupId = ag.artikelGroupId
                                    JOIN type t ON ag.typeId = t.typeId
                                    JOIN ArtikelPreis ap ON a.artikelId = ap.artikelId
                                    WHERE tk.quartalId = ?
                                    GROUP BY t.name;
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, SessionData.getInstance().getSelectedQuartalId());

            ResultSet rs = pstmt.executeQuery();

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
    public static ObservableList<TauschkontoUebersicht> getTauschUbersichtList(){
        return  FXCollections.observableArrayList(getTauschkontoUebersicht());
        }
    }
