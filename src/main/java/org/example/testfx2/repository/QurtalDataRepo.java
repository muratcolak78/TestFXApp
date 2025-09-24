package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.db.Database;
import org.example.testfx2.model.QuartalDTO;
import org.example.testfx2.model2.Quartal;
import org.example.testfx2.model2.QuartalOutput;
import org.example.testfx2.utils.AlertUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class QurtalDataRepo {

	private static Connection conn=Database.connect();
	private static ObservableList<QuartalOutput> mainTable;

	public static ObservableList<QuartalOutput> getMainTable() throws SQLException {
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
		List<Quartal> quartalList=getQuartalListFromDatabase();
		List<QuartalOutput> quartalOutputList = setQuartalOutputList(quartalList);

		if (mainTable == null) {
			mainTable = FXCollections.observableArrayList(quartalOutputList);
		} else {
			mainTable.setAll(quartalOutputList); // Mevcut listeyi güncelle
		}
	}


	private static List<Quartal> getQuartalListFromDatabase() {
		List<Quartal> list = new ArrayList<>();
		String sql = "SELECT * FROM quartal";

		try (Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Quartal q = new Quartal(
						rs.getInt("quartalId"),
						rs.getInt("jahr"),
						rs.getInt("quartalartId"),
						rs.getInt("statusId"),
						rs.getString("abnahmeBei"),
						rs.getString("kommentar")
				);
				list.add(q);
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}
		list.forEach(k-> System.out.println(k));
		return list;
	}

	private static List<QuartalOutput> setQuartalOutputList(List<Quartal> quartalList){
		List<QuartalOutput> list = new ArrayList<>();

		try {
			for (Quartal quartal : quartalList) {
				// Her quartal için quartalArt ve status isimlerini al
				String quartalName = QuartalArtRepo.getQuartalName(conn, quartal.getQuartalartId());
				String statusName = StatusRepo.getStatusName(conn, quartal.getStatusId());

				// QuartalOutput objesi oluştur
				QuartalOutput output = new QuartalOutput();
				output.setJahr(quartal.getJahr());
				output.setQuartalId(quartal.getQuartalId());
				output.setQuartal(quartalName);
				output.setStatus(statusName);
				output.setAbnahmeBei(quartal.getAbnahmeBei());
				output.setKommentar(quartal.getKommentar());

				list.add(output);
			}

			list.forEach(k -> System.out.println(k));

		} catch (Exception e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Fehler","Abruf von Quartalobjekten aus der Datenbank fehlgeschlagen"+e.getMessage());
		}

		list.forEach(k-> System.out.println(k));
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
