package org.example.testfx2.utils;

import javafx.scene.control.Alert;

public class ExceptionUtil {
	public static void showErrorAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}