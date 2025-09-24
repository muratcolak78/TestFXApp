package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.db.Database;
import org.example.testfx2.model2.Artikel;
import org.example.testfx2.model2.ArtikelOutput;
import org.example.testfx2.model2.Quartal;
import org.example.testfx2.utils.AlertUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtikelRepo {

	private static Connection conn=Database.connect();
	private static ObservableList<ArtikelOutput> artikelObservableList;


	public static ObservableList<ArtikelOutput> getArtikelObservableList(int quartalId) throws SQLException {
		if (artikelObservableList == null) {
			refreshData(quartalId);
		}
		return artikelObservableList;
	}

	public static void refreshData(int quartalId) throws SQLException {

		List<ArtikelOutput> artikels = getArtikelOutputListFromQuartal(quartalId);
		if (artikels != null) {
			artikelObservableList = FXCollections.observableArrayList(artikels);
		} else {
			artikelObservableList.setAll(artikels); // Mevcut listeyi güncelle
		}
	}

	private static List<ArtikelOutput> getArtikelOutputListFromQuartal(int quartalId) {
		List<ArtikelOutput> outputList = new ArrayList<>();

		String sql = """
        SELECT 
            a.artikelId, 
            a.name,
            ap.einkaufspreis as einkaufPreis,
            ap.verkaufspreis as verkaufPreis,
            ag.name as artikelGroup,
            t.name as artikelType
        FROM inventur i
        JOIN ARTIKEL a ON i.artikelId = a.artikelId
        LEFT JOIN ArtikelPreis ap ON a.artikelId = ap.artikelId
        LEFT JOIN ARTIKELGROUP ag ON a.artikelGroupId = ag.artikelGroupId
        LEFT JOIN TYPE t ON ag.typeId = t.typeId
        WHERE i.quartalId = ?
        ORDER BY a.name
    """;

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, quartalId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				ArtikelOutput output = new ArtikelOutput();
				output.setArtikelId(rs.getInt("artikelId"));
				output.setName(rs.getString("name"));
				output.setEinkaufPreis(rs.getDouble("einkaufPreis"));
				output.setVerkaufPreis(rs.getDouble("verkaufPreis"));
				output.setArtikelGroup(rs.getString("artikelGroup"));
				output.setArtikelType(rs.getString("artikelType"));

				outputList.add(output);
			}

			System.out.println("Quartal " + quartalId + " için " + outputList.size() + " artikel bulundu");

		} catch (SQLException e) {
			System.err.println("ArtikelOutput listesi alınırken hata: " + e.getMessage());
			e.printStackTrace();
		}

		return outputList;
	}




}
