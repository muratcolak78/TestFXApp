package org.example.testfx2.controller;

import javafx.collections.ObservableList;
import org.example.testfx2.model.Inventur;
import org.example.testfx2.model.InventurArtikel;
import org.example.testfx2.model2.ArtikelDetailStandort;
import org.example.testfx2.model2.InventurOutput;
import org.example.testfx2.model2.Tauschkonten;
import org.example.testfx2.repository.ArtikelRepo;
import org.example.testfx2.repository.InventurArtikelRepo;
import org.example.testfx2.repository.InventurRepo;
import org.example.testfx2.views.InventurListView;

import java.sql.SQLException;


public class InventurController {

    public void openForm() throws SQLException {
        new InventurListView().show();
    }
    public ObservableList<InventurOutput> getInventuroutputList() throws SQLException {
      return InventurRepo.getInventurObservableList();
    }
    
    public void saveInventur(Inventur inventur) {

    }
    
    public void saveInventurArtikel(InventurArtikel inventurArtikel) {
        InventurArtikelRepo.updateInventurArtikel(inventurArtikel);
    }

    public ObservableList<ArtikelDetailStandort>  getArtikelListWithSelectedStandort(String standort) {
        return ArtikelRepo.getArtikelObservableListWithStandort(standort);
    }



}