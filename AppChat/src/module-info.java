module AppChat {
	requires javafx.controls;
	requires javafx.fxml;
	requires de.jensd.fx.glyphs.fontawesome;
	requires com.jfoenix;
	requires java.management;
	requires java.sql;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
	opens application.controller to javafx.graphics,javafx.fxml;
	
    exports server;
    exports server.controller to  javafx.fxml;
    opens server.controller to javafx.fxml;
}
