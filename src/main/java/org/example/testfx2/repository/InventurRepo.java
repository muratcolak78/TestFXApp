package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.db.Database;
import org.example.testfx2.model.Inventur;
import org.example.testfx2.utils.AlertUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// InventurRepo.java
public class InventurRepo {
	private static ObservableList<Inventur> inventurObservableList;

	public static ObservableList<Inventur> getInventurObservableList() throws SQLException {
		if (inventurObservableList == null) {
			refreshData();
		}
		return inventurObservableList;
	}

	public static void refreshData() throws SQLException {
		List<Inventur> inventuren = getAllInventuren();
		if (inventurObservableList == null) {
			inventurObservableList = FXCollections.observableArrayList(inventuren);
		} else {
			inventurObservableList.setAll(inventuren);
		}
	}

	public static List<Inventur> getAllInventuren() {
		List<Inventur> list = new ArrayList<>();
		String sql = "SELECT * FROM inventuren GROUP BY standort_id";

		try (Connection conn = Database.connect();
		     Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Inventur inventur = new Inventur(
						rs.getInt("id"),
						rs.getInt("standort_id"),
						rs.getString("status"),
						rs.getString("abgabe_datum"),
						rs.getInt("abgabe_person"),
						rs.getString("abnahme_datum"),
						rs.getInt("abnahme_person"),
						rs.getDouble("summe_gueter"),  // Dikkat: summe_queter değil summe_gueter
						rs.getInt("summe_paletten"),
						rs.getString("created_at")
				);
				list.add(inventur);
			}
			list.forEach(a-> System.out.println(a));
		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Fehler", "Fehler beim Laden der Inventuren");
		}
		return list;
	}

	public static List<Inventur> getInventurenByStandort(int standortId) {
		List<Inventur> list = new ArrayList<>();
		String sql = "SELECT * FROM inventuren WHERE standort_id = ?";

		try (Connection conn = Database.connect();
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, standortId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Inventur inventur = new Inventur(
						rs.getInt("id"),
						rs.getInt("standort_id"),
						rs.getString("status"),
						rs.getString("abgabe_datum"),
						rs.getInt("abgabe_person"),
						rs.getString("abnahme_datum"),
						rs.getInt("abnahme_person"),
						rs.getDouble("summe_gueter"),
						rs.getInt("summe_paletten"),
						rs.getString("created_at")
				);
				list.add(inventur);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Fehler", "Fehler beim Laden der Inventuren für den Standort");
		}
		return list;
	}

	public static void updateInventur(Inventur inventur) {
		String sql = "UPDATE inventuren SET status = ?, abgabe_datum = ?, abgabe_person = ?, " +
				"abnahme_datum = ?, abnahme_person = ?, summe_gueter = ?, summe_paletten = ? " +
				"WHERE id = ?";

		try (Connection conn = Database.connect();
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, inventur.getStatus());
			pstmt.setString(2, inventur.getAbgabeDatum());
			pstmt.setInt(3, inventur.getAbgabePerson());
			pstmt.setString(4, inventur.getAbnahmeDatum());
			pstmt.setInt(5, inventur.getAbnahmePerson());
			pstmt.setDouble(6, inventur.getSummeGueter());
			pstmt.setInt(7, inventur.getSummePaletten());
			pstmt.setInt(8, inventur.getId());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Fehler", "Fehler beim Aktualisieren der Inventur");
		}
	}
}
