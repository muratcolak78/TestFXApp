package org.example.testfx2.controller;

import org.example.testfx2.model.Inventur;
import org.example.testfx2.model.InventurArtikel;
import org.example.testfx2.repository.InventurArtikelRepo;
import org.example.testfx2.repository.InventurRepo;
import org.example.testfx2.views.InventurListView;

import java.sql.SQLException;

// InventurController.java
public class InventurController {
    public void openInventurList() throws SQLException {
        new InventurListView().show();
    }
    
    public void saveInventur(Inventur inventur) {
        InventurRepo.updateInventur(inventur);
    }
    
    public void saveInventurArtikel(InventurArtikel inventurArtikel) {
        InventurArtikelRepo.updateInventurArtikel(inventurArtikel);
    }
}