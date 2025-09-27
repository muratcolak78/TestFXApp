package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.bean.SessionData;
import org.example.testfx2.db.Database;
import org.example.testfx2.model2.InventurOutput;
import org.example.testfx2.utils.AlertUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// InventurRepo.java
public class InventurRepo {
	private static Connection conn=Database.connect();
	private static ObservableList<InventurOutput> inventurObservableList;

	public static ObservableList<InventurOutput> getInventurObservableList() throws SQLException {

			refreshData();

		return inventurObservableList;
	}

	public static void refreshData() throws SQLException {
		List<InventurOutput> outputList=getOutputListFromInventuren();
		if (inventurObservableList == null) {
			inventurObservableList = FXCollections.observableArrayList(outputList);
		} else {
			inventurObservableList.setAll(outputList);
		}
	}

	private static List<InventurOutput> getOutputListFromInventuren() throws SQLException {
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
			pstmt.setInt(1, SessionData.getInstance().getSelectedQuartalId());

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





}
