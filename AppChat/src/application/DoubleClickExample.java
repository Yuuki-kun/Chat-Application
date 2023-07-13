package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DoubleClickExample extends Application {

    private static final long DOUBLE_CLICK_TIME_DELTA = 300; // milliseconds
    private long lastClickTime = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Double click here");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 200, 200);

        // Lắng nghe sự kiện chuột
        label.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime <= DOUBLE_CLICK_TIME_DELTA) {
                    // Xử lý double click
                    System.out.println("Double click detected");
                }
                lastClickTime = clickTime;
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
