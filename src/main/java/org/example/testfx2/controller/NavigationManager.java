package org.example.testfx2.controller;

import javafx.collections.ObservableList;
import lombok.Getter;
import org.example.testfx2.model2.KundeSaldo;
import org.example.testfx2.model2.TauschkontoUebersicht;
import org.example.testfx2.model2.ArtikelDetailStandort;
import org.example.testfx2.model2.ArtikelOutput;
import org.example.testfx2.model2.InventurOutput;
import org.example.testfx2.model2.QuartalOutput;

import java.sql.SQLException;

public class NavigationManager {
	@Getter
	private static final NavigationManager instance = new NavigationManager();

	private final MainController mainController = new MainController();
	private final ArtikelController artikelController = new ArtikelController();
	private final InventurController inventurController = new InventurController();
	private final TauschkontenController tauschkontenController = new TauschkontenController();
    private final BerechnungController berechnungController=new BerechnungController();

	private NavigationManager() {

	}

	public void openMainView() throws SQLException {
		mainController.openForm();
	}

	public void openTauschkontenView() throws SQLException {
		tauschkontenController.openForm();
	}

	public void openChecklistPdf() {
		mainController.openChecklistPdf();
	}

	public ObservableList<QuartalOutput> getMainTable() throws SQLException {
		return mainController.getMainTable();
	}

	public void openSelectedQuartalForm() throws SQLException {

		artikelController.openForm();
	}

	public void importArtikel() {
		artikelController.importArtikel();
	}

	public void exportAsPdf() {
		artikelController.exportAsPdf();
	}

	public void openInventurForm() throws SQLException {
		inventurController.openForm();
	}

	public ObservableList<ArtikelOutput> getArtikelTable() throws SQLException {
		return artikelController.getArtikelTable();
	}

	public void bearbeitenArtikel(ArtikelOutput artikel) {
		artikelController.bearbeitenArtikel(artikel);
	}

	public void openTauschForm() throws SQLException {
		tauschkontenController.openForm();
	}

	public ObservableList<InventurOutput> getInventuroutputList() throws SQLException {
		return inventurController.getInventuroutputList();
	}

	public ObservableList<ArtikelDetailStandort> getArtikelListWithSelectedStandort(String selected) {
        return inventurController.getArtikelListWithSelectedStandort(selected);
	}

	public void openRechnungsForm() throws SQLException {
		berechnungController.openForm();
	}

	public ObservableList<TauschkontoUebersicht> getTauschkontoUbersichtData() {
		return tauschkontenController.getTauschkontoUbersichtTable();
	}

	public ObservableList<KundeSaldo> getKundeSaldoObservationList() {
		return tauschkontenController.getKundeSaldoObservationList();
	}
}
