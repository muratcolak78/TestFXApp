package org.example.testfx2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.model.Quartal;
import org.example.testfx2.repository.GetJarhlichData;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainController {
    private ObservableList<Quartal> quartalList;

    public MainController() {
        quartalList = FXCollections.observableArrayList(GetJarhlichData.getMainTable());
    }

    public ObservableList<Quartal> getQuartalList() {
        return quartalList;
    }

    public void handleNeueAuswertung() {
        // Form açılır, yeni Quartal eklenir
    }

    public void addQuartal(Quartal q) {
        GetJarhlichData.addQuartal(q);
        quartalList.add(q); // TableView otomatik güncellenir
    }
    public void openChecklistPdf() {
        try {
            File pdfFile = new File("src/main/resources/docs/checklist.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                System.out.println("PDF dosyası bulunamadı.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
