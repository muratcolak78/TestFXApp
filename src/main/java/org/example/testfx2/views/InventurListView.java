package org.example.testfx2.views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.testfx2.controller.InventurController;
import org.example.testfx2.model.InventurArtikel;
import org.example.testfx2.model2.ArtikelDetailStandort;
import org.example.testfx2.model2.InventurOutput;
import org.example.testfx2.model2.QuartalOutput;
import org.example.testfx2.model2.Standort;
import org.example.testfx2.repository.*;
import org.example.testfx2.utils.AlertUtil;
import org.example.testfx2.utils.ModernButton;
import org.example.testfx2.utils.Utilitie;
import org.example.testfx2.utils.ViewNavigator;

import java.sql.SQLException;
import java.util.List;

import javafx.geometry.Insets;

public class InventurListView {

    private TauschkontoView tauschkontoView=new TauschkontoView();
    private InventurController inventurController=new InventurController();
    private TableView<InventurOutput> table = new TableView<>();
    private TableView<InventurArtikel> inventurArtikelTable;
    private Button abbrechenButton = new ModernButton("Abbrechen");
    private Button sichernButton = new ModernButton("Sichern");
    private Button weiterButton = new ModernButton("Weiter");
    private VBox inventurDetailsBox = new VBox();

    private final ObjectProperty<InventurOutput> selectedInventur = new SimpleObjectProperty<>();

    private final  Label detailsLabel = new Label("Inventur details");
    private int selectedQuartalId;

	public InventurListView() throws SQLException {
	}

	public void show(int selectedQuartalId) throws SQLException {
        this.selectedQuartalId=selectedQuartalId;
        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        ViewNavigator.switchViews(scene,"Inventur");

    }

    private Scene createScene() throws SQLException {
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(20));
        //initializeTableSelection();
        VBox inventurListBox=createInventurListBox();
        HBox downHbox=createDownHbox();

        mainBox.getChildren().addAll(inventurListBox, inventurDetailsBox, downHbox);

        return new Scene(mainBox, Utilitie.APP_WIDH,Utilitie.APP_HEIGHT);
    }

    private HBox createDownHbox() {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        abbrechenButton.setOnAction(e -> {
            try {
                new MainView().show();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

       // sichernButton.setOnAction(e -> saveInventur());
        weiterButton.setOnAction(e-> {
	        try {
		        tauschkontoView.show();
	        } catch (SQLException ex) {
		        throw new RuntimeException(ex);
	        }
        });

        buttonBox.getChildren().addAll(abbrechenButton, sichernButton, weiterButton);
        return buttonBox;
    }

    private VBox createInventurListBox() throws SQLException {
        VBox vBox=new VBox();
        vBox.setPrefHeight(300);
        Label inventurLabel = new Label("Inventurliste");
        TableView<InventurOutput> inventurOutputTableView=createInventurTable();
        vBox.getChildren().addAll(inventurLabel, inventurOutputTableView);
        return vBox;
    }
    private VBox createinventurDetailsBox(String standort) {
        VBox vBox=new VBox();
        Label detailsLabel = new Label("Inventur details");
        TableView<ArtikelDetailStandort> table=createInventurArtikelTable(standort);
        vBox.getChildren().addAll(detailsLabel, table);
        return vBox;
    }

    private TableView<InventurOutput> createInventurTable() throws SQLException {
        ObservableList<InventurOutput> getInventurObservableList=inventurController.getInventuroutputList(selectedQuartalId);

        table.setItems(getInventurObservableList);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        System.out.println(table.getItems().size());

        TableColumn<InventurOutput, String> standortCol = new TableColumn<>("Standort");
        standortCol.setCellValueFactory(new PropertyValueFactory<>("standort"));


        TableColumn<InventurOutput, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<InventurOutput, String> abgabePersonCol = new TableColumn<>("Abgabe Person");
        abgabePersonCol.setCellValueFactory(new PropertyValueFactory<>("abgabeBei"));

        TableColumn<InventurOutput, String> abgabeDatumCol = new TableColumn<>("Abgabe Datum");
        abgabeDatumCol.setCellValueFactory(new PropertyValueFactory<>("abgabeDatum"));

        TableColumn<InventurOutput, String> abnahmePersonCol = new TableColumn<>("Abnahme Person");
        abnahmePersonCol.setCellValueFactory(new PropertyValueFactory<>("abnahmeBei"));

        TableColumn<InventurOutput, Double> summeGueterCol = new TableColumn<>("Summe Gitterbox");
        summeGueterCol.setCellValueFactory(new PropertyValueFactory<>("summeGitterBoxPreis"));

        TableColumn<InventurOutput, Double> summePalettenCol = new TableColumn<>("Summe Pale");
        summePalettenCol.setCellValueFactory(new PropertyValueFactory<>("summePalepreis"));

        TableColumn<InventurOutput, Double> summeCol = new TableColumn<>("Summe");
        summeCol.setCellValueFactory(new PropertyValueFactory<>("summe"));

        table.getColumns().addAll(standortCol, statusCol, abgabePersonCol, abgabeDatumCol,
                abnahmePersonCol, summeGueterCol, summePalettenCol, summeCol);

        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // çift tıklama
                openSelectedQuartal();
            }
        });

        return table;
    }


    private void openSelectedQuartal() {
        InventurOutput selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String standort = selected.getStandort();
            VBox newDetailsBox = createinventurDetailsBox(standort);
            inventurDetailsBox.getChildren().setAll(newDetailsBox.getChildren());
        }
    }

    private TableView<ArtikelDetailStandort> createInventurArtikelTable(String selected) {
        ObservableList<ArtikelDetailStandort> detailStandortObservableList= inventurController.getArtikelListWithSelectedStandort(selectedQuartalId,selected);
        TableView<ArtikelDetailStandort> table = new TableView<>();
        table.setItems(detailStandortObservableList);
        table.setPrefHeight(200);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ArtikelDetailStandort, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<ArtikelDetailStandort, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<ArtikelDetailStandort, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<ArtikelDetailStandort, Double> summeCol = new TableColumn<>("Summe");
        summeCol.setCellValueFactory(new PropertyValueFactory<>("summe"));

        table.getColumns().addAll(nameCol, statusCol, typeCol, summeCol);

        return table;
    }



}

