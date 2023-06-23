module AppChat {
	requires javafx.controls;
	requires javafx.fxml;
	requires de.jensd.fx.glyphs.fontawesome;
	requires com.jfoenix;
	requires java.management;
	opens application to javafx.graphics, javafx.fxml;
	opens application.controller to javafx.graphics,javafx.fxml;
}
