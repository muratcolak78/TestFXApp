package org.example.testfx2.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import org.example.testfx2.model.Berechnung;
import org.example.testfx2.utils.AlertUtil;
import org.example.testfx2.utils.ModernButton;
import org.example.testfx2.utils.Utilitie;
import org.example.testfx2.utils.ViewNavigator;

import java.sql.SQLException;

// GitterboxBerechnungView.java
public class GitterboxBerechnungView {
    private Berechnung selectedBerechnung;
    private TextField strompreisField = new TextField("0,2452");
    private TextField schweissdrahtField = new TextField("0,001944");
    private TextField stahlbandField = new TextField("0,000673");
    private TextField drahtField = new TextField("0,000785");
    private TextField rundstangeField = new TextField("0,000895");
    private TextField gitterboxField = new TextField("80");
    private TextField halbeGitterboxField = new TextField("58");
    
    private Button abbrechenButton = new ModernButton("Abbrechen");
    private Button sichernButton = new ModernButton("Sichern");
    private Button weiterButton = new ModernButton("Weiter");
    
    public GitterboxBerechnungView(Berechnung selectedBerechnung) {
        this.selectedBerechnung = selectedBerechnung;
    }

    public void show() throws SQLException {
        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        ViewNavigator.switchViews(scene, "Gitterbox Berechnung");
    }
    
    private Scene createScene() {
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(20));
        
        // Header
        Label headerLabel = new Label("Berechnung Gitterbox");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Seçilen material bilgisi
        Label materialLabel = new Label("Ausgewähltes Material: " + 
            (selectedBerechnung != null ? selectedBerechnung.getMaterial() : "Keine Auswahl"));
        
        // Input alanları
        VBox inputBox = createInputBox();
        
        // Butonlar
        HBox buttonBox = createButtonBox();
        
        mainBox.getChildren().addAll(headerLabel, materialLabel, inputBox, buttonBox);
        return new Scene(mainBox, Utilitie.APP_WIDH,Utilitie.APP_HEIGHT);
    }
    
    private VBox createInputBox() {
        VBox vbox = new VBox(10);
        
        // Preise section
        Label preiseLabel = new Label("Preise:");
        preiseLabel.setStyle("-fx-font-weight: bold;");
        
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        
        // Row 1
        grid.add(new Label("Strompreis pro KWh"), 0, 0);
        grid.add(strompreisField, 1, 0);
        grid.add(new Label("€"), 2, 0);
        
        // Row 2
        grid.add(new Label("Schweißdraht"), 0, 1);
        grid.add(schweissdrahtField, 1, 1);
        grid.add(new Label("€"), 2, 1);
        
        // Row 3
        grid.add(new Label("Stahlband"), 0, 2);
        grid.add(stahlbandField, 1, 2);
        grid.add(new Label("€"), 2, 2);
        
        // Row 4
        grid.add(new Label("Draht"), 0, 3);
        grid.add(drahtField, 1, 3);
        grid.add(new Label("€"), 2, 3);
        
        // Row 5
        grid.add(new Label("Rundstange pro Kilogramm"), 0, 4);
        grid.add(rundstangeField, 1, 4);
        grid.add(new Label("€"), 2, 4);
        
        // Gitterbox Preise section
        Label gitterboxLabel = new Label("Gitterbox Preise:");
        gitterboxLabel.setStyle("-fx-font-weight: bold;");
        
        GridPane gitterboxGrid = new GridPane();
        gitterboxGrid.setVgap(10);
        gitterboxGrid.setHgap(10);
        gitterboxGrid.setPadding(new Insets(10));
        
        // Row 1
        gitterboxGrid.add(new Label("Gitterbox"), 0, 0);
        gitterboxGrid.add(gitterboxField, 1, 0);
        gitterboxGrid.add(new Label("€"), 2, 0);
        
        // Row 2
        gitterboxGrid.add(new Label("halbe Gitterbox"), 0, 1);
        gitterboxGrid.add(halbeGitterboxField, 1, 1);
        gitterboxGrid.add(new Label("€"), 2, 1);
        
        vbox.getChildren().addAll(preiseLabel, grid, gitterboxLabel, gitterboxGrid);
        return vbox;
    }
    
    private HBox createButtonBox() {
        HBox hbox = new HBox(10);

        abbrechenButton.setOnAction(e -> {
            try {
                new BerechnungView().show();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        
        sichernButton.setOnAction(e -> saveGitterboxBerechnung());
        weiterButton.setOnAction(e -> {
	        try {
		        new ZusammenFassung().show();
	        } catch (SQLException ex) {
		        throw new RuntimeException(ex);
	        }
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hbox.getChildren().addAll(spacer, abbrechenButton, sichernButton, weiterButton);
        
        return hbox;
    }
    
    private void saveGitterboxBerechnung() {
        // Save logic here
        try {
            // Değerleri al ve kaydet
            double strompreis = parseGermanDouble(strompreisField.getText());
            double schweissdraht = parseGermanDouble(schweissdrahtField.getText());
            double stahlband = parseGermanDouble(stahlbandField.getText());
            double draht = parseGermanDouble(drahtField.getText());
            double rundstange = parseGermanDouble(rundstangeField.getText());
            double gitterbox = parseGermanDouble(gitterboxField.getText());
            double halbeGitterbox = parseGermanDouble(halbeGitterboxField.getText());
            
            // TODO: Database'e kaydet
            
            AlertUtil.showSuccessAlert("Erfolg", "Gitterbox Berechnung gespeichert");
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Fehler", "Ungültige Eingabe: " + e.getMessage());
        }
    }
    
    private double parseGermanDouble(String text) {
        // Alman formatındaki sayıyı parse et (virgül ile)
        return Double.parseDouble(text.replace(",", ".").replace("€", "").trim());
    }
    
    private void showNextView() {
        // Bir sonraki view'a geçiş
        AlertUtil.showInfoAlert("Info", "Weiter zu nächster Ansicht");
    }
}