package org.example.testfx2.controller;


import javafx.collections.ObservableList;
import org.example.testfx2.model2.Artikel;
import org.example.testfx2.model2.ArtikelOutput;
import org.example.testfx2.model2.QuartalOutput;
import org.example.testfx2.repository.ArtikelRepo;
import org.example.testfx2.utils.AlertUtil;
import org.example.testfx2.views.ArtikelView;

import java.sql.SQLException;

public class ArtikelController {


	public void openForm() throws SQLException {

		new ArtikelView().show();
		
	}

	public void importArtikel() {
	}

	public void exportAsPdf() {
	}

	public ObservableList<ArtikelOutput> getArtikelTable() throws SQLException {
		return ArtikelRepo.getArtikelObservableList();
	}

	public void bearbeitenArtikel(ArtikelOutput artikel) {
		AlertUtil.showInfoAlert("selected artikel info", artikel.toString());
	}

}
