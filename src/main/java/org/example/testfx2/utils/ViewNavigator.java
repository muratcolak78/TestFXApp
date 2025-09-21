package org.example.testfx2.utils;


import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewNavigator {
	private static  Stage mainStage;
	public static void setMainStage(Stage stage){
		mainStage=stage;
	}
	public static void switchViews(Scene scene, String stageName){
		if(mainStage!=null){
			mainStage.setTitle(stageName);
			mainStage.setScene(scene);
			mainStage.show();
		}
	}
}
