package org.example.testfx2.controller;

import javafx.collections.ObservableList;
import org.example.testfx2.model.Inventur;
import org.example.testfx2.model.InventurArtikel;
import org.example.testfx2.model2.ArtikelDetailStandort;
import org.example.testfx2.model2.InventurOutput;
import org.example.testfx2.repository.ArtikelRepo;
import org.example.testfx2.repository.InventurArtikelRepo;
import org.example.testfx2.repository.InventurRepo;
import org.example.testfx2.views.InventurListView;

import java.sql.SQLException;

// InventurController.java
public class InventurController {
    public void openForm(int quartalId) throws SQLException {
        new InventurListView().show( quartalId);
    }
    public ObservableList<InventurOutput> getInventuroutputList(int quartalId ) throws SQLException {
      return InventurRepo.getInventurObservableList(quartalId);
    }
    
    public void saveInventur(Inventur inventur) {
       // InventurRepo.updateInventur(inventur);
    }
    
    public void saveInventurArtikel(InventurArtikel inventurArtikel) {
        InventurArtikelRepo.updateInventurArtikel(inventurArtikel);
    }

    public ObservableList<ArtikelDetailStandort>  getArtikelListWithSelectedStandort(int quartalid, String standort) {
        return ArtikelRepo.getArtikelObservableListWithStandort(quartalid, standort);
    }
}