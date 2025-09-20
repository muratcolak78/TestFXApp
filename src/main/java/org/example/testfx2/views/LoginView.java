package org.example.testfx2.views;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.testfx2.utils.Utilitie;
import org.example.testfx2.utils.ViewNavigator;

public class LoginView {
	private Label login=new Label("Login");
	private TextField username=new TextField();
	private PasswordField passwordField=new PasswordField();
	private Button loginButton=new Button("Login");
	private Label signupLabel=new Label("Don't have an account? Click Here");

	public void show(){
		Scene scene=createScene();
		scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
		ViewNavigator.switchViews(scene);
	}

	private Scene createScene() {
		VBox mainContainerBox=new VBox();
		mainContainerBox.setAlignment(Pos.TOP_CENTER);
		mainContainerBox.getStyleClass().addAll("main-background");
		VBox loginFormBox=createLoginVbox();
		login.getStyleClass().addAll("header", "text-white");
		mainContainerBox.getChildren().addAll(login, loginFormBox);

		return new Scene(mainContainerBox, Utilitie.APP_WIDH,Utilitie.APP_HEIGHT);
	}
	private VBox createLoginVbox(){
		VBox loginFormVBox=new VBox(25);
		loginFormVBox.setAlignment(Pos.CENTER);
		String [] fieldStyle={"field-background", "text-light-gray","text-size-lg","rounded-border"};
		username.getStyleClass().addAll(fieldStyle);
		username.setMaxWidth(400);
		username.setPromptText("Enter Username");

		passwordField.getStyleClass().addAll(fieldStyle);
		passwordField.setMaxWidth(400);
		passwordField.setPromptText("Enter Password");

		loginButton.getStyleClass().addAll("text-size-lg","bg-light-blue", "text-white", "text-weight-700", "rounded-border");
		loginButton.setMaxWidth(400);
		signupLabel.getStyleClass().addAll("text-size-md", "text-light-gray", "text-underline", "link-text");

		loginFormVBox.getChildren().addAll(username,passwordField,loginButton,signupLabel );
		return loginFormVBox;
	}

}
