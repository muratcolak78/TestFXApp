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
	public static String getStandortName(Connection conn, int standortId) {
		String sql = "SELECT name FROM STANDORT WHERE statusId = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, standortId);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("name"); // "Abgenommen", "In Bearbeitung" vb.
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Unknown";
	}

}