package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HBoxExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10));
        
        Button button = new Button("•••");
        button.setVisible(false);

        hbox.getChildren().add(button);

        hbox.setOnMouseEntered(event -> button.setVisible(true));
        hbox.setOnMouseExited(event -> button.setVisible(false));

        primaryStage.setScene(new Scene(hbox, 200, 100));
        primaryStage.show();
    }
}


