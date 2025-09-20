package org.example.testfx2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import org.example.testfx2.model.Quartal;
import org.example.testfx2.model.User;
import org.example.testfx2.model.enums.Status;
import org.example.testfx2.model.enums.QuartalArt;
import org.example.testfx2.repository.GetJarhlichData;
import org.example.testfx2.views.NeueAuswertungView;

import java.util.List;

public class NeueAuswertungController {
    private ObservableList<Quartal> quartalList;

    public NeueAuswertungController() {
       quartalList = FXCollections.observableArrayList(GetJarhlichData.getMainTable());
    }

    public void openForm() {
        NeueAuswertungView view = new NeueAuswertungView();
        view.show(q -> {
            int newId = quartalList.size() + 1;
            q.setId(newId);
            GetJarhlichData.addQuartal(q);
            quartalList.add(q);
        });
    }
    public Quartal buildQuartal(Integer jahr, QuartalArt quartal, Status status, User user, String kommentar) {
        int id = GetJarhlichData.getMainTable().size() + 1;
        String userName = user != null ? user.getUserName() : "";

        return new Quartal(id, jahr, quartal, status, userName, kommentar);
    }
    public void saveQuartal(Quartal q) {
        GetJarhlichData.addQuartal(q);
    }
}
