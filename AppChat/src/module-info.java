module AppChat {
	requires javafx.controls;
	requires javafx.fxml;
	requires de.jensd.fx.glyphs.fontawesome;
	requires com.jfoenix;
	
	opens application to javafx.graphics, javafx.fxml;
	opens application.controller to javafx.fxml;
}
