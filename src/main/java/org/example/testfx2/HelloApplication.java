package org.example.testfx2;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.testfx2.db.Database;
import org.example.testfx2.utils.ViewNavigator;
import org.example.testfx2.views.MainView;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException, SQLException {
		Database.connect();
		stage.setTitle("Login panel");
		ViewNavigator.setMainStage(stage);
		new MainView().show();

	}


}