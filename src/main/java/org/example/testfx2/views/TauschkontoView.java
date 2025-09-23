package org.example.testfx2.views;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.testfx2.model.KundeSaldo;
import org.example.testfx2.model.TauschkontoUebersicht;
import org.example.testfx2.repository.TauschkontoRepo;
import org.example.testfx2.utils.ModernButton;
import org.example.testfx2.utils.Utilitie;
import org.example.testfx2.utils.ViewNavigator;
import javafx.geometry.Insets;
import java.sql.SQLException;

// TauschkontoView.java
public class TauschkontoView {
    private MainView mainView=new MainView();
    private BerechnungView berechnungView=new BerechnungView();
    private TableView<TauschkontoUebersicht> uebersichtTable;
    private TableView<KundeSaldo> kundenTable;
    private Button importButton = new ModernButton("Import");
    private Button exportPdfButton = new ModernButton("Export as PDF");
    private Button bearbeitenButton = new ModernButton("Bearbeiten");
    private Button abnahmeButton = new ModernButton("Abnahme");
    private Button editierenButton = new ModernButton("Editieren");
    private Button hinzufuegenButton = new ModernButton("Hinzufügen");
    private Button abbrechenButton = new ModernButton("Abbrechen");
    private Button sichernButton = new ModernButton("Sichern");
    private Button weiterButton = new ModernButton("Weiter");

	public TauschkontoView() throws SQLException {
	}

	public void show() throws SQLException {
        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        ViewNavigator.switchViews(scene, "Tauschkonten");
    }

    private Scene createScene() throws SQLException {
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(20));

        // Header
        Label headerLabel = new Label("Tauschkonten Übersicht");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Üst kısım: Butonlar ve özet tablo
        VBox uebersichtBox = createUebersichtBox();

        // Orta kısım: Abnahme, Editieren, Hinzufügen butonları
        HBox mittelButtonBox = createMittelButtonBox();

        // Alt kısım: Kunden tablosu
        VBox kundenBox = createKundenBox();

        // En alt: Abbrechen, Sichern, Weiter butonları
        HBox untenButtonBox = createUntenButtonBox();

        mainBox.getChildren().addAll(headerLabel, uebersichtBox, mittelButtonBox, kundenBox, untenButtonBox);
        return new Scene(mainBox, Utilitie.APP_WIDH,Utilitie.APP_HEIGHT);
    }

    private VBox createUebersichtBox() throws SQLException {
        VBox vbox = new VBox(10);

        // Üst butonlar: Import, Export as PDF, Bearbeiten
        HBox topButtonBox = new HBox(10);

        // Butonları sağa hizala
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topButtonBox.getChildren().addAll(spacer, importButton, exportPdfButton, bearbeitenButton);

        // Tablo başlığı
        Label tableLabel = new Label("Tauschkonten Übersicht");

        // Özet tablosu
        uebersichtTable = new TableView<>();
        uebersichtTable.setItems(FXCollections.observableArrayList(
                TauschkontoRepo.getTauschkontoUebersicht()
        ));
        uebersichtTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TauschkontoUebersicht, String> artCol = new TableColumn<>("Art");
        artCol.setCellValueFactory(new PropertyValueFactory<>("art"));

        TableColumn<TauschkontoUebersicht, Integer> saldoCol = new TableColumn<>("Saldo");
        saldoCol.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        saldoCol.setCellFactory(column -> new TableCell<TauschkontoUebersicht, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.format("%,d", item));
            }
        });

        TableColumn<TauschkontoUebersicht, Double> summeCol = new TableColumn<>("Summe");
        summeCol.setCellValueFactory(new PropertyValueFactory<>("summe"));
        summeCol.setCellFactory(column -> new TableCell<TauschkontoUebersicht, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.format("%,.0f", item));
            }
        });

        uebersichtTable.getColumns().addAll(artCol, saldoCol, summeCol);
        uebersichtTable.setPrefHeight(150);

        // Toplam label
        double gesamtSumme = TauschkontoRepo.getTauschkontoUebersicht().stream()
                .mapToDouble(TauschkontoUebersicht::getSumme)
                .sum();
        Label gesamtLabel = new Label("Summe: " + String.format("%,.0f", gesamtSumme));
        gesamtLabel.setStyle("-fx-font-weight: bold;");

        vbox.getChildren().addAll(topButtonBox, tableLabel, uebersichtTable, gesamtLabel);
        return vbox;
    }

    private HBox createMittelButtonBox() {
        HBox hbox = new HBox(10);

        hbox.getChildren().addAll(abnahmeButton, editierenButton, hinzufuegenButton);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        return hbox;
    }

    private VBox createKundenBox() throws SQLException {
        VBox vbox = new VBox(10);
        Label label = new Label("Kunde");

        kundenTable = new TableView<>();
        kundenTable.setItems(FXCollections.observableArrayList(
                TauschkontoRepo.getKundenMitSaldos()
        ));
        kundenTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<KundeSaldo, String> nameCol = new TableColumn<>("Kunde");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("kundeName"));

        TableColumn<KundeSaldo, Integer> saldoCol = new TableColumn<>("Saldo");
        saldoCol.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        saldoCol.setCellFactory(column -> new TableCell<KundeSaldo, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%,d", item));
                    // Negatif değerler için kırmızı renk
                    if (item < 0) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });

        TableColumn<KundeSaldo, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        kundenTable.getColumns().addAll(nameCol, saldoCol, statusCol);
        kundenTable.setPrefHeight(200);

        // Kunden toplamı
        int kundenGesamtSumme = TauschkontoRepo.getKundenMitSaldos().stream()
                .mapToInt(KundeSaldo::getSaldo)
                .sum();
        Label kundenSummeLabel = new Label("Summe: " + String.format("%,d", kundenGesamtSumme));
        kundenSummeLabel.setStyle("-fx-font-weight: bold;");

        vbox.getChildren().addAll(label, kundenTable, kundenSummeLabel);
        return vbox;
    }

    private HBox createUntenButtonBox() {
        HBox hbox = new HBox(10);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        abbrechenButton.setOnAction(e-> {
	        try {
		        mainView.show();
	        } catch (SQLException ex) {
		        throw new RuntimeException(ex);
	        }
        });
        weiterButton.setOnAction(e-> {
	        try {
		        berechnungView.show();
	        } catch (SQLException ex) {
		        throw new RuntimeException(ex);
	        }
        });
        hbox.getChildren().addAll(spacer, abbrechenButton, sichernButton, weiterButton);

        return hbox;
    }
}