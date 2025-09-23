package org.example.testfx2.views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.testfx2.model.Artikel;
import org.example.testfx2.model.Inventur;
import org.example.testfx2.model.InventurArtikel;
import org.example.testfx2.model.Standort;
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

    private TableView<Inventur> inventurTable;
    private TableView<InventurArtikel> inventurArtikelTable;
    private Button abbrechenButton = new ModernButton("Abbrechen");
    private Button sichernButton = new ModernButton("Sichern");
    private Button weiterButton = new ModernButton("Weiter");

    private final ObjectProperty<Inventur> selectedInventur = new SimpleObjectProperty<>();

    private final  Label detailsLabel = new Label("Inventur details");

	public InventurListView() throws SQLException {
	}

	public void show() throws SQLException {
        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        ViewNavigator.switchViews(scene,"Inventur");
    }

    private Scene createScene() throws SQLException {
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(20));

        VBox inventurListBox=createInventurListBox();
        VBox inventurDetailsBox=createinventurDetailsBox();
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

        sichernButton.setOnAction(e -> saveInventur());
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
        inventurTable = createInventurTable();
        vBox.getChildren().addAll(inventurLabel,inventurTable);
        return vBox;
    }
    private VBox createinventurDetailsBox() {
        VBox vBox=new VBox();
        Label detailsLabel = new Label("Inventur details");
        inventurArtikelTable = createInventurArtikelTable();
        vBox.getChildren().addAll(detailsLabel,inventurArtikelTable);
        return vBox;
    }

    private TableView<Inventur> createInventurTable() throws SQLException {
        TableView<Inventur> table = new TableView<>();
        table.setItems(InventurRepo.getInventurObservableList());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Inventur, String> standortCol = new TableColumn<>("Standort");
        standortCol.setCellValueFactory(cellData -> {
            try {
                int standortId = cellData.getValue().getStandortId();
                String standortName = StandortRepo.getStandortNameByIdSafe(standortId);
                return new SimpleStringProperty(standortName);
            } catch (Exception e) {
                return new SimpleStringProperty("Error");
            }
        });

        TableColumn<Inventur, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Inventur, String> abgabeDatumCol = new TableColumn<>("Abgabe");
        abgabeDatumCol.setCellValueFactory(new PropertyValueFactory<>("abgabeDatum"));

        TableColumn<Inventur, String> abgabePersonCol = new TableColumn<>("Abgabe Person");
        abgabePersonCol.setCellValueFactory(cellData -> {
            int personalId = cellData.getValue().getAbgabePerson();
            String abgabePersonName = UsersRepo.getUserById(personalId);
            return new SimpleStringProperty(abgabePersonName);
        });

        TableColumn<Inventur, String> abnahmeDatumCol = new TableColumn<>("Abnahme Datum");
        abnahmeDatumCol.setCellValueFactory(new PropertyValueFactory<>("abnahmeDatum"));

        TableColumn<Inventur, String> abnahmePersonCol = new TableColumn<>("Abnahme Person");
        abnahmePersonCol.setCellValueFactory(cellData -> {
            int personalId = cellData.getValue().getAbnahmePerson();
            String abbahmePersonName = UsersRepo.getUserById(personalId);
            return new SimpleStringProperty(abbahmePersonName);
        });

        TableColumn<Inventur, Double> summeGueterCol = new TableColumn<>("Summe Gitterb");
        summeGueterCol.setCellValueFactory(new PropertyValueFactory<>("summeGueter"));
        summeGueterCol.setCellFactory(column -> new TableCell<Inventur, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.3f €", item));
                }
            }
        });

        TableColumn<Inventur, Integer> summePalettenCol = new TableColumn<>("Summe Pale");
        summePalettenCol.setCellValueFactory(new PropertyValueFactory<>("summePaletten"));
        summePalettenCol.setCellFactory(column -> new TableCell<Inventur, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,d €", item));
                }
            }
        });

        TableColumn<Inventur, Double> summeCol = new TableColumn<>("Summe");
        summeCol.setCellValueFactory(cellData -> {
            Inventur inventur = cellData.getValue();
            double summe = inventur.getSummeGueter() + inventur.getSummePaletten();
            return new SimpleDoubleProperty(summe).asObject();
        });
        summeCol.setCellFactory(column -> new TableCell<Inventur, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.3f €", item));
                }
            }
        });

        table.getColumns().addAll(standortCol, statusCol, abgabePersonCol, abgabeDatumCol,
                abnahmePersonCol, summeGueterCol, summePalettenCol, summeCol);

        // Selection listener
        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedInventur.set(newSelection);
                    if (newSelection != null) {
                        // Başlığı güncelle: "Inventur details von [StandortName]"
                        try {
                            int standortId = newSelection.getStandortId();
                            Standort standort = StandortRepo.getStandortObservableList().stream()
                                    .filter(s -> s.getId() == standortId)
                                    .findFirst()
                                    .orElse(null);

                            if (standort != null) {
                                detailsLabel.setText("Inventur details von " + standort.getName() + " (selektiert)");
                            } else {
                                detailsLabel.setText("Inventur details");
                            }
                        } catch (SQLException e) {
                            detailsLabel.setText("Inventur details");
                        }

                        loadInventurArtikel(newSelection.getId());
                    } else {
                        detailsLabel.setText("Inventur details");
                        inventurArtikelTable.setItems(FXCollections.observableArrayList());
                    }
                });

        return table;
    }

    private TableView<InventurArtikel> createInventurArtikelTable() {
        TableView<InventurArtikel> table = new TableView<>();
        table.setPrefHeight(200);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<InventurArtikel, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> {
            try {
                int artikelId = cellData.getValue().getArtikelId();
                Artikel artikel = ArtikelRepo.getArtikelObservableList().stream()
                        .filter(a -> a.getId() == artikelId)
                        .findFirst()
                        .orElse(null);
                return new SimpleStringProperty(artikel != null ? artikel.getArtikelName() : "");
            } catch (SQLException e) {
                return new SimpleStringProperty("");
            }
        });

        TableColumn<InventurArtikel, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            Inventur inventur = selectedInventur.get();
            return new SimpleStringProperty(inventur != null ? inventur.getStatus() : "");
        });

        TableColumn<InventurArtikel, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> {
            try {
                int artikelId = cellData.getValue().getArtikelId();
                Artikel artikel = ArtikelRepo.getArtikelObservableList().stream()
                        .filter(a -> a.getId() == artikelId)
                        .findFirst()
                        .orElse(null);

                String type = "";
                if (artikel != null) {
                    // Hier müsste die Logik für den Typ basierend auf artikelType implementiert werden
                    type = artikel.getArtikelType() == 1 ? "Gitterbox" : "Paletten";
                }
                return new SimpleStringProperty(type);
            } catch (SQLException e) {
                return new SimpleStringProperty("");
            }
        });

        TableColumn<InventurArtikel, Double> summeCol = new TableColumn<>("Summe");
        summeCol.setCellValueFactory(new PropertyValueFactory<>("gesamtwert"));
        summeCol.setCellFactory(column -> new TableCell<InventurArtikel, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f€", item));
                }
            }
        });

        table.getColumns().addAll(nameCol, statusCol, typeCol, summeCol);

        return table;
    }

    private void loadInventurArtikel(int inventurId) {
        try {
            List<InventurArtikel> inventurArtikel = InventurArtikelRepo.getInventurArtikelByInventur(inventurId);
            inventurArtikelTable.setItems(FXCollections.observableArrayList(inventurArtikel));
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showErrorAlert("Fehler", "Fehler beim Laden der Inventur-Artikel");
            inventurArtikelTable.setItems(FXCollections.observableArrayList());
        }
    }

    private void saveInventur() {
        // Speichern der aktuellen Inventur
        Inventur selected = selectedInventur.get();
        if (selected != null) {
            try {
                InventurRepo.updateInventur(selected);

                // Speichern der Artikel
                for (InventurArtikel artikel : inventurArtikelTable.getItems()) {
                    InventurArtikelRepo.updateInventurArtikel(artikel);
                }

                AlertUtil.showSuccessAlert("Erfolg", "Inventur erfolgreich gespeichert");
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtil.showErrorAlert("Fehler", "Fehler beim Speichern der Inventur");
            }
        } else {
            AlertUtil.showWarningAlert("Warnung", "Bitte wählen Sie eine Inventur aus");
        }
    }

    private void showNextView() {
        // Logik für die nächste Ansicht
        AlertUtil.showInfoAlert("Info", "Weiter-Button wurde geklickt");
    }
}