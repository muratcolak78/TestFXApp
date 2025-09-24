package org.example.testfx2.controller;


import javafx.collections.ObservableList;
import org.example.testfx2.model2.Artikel;
import org.example.testfx2.model2.ArtikelOutput;
import org.example.testfx2.model2.QuartalOutput;
import org.example.testfx2.repository.ArtikelRepo;
import org.example.testfx2.views.ArtikelView;

import java.sql.SQLException;

public class ArtikelController {



	public void openForm(int selectedQuartalId) throws SQLException {

		new ArtikelView(selectedQuartalId).show();
		
	}

	public void importArtikel() {
	}

	public void exportAsPdf() {
	}

	public void bearbeiten() {
	}

	public ObservableList<ArtikelOutput> getArtikelTable(int selectedQuartalId) throws SQLException {
		return ArtikelRepo.getArtikelObservableList(selectedQuartalId);
	}
}
