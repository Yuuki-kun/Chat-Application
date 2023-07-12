package application;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginNotification2 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Tạo một Label để hiển thị thông báo
        Label notificationLabel = new Label("Đang nhập...");
        notificationLabel.setStyle("-fx-background-color: #4287f5; -fx-padding: 10px;");
        notificationLabel.setTextFill(Color.WHITE);

        // Tạo một StackPane để chứa Label và căn giữa cho Label
        StackPane root = new StackPane(notificationLabel);
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        root.setPrefSize(200, 100);

        // Tạo một Scene với StackPane làm nền
        Scene scene = new Scene(root);

        // Thiết lập Stage
        primaryStage.setTitle("Thông báo đang nhập");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        // Tạo TranslateTransition để chuyển động Label
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), notificationLabel);
        translateTransition.setFromY(0);
        translateTransition.setToY(-50);
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setAutoReverse(true);
        translateTransition.setInterpolator(Interpolator.SPLINE(0.4, 0.1, 0.2, 0.9));

        // Bắt đầu chuyển động
        translateTransition.play();

        // Hiển thị Stage
        primaryStage.show();
    }
}

