package org.example.testfx2.controller;

import javafx.collections.ObservableList;
import org.example.testfx2.model2.QuartalOutput;
import org.example.testfx2.repository.QurtalDataRepo;
import org.example.testfx2.utils.AlertUtil;
import org.example.testfx2.views.MainView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class MainController {


    public void openForm() throws SQLException {
        new MainView().show();
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

    public ObservableList<QuartalOutput> getMainTable() throws SQLException {
        return QurtalDataRepo.getMainTable();
    }


}
