package application;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TestBGGD extends Application {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Tạo hình chữ nhật làm background
        Rectangle background = new Rectangle(WIDTH, HEIGHT);

        // Tạo một timeline để thay đổi màu sắc dần dần
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    // Màu sắc hiện tại
                    Color currentColor = (Color) background.getFill();

                    // Tạo một màu sắc ngẫu nhiên cho màu tiếp theo
                    Color nextColor = generateRandomColor();

                    // Thiết lập gradient linear từ màu hiện tại đến màu tiếp theo
                    background.setFill(Color.rgb(
                            (int) (currentColor.getRed() * 255),
                            (int) (currentColor.getGreen() * 255),
                            (int) (currentColor.getBlue() * 255),
                            1.0));
                    background.setStyle("-fx-fill: linear-gradient(to right, " +
                            toHex(currentColor) + " 0%, " +
                            toHex(nextColor) + " 100%);");
                }),
                new KeyFrame(Duration.seconds(3))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        root.getChildren().add(background);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Tạo một màu ngẫu nhiên với độ trong suốt thấp
    private Color generateRandomColor() {
        double red = Math.random();
        double green = Math.random();
        double blue = Math.random();
        double opacity = Math.random() * 0.5 + 0.1; // Độ trong suốt từ 0.1 đến 0.6
        return new Color(red, green, blue, opacity);
    }

    // Chuyển đổi màu sang dạng hex
    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
