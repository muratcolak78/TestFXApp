package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.db.Database;
import org.example.testfx2.model.Inventur;
import org.example.testfx2.model.InventurArtikel;
import org.example.testfx2.model.Standort;
import org.example.testfx2.utils.AlertUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StandortRepo {
	private static ObservableList<Standort> standortObservableList;
	private static List<Standort> standorte = new ArrayList<>();

	public static ObservableList<Standort> getStandortObservableList() throws SQLException {
		if (standortObservableList == null) {
			refreshData();
		}
		return standortObservableList;
	}

	public static void refreshData() throws SQLException {
		standorte = getAllStandorte(); // Bu satırı ekledim
		if (standortObservableList == null) {
			standortObservableList = FXCollections.observableArrayList(standorte);
		} else {
			standortObservableList.setAll(standorte);
		}
	}

	public static List<Standort> getAllStandorte() {
		List<Standort> list = new ArrayList<>();
		String sql = "SELECT * FROM standorte";

		try (Connection conn = Database.connect();
		     Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Standort standort = new Standort(
						rs.getInt("id"),
						rs.getString("name")
				);
				list.add(standort);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Fehler", "Fehler beim Laden der Standorte");
		}
		return list;
	}

	public static String getStandortNameById(int standortId) {
		// Önce standorte listesinin boş olup olmadığını kontrol et
		if (standorte.isEmpty()) {
			try {
				refreshData(); // Verileri yeniden yükle
			} catch (SQLException e) {
				e.printStackTrace();
				return "N/A";
			}
		}

		return standorte.stream()
				.filter(e -> e.getId() == standortId)
				.findFirst()
				.map(Standort::getName)
				.orElse("N/A"); // null yerine "N/A" döndür
	}

	// Daha güvenli alternatif metod
	public static String getStandortNameByIdSafe(int standortId) {
		try {
			// Doğrudan database'den al
			String sql = "SELECT name FROM standorte WHERE id = ?";
			try (Connection conn = Database.connect();
			     PreparedStatement pstmt = conn.prepareStatement(sql)) {

				pstmt.setInt(1, standortId);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					return rs.getString("name");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "N/A";
	}
}