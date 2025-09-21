package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.db.Database;
import org.example.testfx2.model.Quartal;
import org.example.testfx2.model.QuartalDTO;
import org.example.testfx2.model.enums.Status;
import org.example.testfx2.model.enums.QuartalArt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class QurtalDataRepo {
	private static ObservableList<Quartal> mainTable;
	public static ObservableList<Quartal> getMainTable() throws SQLException {
		if (mainTable == null) {
			refreshData();
		}
		return mainTable;
	}
	public static void addQuartal(QuartalDTO q) throws SQLException {
		saveQuadral(q);
		refreshData(); // Veritabanından yeniden veri çek
	}
	public static void refreshData() throws SQLException {
		List<Quartal> quartalList = QuartalDao();
		if (mainTable == null) {
			mainTable = FXCollections.observableArrayList(quartalList);
		} else {
			mainTable.setAll(quartalList); // Mevcut listeyi güncelle
		}
	}


	public static List<Quartal> QuartalDao() {
		List<Quartal> list = new ArrayList<>();
		String sql = "SELECT * FROM quartal";

		try (Connection conn = Database.connect();
		     Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Quartal q = new Quartal(
						rs.getInt("id"),
						rs.getInt("jahr"),
						QuartalArt.valueOf(rs.getString("quartal")),
						Status.valueOf(rs.getString("status")),
						rs.getString("abnahmeBei"),
						rs.getString("kommentar")
				);
				list.add(q);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
	private static void saveQuadral(QuartalDTO quartal){
		String sql="INSERT INTO quartal (jahr, quartal, status, abnahmeBei, kommentar) VALUES (?,?,?,?,?)";
		try (Connection conn = Database.connect();
		    PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1,quartal.getJahr());
			pstmt.setString(2,quartal.getQuartal().name());
			pstmt.setString(3,quartal.getStatus().name());
			pstmt.setString(4,quartal.getAbnaheBei());
			pstmt.setString(5,quartal.getKommentar());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
