package org.example.testfx2.controller;


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

	public void bearbeiten() {
	}
}
