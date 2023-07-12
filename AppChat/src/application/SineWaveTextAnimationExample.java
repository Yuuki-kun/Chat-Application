package application;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SineWaveTextAnimationExample extends Application {

    private static final int ANIMATION_DURATION = 5000;
    private static final int WAVE_AMPLITUDE = 50;
    private static final int WAVE_PERIOD = 200;
    private static final int TEXT_SIZE = 24;


 
    @Override
    public void start(Stage primaryStage) {
        Text text = new Text("Đang nhập");
        text.setFont(Font.font(TEXT_SIZE));

        StackPane root = new StackPane();
        root.getChildren().add(text);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        Path wavePath = createWavePath(text);
        PathTransition pathTransition = createPathTransition(text, wavePath, Duration.millis(WAVE_PERIOD * 2)); // Tăng duration lên gấp đôi

        SequentialTransition animation = new SequentialTransition(pathTransition);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(ANIMATION_DURATION));
        pauseTransition.setOnFinished(event -> {
            animation.stop();
            text.setText("Hoàn thành đăng nhập");
        });
        pauseTransition.play();
    }



    private Path createWavePath(Text text) {
        double startX = text.getLayoutX();
        double startY = text.getLayoutY();
        double amplitude = WAVE_AMPLITUDE;
        double period = WAVE_PERIOD;
        double height = text.getLayoutBounds().getHeight();

        Path path = new Path();
        path.getElements().add(new MoveTo(startX, startY));

        for (double x = startX; x < startX + period; x++) {
            double y = startY + amplitude * Math.sin(2 * Math.PI * (x - startX) / period);
            path.getElements().add(new CubicCurveTo(x, startY, x + 0.5, y, x + 1, startY));
        }

        return path;
    }

    private PathTransition createPathTransition(Text text, Path path, Duration duration) {
        PathTransition transition = new PathTransition(duration, path, text);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setOrientation(PathTransition.OrientationType.NONE);
        return transition;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

