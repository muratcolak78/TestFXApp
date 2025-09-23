package org.example.testfx2.views;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.testfx2.utils.AlertUtil;
import org.example.testfx2.utils.ModernButton;
import org.example.testfx2.utils.Utilitie;
import org.example.testfx2.utils.ViewNavigator;

import java.sql.SQLException;

public class ZusammenFassung {

	private Button abbrechenButton = new ModernButton("Abbrechen");
	private Button sichernButton = new Button("Sichern");
	private Button excelExport = new Button("Excel Export");

	public void show() throws SQLException {
		Scene scene = createScene();
		scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
		ViewNavigator.switchViews(scene, "Berechnungen");
	}
	private Scene createScene() throws SQLException {
		VBox mainBox = new VBox(20);
		mainBox.setPadding(new Insets(20));

		// Header
		Label headerLabel = new Label("Zusammenfassung");
		VBox vBox=createZusammenFassungBox();



		mainBox.getChildren().addAll(headerLabel, vBox);
		return new Scene(mainBox, Utilitie.APP_WIDH,Utilitie.APP_HEIGHT);
	}

	private VBox createZusammenFassungBox() {
		Label artikelListe = new Label("Artikel Liste       ");
		Label artikelListeOK = new Label(getArtikelStatus());
		HBox artikelHbox=new HBox();
		artikelHbox.setPadding(new Insets(10));
		artikelHbox.getChildren().addAll(artikelListe,artikelListeOK);

		Label inventur = new Label("Inventur            ");
		Label inventurOK = new Label(getInventurStatus());
		HBox inventurHbox=new HBox();
		inventurHbox.setPadding(new Insets(10));
		inventurHbox.getChildren().addAll(inventur,inventurOK);

		Label tauschkonten = new Label("Tauschkonten    ");
		Label tauschkontenOK = new Label(getTauschkontenStatus());
		HBox tauschkontenHbox=new HBox();
		tauschkontenHbox.setPadding(new Insets(10));
		tauschkontenHbox.getChildren().addAll(tauschkonten, tauschkontenOK);


		Label berechnungen = new Label("Berechnungen    ");
		Label berechnungenOK = new Label(getBerechnungenStatus());
		HBox berechnungenHbox=new HBox();
		berechnungenHbox.setPadding(new Insets(10));
		berechnungenHbox.getChildren().addAll(berechnungen, berechnungenOK);

		HBox buttonsHbox=createButtonBox();

		VBox vBox=new VBox();
		vBox.getChildren().addAll(artikelHbox, inventurHbox, tauschkontenHbox, berechnungenHbox, buttonsHbox);
		return vBox;
	}

	private String getArtikelStatus(){
		return "OK";
	}
	private String getInventurStatus(){
		return "OK";
	}
	private String getTauschkontenStatus(){
		return "OK";
	}
	private String getBerechnungenStatus(){
		return "OK";
	}


	private HBox createButtonBox() {
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.BASELINE_RIGHT);
		abbrechenButton.getStyleClass().addAll("button-modern");
		sichernButton.getStyleClass().addAll("button-modern");
		excelExport.getStyleClass().addAll("button-modern");

		abbrechenButton.setOnAction(e -> {
			try {
				new MainView().show();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});

		sichernButton.setOnAction(e -> saveBerechnungen());
		excelExport.setOnAction(e -> excelExport());

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		hbox.getChildren().addAll(spacer, abbrechenButton, sichernButton, excelExport);

		return hbox;
	}


	private void saveBerechnungen() {
		// Save logic here
		AlertUtil.showSuccessAlert("Erfolg", "Berechnungen erfolgreich gespeichert");
	}
	private void excelExport() {
		// Save logic here
		AlertUtil.showSuccessAlert("Erfolg", "Berechnungen erfolgreich als excel exportet");
	}
}
