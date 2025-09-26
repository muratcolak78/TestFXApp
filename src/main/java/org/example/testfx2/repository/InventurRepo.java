package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.db.Database;
import org.example.testfx2.model2.Inventur;
import org.example.testfx2.model2.InventurOutput;
import org.example.testfx2.utils.AlertUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// InventurRepo.java
public class InventurRepo {
	private static Connection conn=Database.connect();
	private static ObservableList<InventurOutput> inventurObservableList;

	public static ObservableList<InventurOutput> getInventurObservableList(int quartalId) throws SQLException {

			refreshData(quartalId);

		return inventurObservableList;
	}

	public static void refreshData(int quartalId) throws SQLException {
		List<InventurOutput> outputList=getOutputListFromInventuren(quartalId);
		if (inventurObservableList == null) {
			inventurObservableList = FXCollections.observableArrayList(outputList);
		} else {
			inventurObservableList.setAll(outputList);
		}
	}

	private static List<InventurOutput> getOutputListFromInventuren(int quartalId) throws SQLException {
		List<InventurOutput> list = new ArrayList<>();
		String sql = """
				
				SELECT
				    s.name AS standortName,
				    sts.name AS statusName,
				    i.abnahmeBei AS abnahmebei,
				
				    -- GitterBox toplamı (typeId 1 veya 3)
				    SUM(CASE WHEN t.typeId IN (1, 3) THEN ap.verkaufspreis * i.menge ELSE 0 END) AS summeGitterBox,
				
				    -- PaleBox toplamı (typeId = 2)
				    SUM(CASE WHEN t.typeId = 2 THEN ap.verkaufspreis * i.menge ELSE 0 END) AS summePaleBox,
				
				    -- Genel toplam (ikisini toplayarak)
				    SUM(CASE
				        WHEN t.typeId IN (1, 3) THEN ap.verkaufspreis * i.menge
				        WHEN t.typeId = 2 THEN ap.verkaufspreis * i.menge
				        ELSE 0
				    END) AS summeGesamt
				
				FROM INVENTUR i
				JOIN STANDORT s ON s.standortId = i.standortId
				JOIN STATUS sts ON sts.statusId = i.statusId
				JOIN ARTIKEL a ON i.artikelId = a.artikelId
				JOIN ARTIKELGROUP ag ON ag.artikelGroupId = a.artikelGroupId
				JOIN TYPE t ON t.typeId = ag.typeId
				JOIN ARTIKELPREIS ap ON ap.artikelId = a.artikelId AND ap.quartalId = i.quartalId
				
				WHERE i.quartalId = ?
				
				GROUP BY s.name
				ORDER BY s.name;
				
				""";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, quartalId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				InventurOutput inventurOutput = new InventurOutput();
				inventurOutput.setStandort(rs.getString("standortName"));
				inventurOutput.setStatus(rs.getString("statusName"));
				inventurOutput.setAbnahmeBei(rs.getString("abnahmebei"));
				inventurOutput.setSummeGitterBoxPreis(rs.getDouble("summeGitterBox"));
				inventurOutput.setSummePalepreis(rs.getDouble("summePaleBox"));
				inventurOutput.setSumme(rs.getDouble("summeGesamt"));
				inventurOutput.setAbgabeBei("test");
				inventurOutput.setAbgabeDatum("test datum");

				list.add(inventurOutput);
			}
			list.forEach(a-> System.out.println("-->"+a));
		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Fehler", "Fehler beim Laden der Inventuren");
		}
		return list;

	}

	public static List<Inventur> getAllFromSelectedInventuren(int quartalId) {
		List<Inventur> list = new ArrayList<>();
		String sql = "SELECT * FROM inventuren where quartalId=? ";

		try (Statement stmt = conn.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Inventur inventur = new Inventur(
						rs.getInt("inventurId"),
						rs.getInt("quartalId"),
						rs.getInt("standortId"),
						rs.getInt("artikelId"),
						rs.getInt("menge"),
						rs.getString("abnahmeBei"),
						rs.getInt("status")
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



}
