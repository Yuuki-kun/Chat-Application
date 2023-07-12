module AppChat {
	requires javafx.controls;
	requires javafx.fxml;
	requires de.jensd.fx.glyphs.fontawesome;
	requires com.jfoenix;
	requires java.management;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.media;
	
	opens application to javafx.graphics, javafx.fxml;
	opens application.controller to javafx.graphics,javafx.fxml;
	
    exports server;
    exports server.controller to  javafx.fxml;
    
    exports application.resources.fxml to  javafx.graphics, javafx.fxml; 
    opens application.resources.fxml to  javafx.graphics, javafx.fxml; 
    
    opens server.controller to javafx.fxml;
}
