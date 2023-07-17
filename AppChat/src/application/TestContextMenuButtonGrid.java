package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class TestContextMenuButtonGrid extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button openButton = new Button("Open Matrix Context Menu");
        openButton.setOnAction(e -> showContextMenu(openButton));

        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.add(openButton, 0, 0);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showContextMenu(Button openButton) {
        Popup contextMenu = new Popup();

        GridPane menuGrid = new GridPane();
        menuGrid.setPadding(new Insets(10));
        menuGrid.setHgap(5);
        menuGrid.setVgap(5);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button("Button " + (row * 3 + col + 1));
                menuGrid.add(button, col, row);
            }
        }

        contextMenu.getContent().add(menuGrid);

        contextMenu.show(openButton.getScene().getWindow(),
                openButton.getScene().getWindow().getX() + openButton.localToScene(0, 0).getX() ,
                openButton.getScene().getWindow().getY() + openButton.localToScene(0, 0).getY()-openButton.getHeight()*2);

        contextMenu.setAutoHide(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

