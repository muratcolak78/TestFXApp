package org.example.testfx2.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertUtil {
	private static final String ALERT_STYLES = "/alert.css";
	// Erfolgswarnungen
	public static void showSuccessAlert(String title, String message) {
		showAlert(Alert.AlertType.INFORMATION, title, null, message);
	}

	// Fehlerswarnungen
	public static void showErrorAlert(String title, String message) {
		showAlert(Alert.AlertType.ERROR, title, null, message);
	}

	// Informationsalarm
	public static void showInfoAlert(String title, String message) {
		showAlert(Alert.AlertType.INFORMATION, title, null, message);
	}

	// Warnmeldungen
	public static void showWarningAlert(String title, String message) {
		showAlert(Alert.AlertType.WARNING, title, null, message);
	}

	// Allgemeine Warnmethode
	private static void showAlert(Alert.AlertType alertType, String title, String header, String content) {
		Platform.runLater(() -> {
			Alert alert = new Alert(alertType);
			alert.setTitle(title);
			alert.setHeaderText(header);
			alert.setContentText(content);

			// Symbolstil festlegen (optional)
			if (alertType == Alert.AlertType.INFORMATION) {
				alert.getDialogPane().getStyleClass().add("success-alert");
			} else if (alertType == Alert.AlertType.ERROR) {
				alert.getDialogPane().getStyleClass().add("error-alert");
			}

			alert.showAndWait();
		});
	}

	// Akzeptation
	public static boolean showConfirmationDialog(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		Optional<ButtonType> result = alert.showAndWait();
		return result.isPresent() && result.get() == ButtonType.OK;
	}
}
