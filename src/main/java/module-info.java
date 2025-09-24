module org.example.testfx2 {
	requires javafx.controls;
	requires javafx.fxml;

	requires com.dlsc.formsfx;
	requires jdk.jfr;
	requires static lombok;
	requires java.desktop;
	requires java.sql;
	requires jakarta.validation;

	opens org.example.testfx2 to javafx.fxml;
	opens org.example.testfx2.model to javafx.base;
	opens org.example.testfx2.model2 to javafx.base;

	exports org.example.testfx2;
	opens org.example.testfx2.model.enums to javafx.base;
}