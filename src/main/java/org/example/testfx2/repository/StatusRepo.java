package org.example.testfx2.repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusRepo {
	public static String getStatusName(Connection conn, int statusId) {
		String sql = "SELECT name FROM STATUS WHERE statusId = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, statusId);

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
