package org.example.testfx2.views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.testfx2.model.Berechnung;
import org.example.testfx2.model.BerechnungDetail;
import org.example.testfx2.repository.BerechnungDetailRepo;
import org.example.testfx2.repository.BerechnungRepo;
import org.example.testfx2.utils.AlertUtil;
import org.example.testfx2.utils.ModernButton;
import org.example.testfx2.utils.Utilitie;
import org.example.testfx2.utils.ViewNavigator;

import java.sql.SQLException;
import java.util.List;

// BerechnungView.java
public class BerechnungView {
    private TableView<Berechnung> berechnungTable;
    private TableView<BerechnungDetail> detailTable;


    private Button abbrechenButton = new ModernButton("Abbrechen");
    private Button sichernButton = new Button("Sichern");
    private Button weiterButton = new Button("Weiter");
    
    private final ObjectProperty<Berechnung> selectedBerechnung = new SimpleObjectProperty<>();
    private Label detailLabel = new Label("Berechnungen");
    
    public void show() throws SQLException {
        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        ViewNavigator.switchViews(scene, "Berechnungen");
    }
    
    private Scene createScene() throws SQLException {
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(20));
        
        // Header
        Label headerLabel = new Label("Berechnungen");

        // Ana tablo
        VBox berechnungBox = createBerechnungBox();
        
        // Detay tablo
        VBox detailBox = createDetailBox();
        
        // Butonlar
        HBox buttonBox = createButtonBox();

        weiterButton.setDisable(true);
        mainBox.getChildren().addAll(headerLabel, berechnungBox, detailBox, buttonBox);
        return new Scene(mainBox, Utilitie.APP_WIDH,Utilitie.APP_HEIGHT);
    }
    
    private VBox createBerechnungBox() throws SQLException {
        VBox vbox = new VBox(10);
        Label label = new Label("Berechnungen");
        
        berechnungTable = new TableView<>();
        berechnungTable.setItems(BerechnungRepo.getBerechnungObservableList());
        berechnungTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        berechnungTable.setPrefHeight(200);
        
        TableColumn<Berechnung, String> materialCol = new TableColumn<>("Material");
        materialCol.setCellValueFactory(new PropertyValueFactory<>("material"));
        
        TableColumn<Berechnung, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        TableColumn<Berechnung, Double> preisCol = new TableColumn<>("Preis pro gram");
        preisCol.setCellValueFactory(new PropertyValueFactory<>("preisProGram"));
        preisCol.setCellFactory(column -> new TableCell<Berechnung, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.format("%,.6f", item));
            }
        });
        
        TableColumn<Berechnung, Double> summeCol = new TableColumn<>("Summe");
        summeCol.setCellValueFactory(new PropertyValueFactory<>("summe"));
        summeCol.setCellFactory(column -> new TableCell<Berechnung, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.format("%,.3f", item));
            }
        });
        
        berechnungTable.getColumns().addAll(materialCol, statusCol, preisCol, summeCol);
        
        // Selection listener
        berechnungTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                selectedBerechnung.set(newSelection);
                weiterButton.setDisable(newSelection == null);
                if (newSelection != null) {
                    detailLabel.setText(newSelection.getMaterial() + " (selektiert)");
                    loadDetails(newSelection.getId());
                } else {
                    detailLabel.setText("Berechnungen");
                    detailTable.setItems(FXCollections.observableArrayList());
                }
            });
        
        vbox.getChildren().addAll(label, berechnungTable);
        return vbox;
    }
    
    private VBox createDetailBox() {
        VBox vbox = new VBox(10);
        
        detailTable = new TableView<>();
        detailTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        detailTable.setPrefHeight(200);
        
        TableColumn<BerechnungDetail, String> rechnungsnrCol = new TableColumn<>("Rechnungsnummer");
        rechnungsnrCol.setCellValueFactory(new PropertyValueFactory<>("rechnungsnummer"));
        
        TableColumn<BerechnungDetail, Double> mengeCol = new TableColumn<>("Menge");
        mengeCol.setCellValueFactory(new PropertyValueFactory<>("menge"));
        mengeCol.setCellFactory(column -> new TableCell<BerechnungDetail, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.format("%,.0f", item));
            }
        });
        
        TableColumn<BerechnungDetail, String> massCol = new TableColumn<>("Maß");
        massCol.setCellValueFactory(new PropertyValueFactory<>("mass"));
        
        TableColumn<BerechnungDetail, Double> kostenCol = new TableColumn<>("Kosten");
        kostenCol.setCellValueFactory(new PropertyValueFactory<>("kosten"));
        kostenCol.setCellFactory(column -> new TableCell<BerechnungDetail, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.format("%,.3f", item));
            }
        });
        
        TableColumn<BerechnungDetail, Double> summeCol = new TableColumn<>("Summe");
        summeCol.setCellValueFactory(new PropertyValueFactory<>("summe"));
        summeCol.setCellFactory(column -> new TableCell<BerechnungDetail, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.format("%,.3f", item));
            }
        });
        
        detailTable.getColumns().addAll(rechnungsnrCol, mengeCol, massCol, kostenCol, summeCol);
        
        vbox.getChildren().addAll(detailLabel, detailTable);
        return vbox;
    }
    
    private HBox createButtonBox() {
        HBox hbox = new HBox(10);
        abbrechenButton.getStyleClass().addAll("button-modern");
        sichernButton.getStyleClass().addAll("button-modern");
        weiterButton.getStyleClass().addAll("button-modern");
        
        abbrechenButton.setOnAction(e -> {
            try {
                new MainView().show();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        
        sichernButton.setOnAction(e -> saveBerechnungen());
        weiterButton.setOnAction(e -> {
	        try {

		        new GitterboxBerechnungView(selectedBerechnung.get()).show();
	        } catch (SQLException ex) {
		        throw new RuntimeException(ex);
	        }
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hbox.getChildren().addAll(spacer, abbrechenButton, sichernButton, weiterButton);
        
        return hbox;
    }
    
    private void loadDetails(int berechnungId) {
        List<BerechnungDetail> details = BerechnungDetailRepo.getDetailsByBerechnungId(berechnungId);
        detailTable.setItems(FXCollections.observableArrayList(details));
    }
    
    private void saveBerechnungen() {
        // Save logic here
        AlertUtil.showSuccessAlert("Erfolg", "Berechnungen erfolgreich gespeichert");
    }

}