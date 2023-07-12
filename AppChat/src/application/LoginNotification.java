package application;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginNotification extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Tạo một Label để hiển thị thông báo
        Label notificationLabel = new Label("Đang nhập...");
        notificationLabel.setStyle("-fx-background-color: #4287f5; -fx-padding: 10px;");
        notificationLabel.setTextFill(Color.WHITE);
        notificationLabel.setAlignment(Pos.CENTER);

        // Tạo một StackPane để chứa Label và cung cấp căn giữa cho Label
        StackPane root = new StackPane(notificationLabel);
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        root.setPrefSize(200, 100);

        // Tạo một Scene với StackPane làm nền
        Scene scene = new Scene(root);

        // Thiết lập Stage
        primaryStage.setTitle("Thông báo đang nhập");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        // Thiết lập animation để hiển thị thông báo trong 3 giây và sau đó đóng ứng dụng
        Timeline timeline = new Timeline();
        KeyFrame showFrame = new KeyFrame(Duration.ZERO,
                new KeyValue(root.opacityProperty(), 0),
                new KeyValue(root.scaleXProperty(), 0.8),
                new KeyValue(root.scaleYProperty(), 0.8)
        );
        KeyFrame fadeInFrame = new KeyFrame(Duration.seconds(0.5),
                new KeyValue(root.opacityProperty(), 1),
                new KeyValue(root.scaleXProperty(), 1.1),
                new KeyValue(root.scaleYProperty(), 1.1)
        );
        KeyFrame fadeOutFrame = new KeyFrame(Duration.seconds(2.5),
                new KeyValue(root.opacityProperty(), 0),
                new KeyValue(root.scaleXProperty(), 0.8),
                new KeyValue(root.scaleYProperty(), 0.8)
        );
        KeyFrame closeFrame = new KeyFrame(Duration.seconds(3),
                event -> primaryStage.close()
        );
        timeline.getKeyFrames().addAll(showFrame, fadeInFrame, fadeOutFrame, closeFrame);
        timeline.setDelay(Duration.seconds(1));
        timeline.setCycleCount(1);

        // Hiển thị Stage và bắt đầu animation
        primaryStage.show();
        timeline.play();
    }
}

