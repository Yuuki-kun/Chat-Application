package application;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginNotification3 extends Application {

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

        // Tạo TranslateTransition để chuyển động Label từ trái qua phải
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), notificationLabel);
        translateTransition.setFromX(-root.getWidth());
        translateTransition.setToX(root.getWidth());
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setAutoReverse(true);
        translateTransition.setInterpolator(Interpolator.SPLINE(0.4, 0.1, 0.2, 0.9));

        // Tạo ScaleTransition để làm cho Label "uống lượn"
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), notificationLabel);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(2);
        scaleTransition.setCycleCount(TranslateTransition.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setInterpolator(Interpolator.SPLINE(0.4, 0.1, 0.2, 0.9));

        // Tạo ParallelTransition để kết hợp TranslateTransition và ScaleTransition
        ParallelTransition parallelTransition = new ParallelTransition(translateTransition, scaleTransition);

        // Bắt đầu chuyển động
        parallelTransition.play();

        // Hiển thị Stage
        primaryStage.show();
    }
}

