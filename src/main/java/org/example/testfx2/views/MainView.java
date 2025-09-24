package org.example.testfx2.views;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.testfx2.controller.ArtikelController;
import org.example.testfx2.controller.MainController;
import org.example.testfx2.controller.NeueAuswertungController;
import org.example.testfx2.model2.QuartalOutput;
import org.example.testfx2.service.QuartalService;
import org.example.testfx2.utils.AlertUtil;
import org.example.testfx2.utils.ModernButton;
import org.example.testfx2.utils.Utilitie;
import org.example.testfx2.utils.ViewNavigator;

import java.sql.SQLException;
public class MainView {
	private QuartalService service = new QuartalService();
	private MainController mainController = new MainController(service);
	private ArtikelController artikelController = new ArtikelController();
	private NeueAuswertungController auswertungController = new NeueAuswertungController();
	private TableView<QuartalOutput> tableView;

	private Button checkList = new ModernButton("Checklist");
	private Button neueAuswertung = new ModernButton("Neue Auswertung");
	private Button offnen = new ModernButton("Öffnen");
	private Button exportAlsExcel = new ModernButton("Export als Excel");
	private Button abnahme = new ModernButton("Abnahme");

	public MainView() throws SQLException {
	}

	public void show() throws SQLException {
		Scene scene = createScene();
		scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
		ViewNavigator.switchViews(scene, "Main");
	}

	private Scene createScene() throws SQLException {
		VBox mainBox = new VBox();
		VBox tableBox = createMainVBox();

		TableView<QuartalOutput> tableView = getTableList();
		mainBox.getChildren().addAll(tableBox, tableView);

		return new Scene(mainBox, Utilitie.APP_WIDH, Utilitie.APP_HEIGHT);
	}

	private VBox createMainVBox() {
		checkList.setOnAction(e -> mainController.openChecklistPdf());

		HBox leftBox = new HBox(checkList);
		offnen.setOnAction(e->openSelectedQuartal());
		HBox rightBox = new HBox(10, neueAuswertung, offnen, exportAlsExcel, abnahme);

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		HBox buttonBox = new HBox(10, leftBox, spacer, rightBox);
		buttonBox.setAlignment(Pos.CENTER);
		return new VBox(15, buttonBox);
	}

	private TableView<QuartalOutput> getTableList() throws SQLException {
		tableView = new TableView<>();
		ObservableList<QuartalOutput> mainTable = mainController.getMainTable();
		tableView.setItems(mainTable);
		tableView.setPrefHeight(Utilitie.MAIN_TABLE_HEIGHT);
		tableView.setPrefWidth(Utilitie.MAIN_TABLE_WIDTH);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Tablo kolonları
		TableColumn<QuartalOutput, Integer> yearCol = new TableColumn<>("Jahr");
		yearCol.setCellValueFactory(cellData ->
				new SimpleIntegerProperty(cellData.getValue().getJahr()).asObject());

		TableColumn<QuartalOutput, String> quartalCol = new TableColumn<>("Quartal");
		quartalCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getQuartal()));

		TableColumn<QuartalOutput, String> statusCol = new TableColumn<>("Status");
		statusCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getStatus()));

		TableColumn<QuartalOutput, String> userCol = new TableColumn<>("AbnahmeBei");
		userCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getAbnahmeBei()));

		TableColumn<QuartalOutput, String> commentCol = new TableColumn<>("Kommentar");
		commentCol.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getKommentar()));

		tableView.getColumns().addAll(yearCol, quartalCol, statusCol, userCol, commentCol);

		// CONTEXT MENU EKLE - BASIT VERSIYON
		setupContextMenu();

		return tableView;
	}

	// BASIT CONTEXT MENU METODU
	private void setupContextMenu() {
		// Context Menu oluştur
		ContextMenu contextMenu = new ContextMenu();

		// Sadece 2 menu item
		MenuItem offnenItem = new MenuItem("Öffnen");
		MenuItem abnahmeItem = new MenuItem("Abnahme");

		// Tıklama olayları
		offnenItem.setOnAction(e -> openSelectedQuartal());
		abnahmeItem.setOnAction(e -> abnahmeSelectedQuartal());

		// Menüye ekle
		contextMenu.getItems().addAll(offnenItem, abnahmeItem);

		// Tabloya context menu'yu bağla
		tableView.setContextMenu(contextMenu);

		// Sağ tıklamayı aktif et
		tableView.setRowFactory(tv -> {
			TableRow<QuartalOutput> row = new TableRow<>();

			row.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
					// Sağ tıklama - menüyü göster
					contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
				}
			});

			return row;
		});
	}

	// Öffnen butonu action
	private void openSelectedQuartal() {
		QuartalOutput selected = tableView.getSelectionModel().getSelectedItem();
		if (selected != null) {
			try {
				artikelController.openForm(selected.getQuartalId());
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	// Abnahme butonu action
	private void abnahmeSelectedQuartal() {
		QuartalOutput selected = tableView.getSelectionModel().getSelectedItem();
		if (selected != null) {
			AlertUtil.showWarningAlert("Warnung","Abnahme: " + selected.getJahr() + " " + selected.getQuartal());

		}
	}
}