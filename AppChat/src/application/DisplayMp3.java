package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class DisplayMp3 extends Application {

    private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MP3 Player");

        Button openButton = new Button("Open MP3");
        openButton.setOnAction(event -> openMP3File());

        VBox root = new VBox(10);
        root.getChildren().add(openButton);

        primaryStage.setScene(new Scene(root, 200, 100));
        primaryStage.show();
    }

    private void openMP3File() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open MP3 File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Media media = new Media(selectedFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            Stage playerStage = new Stage();
            playerStage.setTitle("MP3 Player");
            playerStage.setScene(new Scene(new PlayerPane(mediaPlayer), 400, 100));
            playerStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class PlayerPane extends javafx.scene.layout.BorderPane {
        public PlayerPane(MediaPlayer mediaPlayer) {
            MediaControl mediaControl = new MediaControl(mediaPlayer);
            setCenter(mediaControl);
        }
    }

    private static class MediaControl extends javafx.scene.layout.HBox {
        public MediaControl(MediaPlayer mediaPlayer) {
            Button playButton = new Button("Play");
            playButton.setOnAction(event -> mediaPlayer.play());

            Button pauseButton = new Button("Pause");
            pauseButton.setOnAction(event -> mediaPlayer.pause());

            Button stopButton = new Button("Stop");
            stopButton.setOnAction(event -> mediaPlayer.stop());

            getChildren().addAll(playButton, pauseButton, stopButton);
        }
    }
}
