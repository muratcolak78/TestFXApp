package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.db.Database;
import org.example.testfx2.model2.*;
import org.example.testfx2.utils.AlertUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtikelRepo {

	private static Connection conn=Database.connect();
	private static ObservableList<ArtikelOutput> artikelObservableList;
	;


	public static ObservableList<ArtikelOutput> getArtikelObservableList(int quartalId) throws SQLException {

			refreshData(quartalId);

		return artikelObservableList;
	}

	public static void refreshData(int quartalId) throws SQLException {
		List<ArtikelOutput> artikels=getArtikelOutputListFromQuartal(quartalId);
		if (artikels != null) {
			artikelObservableList = FXCollections.observableArrayList(artikels);
		} else {
			artikelObservableList.setAll(artikels); // Mevcut listeyi güncelle
		}
	}

	private static List<ArtikelOutput> getArtikelOutputListFromQuartal(int quartalId) {

		List<ArtikelOutput> artikels=new ArrayList<>();
		String sql = """
            SELECT
				            a.artikelId,
				            a.name,
				            ap.quartalId,
				            ap.einkaufspreis AS einkaufPreis,
				            ap.verkaufspreis AS verkaufPreis
				        FROM ArtikelPreis ap
				        JOIN ARTIKEL a ON ap.artikelId = a.artikelId
				        WHERE ap.quartalId = ?
				        ORDER BY a.name;
    """;

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, quartalId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				ArtikelOutput output = new ArtikelOutput();
				output.setArtikelId(rs.getInt("artikelId"));
				output.setQuartalId(rs.getInt("quartalId"));
				output.setName(rs.getString("name"));
				output.setEinkaufPreis(rs.getDouble("einkaufPreis"));
				output.setVerkaufPreis(rs.getDouble("verkaufPreis"));

				artikels.add(output);
			}

			System.out.println("Quartal " + quartalId + " için " + artikels.size() + " artikel bulundu");

		} catch (SQLException e) {
			System.err.println("ArtikelOutput listesi alınırken hata: " + e.getMessage());
			e.printStackTrace();
		}

		return artikels;
	}

	public static ObservableList<ArtikelDetailStandort> getArtikelObservableListWithStandort(int quartalid, String standort) {
		List<ArtikelDetailStandort> list = new ArrayList<>();
		String sql = """
				
				SELECT
				    AG.name AS ArtikelGroupName,
				    S.name AS Status,
				    T.name AS Type,
				    SUM(I.menge * AP.einkaufspreis) AS Summe
				FROM INVENTUR I
				JOIN STANDORT ST ON I.standortId = ST.standortId
				JOIN STATUS S ON I.statusId = S.statusId
				JOIN ARTIKEL A ON I.artikelId = A.artikelId
				JOIN ARTIKELGROUP AG ON A.artikelGroupId = AG.artikelGroupId
				JOIN TYPE T ON AG.typeId = T.typeId
				JOIN ArtikelPreis AP ON A.artikelId = AP.artikelId
				WHERE ST.name = ?
				  AND I.quartalId = ?
				GROUP BY AG.name, S.name, T.name
				ORDER BY AG.name;
				
				""";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, standort);
			pstmt.setInt(2,quartalid);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				ArtikelDetailStandort output=new ArtikelDetailStandort();
				output.setName(rs.getString("ArtikelGroupName"));
				output.setStatus(rs.getString("Status"));
				output.setType(rs.getString("Type"));
				output.setSumme(rs.getDouble("Summe"));

				list.add(output);
			}
			list.forEach(a-> System.out.println("-->"+a));
		} catch (SQLException e) {
			e.printStackTrace();
			AlertUtil.showErrorAlert("Fehler", "Fehler beim Laden der Inventuren");
		}
		ObservableList<ArtikelDetailStandort> artikelDetailStandortObservableList=
				FXCollections.observableArrayList(list);
		return artikelDetailStandortObservableList;

	}

}
