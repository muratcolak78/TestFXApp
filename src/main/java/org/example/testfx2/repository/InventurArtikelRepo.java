package org.example.testfx2.repository;

import org.example.testfx2.db.Database;
import org.example.testfx2.model.InventurArtikel;
import org.example.testfx2.utils.AlertUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class InventurArtikelRepo {
	public static List<InventurArtikel> getInventurArtikelByInventur(int inventurId) {
		List<InventurArtikel> list = new ArrayList<>();
		String sql = "SELECT * FROM inventur_artikel WHERE inventur_id = ?";

		try (Connection conn = Database.connect();
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, inventurId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				InventurArtikel inventurArtikel = new InventurArtikel(
						rs.getInt("id"),
						rs.getInt("inventur_id"),
						rs.getInt("artikel_id"),
						rs.getInt("menge"),
						rs.getDouble("gesamtwert")
				);
				list.add(inventurArtikel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Fehler", "Fehler beim Laden der Inventur-Artikel");
		}
		return list;
	}

	public static void updateInventurArtikel(InventurArtikel inventurArtikel) {
		String sql = "UPDATE inventur_artikel SET menge = ?, gesamtwert = ? WHERE id = ?";

		try (Connection conn = Database.connect();
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, inventurArtikel.getMenge());
			pstmt.setDouble(2, inventurArtikel.getGesamtwert());
			pstmt.setInt(3, inventurArtikel.getId());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Fehler", "Fehler beim Aktualisieren des Inventur-Artikels");
		}
	}
}