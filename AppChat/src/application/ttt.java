package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class ttt extends Application {
    private static final Duration COLOR_CHANGE_DURATION = Duration.seconds(0.1);
    private static final String[] COLORS = {
    		"#7269b5",
    		"#7066bd",
            "#695ccc",
            "#6456d1",
            "#5c4cd9",
            "#4e3dd9",
            "#4431de",
            "#3822e6",
            "#2e15ed",
            "#1d00fa",
    };

    private int colorIndex = 0;

    @Override
    public void start(Stage primaryStage) {
        Button button = new Button("");
        button.setMinWidth(300);
        button.setMinHeight(300);

        button.setStyle("-fx-background-color: linear-gradient(to bottom," + (COLORS[colorIndex])+", #000000);");

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    // Thay đổi màu nền
                    colorIndex = (colorIndex + 1) % COLORS.length;
                    button.setStyle("-fx-background-color: linear-gradient(to bottom," + (COLORS[colorIndex])+", #000000);");
                }),
                new KeyFrame(COLOR_CHANGE_DURATION)
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        StackPane root = new StackPane(button);
        Scene scene = new Scene(root, 500, 500);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Chuyển đổi màu sang chuỗi hex
    private String getColorStyle(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
