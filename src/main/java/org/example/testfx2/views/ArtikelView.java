package org.example.testfx2.views;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.testfx2.controller.ArtikelController;
import org.example.testfx2.model2.ArtikelOutput;
import org.example.testfx2.utils.ModernButton;
import org.example.testfx2.utils.Utilitie;
import org.example.testfx2.utils.ViewNavigator;

import java.sql.SQLException;

public class ArtikelView {

	private ArtikelController artikelController=new ArtikelController();
	private MainView mainView=new MainView();
	private InventurListView inventurListView=new InventurListView();


	private TableView<ArtikelOutput> artikelEinkaufTable;
	private TableView<ArtikelOutput> artikelVerkaufTable;



	private Label artikellist =new Label("Artikellist");
	private Button Import=new ModernButton("Import");
	private Button exportAsPdf =new ModernButton("Export as PDF");
	private Button bearbeiten =new ModernButton("Bearbeiten");
	private Button Import2=new ModernButton("Import");
	private Button exportAsPdf2 =new ModernButton("Export as PDF");
	private Button bearbeiten2 =new ModernButton("Bearbeiten");

	private Button abbrechen=new ModernButton("Abbrechen");
	private Button sichern =new ModernButton("Sichern");
	private Button weiter =new ModernButton("Weiter");

	private int selectedQuartalId;

	private final ObjectProperty<ArtikelOutput> selectedArtikel = new SimpleObjectProperty<>();

	public ArtikelView(int selected) throws SQLException {
		this.selectedQuartalId=selected;
	}

	public void show() throws SQLException {
		Scene scene=createScene();
		scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
		ViewNavigator.switchViews(scene,"Artikel");
	}
	private Scene createScene() throws SQLException {
		VBox mainBox = new VBox();
		VBox artikelEinKaufVBox = createArtikelEinKaufVBox();

		// Önce tabloları oluştur
		artikelEinkaufTable = getTableArtikelEinkauf();
		artikelVerkaufTable = getTableArtikelVerkauf();
		// Tabloları doldur
		//artikelEinkaufTable.setItems(ArtikelRepo.getArtikelObservableList());
		//artikelVerkaufTable.setItems(ArtikelRepo.getArtikelObservableList());

		// Sonra selection listener'ları ekle
		initializeTableSelection();

		VBox artikelVerkaufVbox = createArtikelVerkaufVBox();
		HBox downButtonsHbox = createDownHBox();

		mainBox.getChildren().addAll(artikelEinKaufVBox, artikelEinkaufTable,
				artikelVerkaufVbox, artikelVerkaufTable, downButtonsHbox);
		return new Scene(mainBox, Utilitie.APP_WIDH, Utilitie.APP_HEIGHT);
	}

	private VBox createArtikelEinKaufVBox() {

		Import.setOnAction(e -> {
			artikelController.importArtikel();
		});


		exportAsPdf.setOnAction(e->artikelController.exportAsPdf());

		bearbeiten.setOnAction(e->artikelController.bearbeiten());



		HBox leftBox=new HBox(artikellist);
		HBox rightBox=new HBox(10,Import,exportAsPdf, bearbeiten);

		// Region for spacing
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		// Ana button call
		HBox buttonBox = new HBox(10, leftBox, spacer, rightBox);
		buttonBox.setAlignment(Pos.CENTER_LEFT);
		buttonBox.setAlignment(Pos.CENTER);
		return new VBox(15, buttonBox);
	}

	private VBox createArtikelVerkaufVBox() {


		Import2.setOnAction(e -> {
			artikelController.importArtikel();
		});


		exportAsPdf2.setOnAction(e->artikelController.exportAsPdf());

		bearbeiten2.setOnAction(e->artikelController.bearbeiten());

		HBox rightBox=new HBox(10,Import2,exportAsPdf2, bearbeiten2);
		// Region for spacing
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		// Ana button call
		HBox buttonBox = new HBox(10, spacer, rightBox);
		buttonBox.setAlignment(Pos.CENTER_LEFT);
		buttonBox.setAlignment(Pos.CENTER);
		return new VBox(15, buttonBox);

	}

	private HBox createDownHBox(){

		abbrechen.setOnAction(e-> {
			try {
				mainView.show();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		});

		weiter.setOnAction(e-> {
			try {
				inventurListView.show();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		});


		HBox rightBox=new HBox(10,abbrechen,sichern, weiter);
		// Region for spacing
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		HBox buttonBox = new HBox(10, spacer, rightBox);
		buttonBox.setAlignment(Pos.CENTER_LEFT);
		buttonBox.setAlignment(Pos.CENTER);
		return buttonBox;
	}

	private TableView<ArtikelOutput> getTableArtikelEinkauf() throws SQLException {
		artikelEinkaufTable =new TableView<>();
		ObservableList<ArtikelOutput> artikelTable = artikelController.getArtikelTable(selectedQuartalId);
		artikelEinkaufTable.setItems(artikelTable);
		artikelEinkaufTable.setPrefHeight(Utilitie.MAIN_TABLE_WIDTH);
		artikelEinkaufTable.setPrefWidth(Utilitie.MAIN_TABLE_HEIGHT);
		artikelEinkaufTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);



		TableColumn<ArtikelOutput, Integer> artikelNrCol = new TableColumn<>("Artikel Nr");
		artikelNrCol.setCellValueFactory(new PropertyValueFactory<>("artikelId"));

		TableColumn<ArtikelOutput, String> artikelNameCol = new TableColumn<>("Name");
		artikelNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<ArtikelOutput, Double> einkaufPreis = new TableColumn<>("Einkaufpreis");
		einkaufPreis.setCellValueFactory(new PropertyValueFactory<>("einkaufPreis"));

		artikelEinkaufTable.getColumns().addAll(artikelNrCol, artikelNameCol,einkaufPreis);

		return artikelEinkaufTable;

	}

	private TableView<ArtikelOutput> getTableArtikelVerkauf() throws SQLException {
		artikelVerkaufTable =new TableView<>();
		ObservableList<ArtikelOutput> artikelTable = artikelController.getArtikelTable(selectedQuartalId);
		artikelVerkaufTable.setItems(artikelTable);
		artikelVerkaufTable.setPrefHeight(Utilitie.MAIN_TABLE_WIDTH);
		artikelVerkaufTable.setPrefWidth(Utilitie.MAIN_TABLE_HEIGHT);
		artikelVerkaufTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);



		TableColumn<ArtikelOutput, Integer> artikelNrCol = new TableColumn<>("Artikel Nr");
		artikelNrCol.setCellValueFactory(new PropertyValueFactory<>("artikelId"));

		TableColumn<ArtikelOutput, String> artikelNameCol = new TableColumn<>("Name");
		artikelNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<ArtikelOutput, Double> verkaufPreis = new TableColumn<>("Verkaufpreis");
		verkaufPreis.setCellValueFactory(new PropertyValueFactory<>("verkaufPreis"));

		artikelVerkaufTable.getColumns().addAll( artikelNrCol, artikelNameCol,verkaufPreis);

		return artikelVerkaufTable;

	}

	private void initializeTableSelection() {
		// Einkauf tablosundan seçim dinle
		artikelEinkaufTable.getSelectionModel().selectedItemProperty().addListener(
				(obs, oldSelection, newSelection) -> {
					selectedArtikel.set(newSelection);
					artikelVerkaufTable.getSelectionModel().select(newSelection);
				});

		// Verkauf tablosundan seçim dinle
		artikelVerkaufTable.getSelectionModel().selectedItemProperty().addListener(
				(obs, oldSelection, newSelection) -> {
					selectedArtikel.set(newSelection);
					artikelEinkaufTable.getSelectionModel().select(newSelection);
				});

		// Bearbeiten butonunu sadece seçili öğe varsa aktif et
		bearbeiten.disableProperty().bind(selectedArtikel.isNull());
		bearbeiten2.disableProperty().bind(selectedArtikel.isNull());
	}
}
