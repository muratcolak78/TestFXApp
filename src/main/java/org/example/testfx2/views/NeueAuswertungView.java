package org.example.testfx2.views;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.testfx2.controller.NeueAuswertungController;
import org.example.testfx2.model.Quartal;
import org.example.testfx2.model.User;
import org.example.testfx2.model.enums.QuartalArt;
import org.example.testfx2.model.enums.Status;
import org.example.testfx2.repository.GetJarhlichData;
import org.example.testfx2.repository.GetUsers;

import java.util.function.Consumer;

public class NeueAuswertungView {
    private NeueAuswertungController controller=new NeueAuswertungController();
    private ComboBox<Integer> jahrBox;
    private ComboBox<QuartalArt> quartalBox;
    private ComboBox<Status> statusBox;
    private ComboBox<User> abnahmeBox;
    private TextArea kommentarArea;
    private Button sichernBtn;
    private Button abbrechenBtn;
    private Button weiterBtn;

    public void show(Consumer<Quartal> onSave) {
        Stage stage = new Stage();
        stage.setTitle("Neue Auswertung");
        VBox layout=createNewuAuswerungBox(stage,onSave);

        stage.setScene(new Scene(layout, 450, 500));
        stage.show();
    }
    private VBox createNewuAuswerungBox(Stage stage, Consumer<Quartal> onSave ){
        // Jahr
        Label jahrLabel = new Label("Jahr:");
        jahrBox = new ComboBox<>(FXCollections.observableArrayList(
                2025, 2026, 2027, 2028, 2029, 2030, 2031, 2032, 2033
        ));
        jahrBox.setPromptText("Jahr wählen");
        VBox jahrGroup = new VBox(5, jahrLabel, jahrBox);

        // Quartal
        Label quartalLabel = new Label("Quartal:");
        quartalBox = new ComboBox<>(FXCollections.observableArrayList(QuartalArt.values()));
        quartalBox.setPromptText("Quartal wählen");
        VBox quartalGroup = new VBox(5, quartalLabel, quartalBox);

        // Status
        Label statusLabel = new Label("Status:");
        statusBox = new ComboBox<>(FXCollections.observableArrayList(Status.values()));
        statusBox.setPromptText("Status wählen");
        VBox statusGroup = new VBox(5, statusLabel, statusBox);

        // Abnahme
        Label abnahmeLabel = new Label("Abnahme durch:");
        abnahmeBox = new ComboBox<>(FXCollections.observableArrayList(GetUsers.getUsers()));
        abnahmeBox.setPromptText("Benutzer");

        abnahmeBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getUserName());
            }
        });
        abnahmeBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getUserName());
            }
        });
        VBox abnahmeGroup = new VBox(5, abnahmeLabel, abnahmeBox);

        // Kommentar
        Label kommentarLabel = new Label("Kommentar:");
        kommentarArea = new TextArea();
        kommentarArea.setPrefRowCount(4);
        kommentarArea.setWrapText(true);
        kommentarArea.setPrefHeight(80);
        VBox kommentarGroup = new VBox(5, kommentarLabel, kommentarArea);

        // Butonlar
        sichernBtn = new Button("Sichern");
        abbrechenBtn = new Button("Abbrechen");
        weiterBtn = new Button("Weiter");

        BooleanBinding isFormValid = jahrBox.valueProperty().isNotNull()
                .and(quartalBox.valueProperty().isNotNull())
                .and(statusBox.valueProperty().isNotNull())
                .and(abnahmeBox.valueProperty().isNotNull())
                .and(kommentarArea.textProperty().isNotEmpty());

        sichernBtn.disableProperty().bind(isFormValid.not());

        sichernBtn.setOnAction(e -> {
            try {
                Quartal q = controller.buildQuartal(
                        jahrBox.getValue(),
                        quartalBox.getValue(),
                        statusBox.getValue(),
                        abnahmeBox.getValue(),
                        kommentarArea.getText()
                );
                controller.saveQuartal(q);
                onSave.accept(q);
                stage.close();
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Unvollständige Informationen");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });

        abbrechenBtn.setOnAction(e -> stage.close());
        weiterBtn.setOnAction(e -> System.out.println("Weiter tıklandı"));

        HBox buttonBox = new HBox(10, sichernBtn, abbrechenBtn, weiterBtn);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10,
                jahrGroup, quartalGroup, statusGroup,
                abnahmeGroup, kommentarGroup,
                buttonBox
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        return layout;
    }
}
