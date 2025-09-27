package org.example.testfx2.controller;

import javafx.collections.ObservableList;
import org.example.testfx2.model2.KundeSaldo;
import org.example.testfx2.model2.TauschkontoUebersicht;
import org.example.testfx2.repository.TauschkontoRepo;
import org.example.testfx2.views.TauschkontoView;

import java.sql.SQLException;

public class TauschkontenController {

	public void openForm() throws SQLException {
		new TauschkontoView().show();
	}

	public ObservableList<TauschkontoUebersicht> getTauschkontoUbersichtTable() {
		return TauschkontoRepo.getTauschUbersichtList();
	}

	public ObservableList<KundeSaldo> getKundeSaldoObservationList() {
		return TauschkontoRepo.getKundeTauschkontoList();
	}
}
