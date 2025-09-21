package org.example.testfx2.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.testfx2.controller.ArtikelController;
import org.example.testfx2.controller.MainController;
import org.example.testfx2.controller.NeueAuswertungController;
import org.example.testfx2.model.Quartal;
import org.example.testfx2.model.enums.QuartalArt;
import org.example.testfx2.model.enums.Status;
import org.example.testfx2.repository.QurtalDataRepo;
import org.example.testfx2.service.QuartalService;
import org.example.testfx2.utils.Utilitie;
import org.example.testfx2.utils.ViewNavigator;

import java.sql.SQLException;

public class MainView {
	private QuartalService service=new QuartalService();
	private MainController mainController =new MainController(service);
	private ArtikelController artikelController=new ArtikelController();

	private NeueAuswertungController auswertungController = new NeueAuswertungController();
	private TableView<Quartal> tableView;


	private Button checkList=new Button("Checklist");
	private Button neueAuswertung=new Button("Neue Auswertung");
	private Button offnen=new Button("Öffnen");
	private Button exportAlsExcel=new Button("Export als Excel");
	private Button abnahme=new Button("Abnahme");

	public MainView() throws SQLException {
	}


	public void show() throws SQLException {
		Scene scene=createScene();
		scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
		ViewNavigator.switchViews(scene);
	}
	private Scene createScene() throws SQLException {
		VBox mainBox=new VBox();
		VBox tableBox=createMainVBox();
		TableView<Quartal> tableView=getTableList();
		mainBox.getChildren().addAll(tableBox,tableView);
		return  new Scene(mainBox, Utilitie.APP_WIDH,Utilitie.APP_HEIGHT);
	}

	private VBox createMainVBox() {
		String [] buttonStyle={"text-size-sm","bg-light-blue", "text-white", "text-weight-100", "rounded-border"};
		checkList.getStyleClass().addAll(buttonStyle);
		checkList.setOnAction(e-> mainController.openChecklistPdf());

		neueAuswertung.getStyleClass().addAll(buttonStyle);
		neueAuswertung.setOnAction(e -> {
			try {
				auswertungController.openForm();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		});


		offnen.getStyleClass().addAll(buttonStyle);
		offnen.setOnAction(e-> {
			try {
				artikelController.openForm();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		});

		exportAlsExcel.getStyleClass().addAll(buttonStyle);
		abnahme.getStyleClass().addAll(buttonStyle);

		HBox leftBox=new HBox(checkList);
		HBox rightBox=new HBox(10,neueAuswertung,offnen,exportAlsExcel,abnahme);

		// Region for spacing
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		// Ana button call
		HBox buttonBox = new HBox(10, leftBox, spacer, rightBox);
		buttonBox.setAlignment(Pos.CENTER_LEFT);
		buttonBox.setAlignment(Pos.CENTER);
		return new VBox(15, buttonBox);
	}
	private TableView<Quartal> getTableList() throws SQLException {
		tableView=new TableView<>();
		tableView.setItems(QurtalDataRepo.getMainTable());
		tableView.setPrefHeight(Utilitie.MAIN_TABLE_WIDTH);
		tableView.setPrefWidth(Utilitie.MAIN_TABLE_HEIGHT);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


		TableColumn<Quartal, Integer> idCol = new TableColumn<>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Quartal, Integer> yearCol = new TableColumn<>("Jahr");
		yearCol.setCellValueFactory(new PropertyValueFactory<>("jahr"));

		TableColumn<Quartal, QuartalArt> quartalCol = new TableColumn<>("Quartal");
		quartalCol.setCellValueFactory(new PropertyValueFactory<>("quartal"));

		TableColumn<Quartal, Status> statusCol = new TableColumn<>("Status");
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

		TableColumn<Quartal, String> userCol = new TableColumn<>("AbnahmeBei");
		userCol.setCellValueFactory(new PropertyValueFactory<>("abnaheBei"));

		TableColumn<Quartal, String> commentCol = new TableColumn<>("Kommentar");
		commentCol.setCellValueFactory(new PropertyValueFactory<>("kommentar"));


		tableView.getColumns().addAll(idCol, yearCol, quartalCol,statusCol, userCol, commentCol);
		return tableView;

	}


}
