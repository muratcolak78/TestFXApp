package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.db.Database;
import org.example.testfx2.model.Artikel;
import org.example.testfx2.model.Quartal;
import org.example.testfx2.model.enums.QuartalArt;
import org.example.testfx2.model.enums.Status;
import org.example.testfx2.utils.AlertUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtikelRepo {
	private static ObservableList<Artikel> artikelObservableList;
	public static ObservableList<Artikel> getArtikelObservableList() throws SQLException {
		if (artikelObservableList == null) {
			refreshData();
		}
		return artikelObservableList;
	}
	public static void addArtikel(Artikel artikel) throws SQLException {
		saveArtikel(artikel);
		refreshData();
	}
	public static void refreshData() throws SQLException {
		System.out.println("refresh metot calisti");
		List<Artikel> artikels = ArtikelDao();
		if (artikels != null) {
			artikelObservableList = FXCollections.observableArrayList(artikels);
		} else {
			artikelObservableList.setAll(artikels); // Mevcut listeyi güncelle
		}
	}


	public static List<Artikel> ArtikelDao() {
		System.out.println("artikle getiren metot calisti");
		List<Artikel> list = new ArrayList<>();
		String sql = "SELECT * FROM artikels";

		try (Connection conn = Database.connect();
		     Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Artikel artikel = new Artikel(
						rs.getInt("id"),
						rs.getString("artikel_nr"),
						rs.getString("name"),
						rs.getDouble("einkaufspreis"),
						rs.getDouble("verkaufspreis"),
						rs.getDate("created_at")
				);
				list.add(artikel);
			}
			System.out.println(list.size());
		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Fehler","Fehler beim Bringen des Artikels aus den Daten");
		}

		return list;
	}
	private static void saveArtikel(Artikel artikel){
		String sql="INSERT INTO artikel (artikelNr, artikelName, einkaufPreis, verkaufPreis, createdAt) VALUES (?,?,?,?,?)";
		try (Connection conn = Database.connect();
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1,artikel.getArtikelNr());
			pstmt.setString(2,artikel.getArtikelName());
			pstmt.setDouble(3,artikel.getEinkaufPreis());
			pstmt.setDouble(4,artikel.getVerkaufPreis());
			pstmt.setDate(5,artikel.getCreatedAt());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Fehler","Fehler beim Speichern des Artikels in der Datenbank");

		}
	}

}
