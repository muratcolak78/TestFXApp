package org.example.testfx2;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.testfx2.utils.ViewNavigator;
import org.example.testfx2.views.MainView;

import java.io.IOException;

public class HelloApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		stage.setTitle("Login panel");
		ViewNavigator.setMainStage(stage);
		new MainView().show();

	}


}