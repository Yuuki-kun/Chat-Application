package application;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SnakeTextAnimationExample extends Application {

    private static final int ANIMATION_DURATION = 5000;
    private static final int DISTANCE = 200;

    @Override
    public void start(Stage primaryStage) {
        Text text = new Text("Đang nhập");
        text.setStyle("-fx-font-size: 24px;");

        StackPane root = new StackPane();
        root.getChildren().add(text);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        TranslateTransition moveUp = createTranslateTransition(text, Duration.seconds(1), 0, -DISTANCE);
        TranslateTransition moveDown = createTranslateTransition(text, Duration.seconds(1), 0, DISTANCE);
        TranslateTransition moveBack = createTranslateTransition(text, Duration.seconds(1), 0, 0);

        SequentialTransition animation = new SequentialTransition(moveUp, moveDown, moveBack);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(ANIMATION_DURATION));
        pauseTransition.setOnFinished(event -> {
            animation.stop();
            text.setText("Hoàn thành đăng nhập");
        });
        pauseTransition.play();
    }

    private TranslateTransition createTranslateTransition(Text text, Duration duration, double x, double y) {
        TranslateTransition transition = new TranslateTransition(duration, text);
        transition.setToX(x);
        transition.setToY(y);
        transition.setInterpolator(Interpolator.LINEAR);
        return transition;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
