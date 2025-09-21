package org.example.testfx2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.model.Quartal;
import org.example.testfx2.model.QuartalDTO;
import org.example.testfx2.repository.QurtalDataRepo;
import org.example.testfx2.service.QuartalService;
import org.example.testfx2.utils.AlertUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class MainController {
    private final QuartalService quartalService;
    private final ObservableList<Quartal> quartalList;

    public MainController(QuartalService quartalService) throws SQLException {
        this.quartalService = quartalService;
        this.quartalList = quartalService.getAllQuartals();
    }

    public void openChecklistPdf() {
        try {
            File pdfFile = new File("src/main/resources/docs/checklist.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
                AlertUtil.showSuccessAlert("CheckList","bitte boebeacten Sie die Liste ");
            } else {
                System.out.println("PDF dosyası bulunamadı.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
