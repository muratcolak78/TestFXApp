package org.example.testfx2.repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuartalArtRepo {
	public static String getQuartalName(Connection conn, int quartalartId) {
		String sql = "SELECT name FROM QUARTALART WHERE quartalartId = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, quartalartId);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("name"); // "Q1", "Q2" vb.
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Unknown"; // Bulunamazsa default değer
	}
}
