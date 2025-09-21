package org.example.testfx2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.model.Quartal;
import org.example.testfx2.model.QuartalDTO;
import org.example.testfx2.model.User;
import org.example.testfx2.model.enums.Status;
import org.example.testfx2.model.enums.QuartalArt;
import org.example.testfx2.repository.QurtalDataRepo;
import org.example.testfx2.utils.AlertUtil;
import org.example.testfx2.views.NeueAuswertungView;

import java.sql.SQLException;

public class NeueAuswertungController {
    private ObservableList<Quartal> quartalList;

    public NeueAuswertungController() throws SQLException {
       quartalList = FXCollections.observableArrayList(QurtalDataRepo.getMainTable());
    }

    public void openForm() throws SQLException {
        NeueAuswertungView view = new NeueAuswertungView();
        view.show();
    }

    public void saveQuartal(QuartalDTO q) throws SQLException {
        QurtalDataRepo.addQuartal(q);
        AlertUtil.showSuccessAlert("Speichert","object wurde erlfolrich gespeichert !");
    }
}
