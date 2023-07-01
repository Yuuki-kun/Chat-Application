package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ButtonContextMenuExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button button = new Button("Click Me");

        ContextMenu contextMenu = new ContextMenu();
        MenuItem option1 = new MenuItem("Option 1");
        MenuItem option2 = new MenuItem("Option 2");
        MenuItem option3 = new MenuItem("Option 3");
        contextMenu.getItems().addAll(option1, option2, option3);

        button.setOnMouseClicked(event -> contextMenu.show(button, event.getSceneX(), event.getSceneY()));

        StackPane root = new StackPane(button);
        root.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(root, 200, 100));
        primaryStage.show();
    }
}

