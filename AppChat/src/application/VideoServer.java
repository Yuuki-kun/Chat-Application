package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class VideoServer extends Application {

    private static final int SERVER_PORT = 1234;
    private Stage primaryStage;
    private Stage videoWindow;
    private String videoPath;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Video Server");

        Button openButton = new Button("Open Video");
        openButton.setOnAction(event -> {
            // Hiển thị cửa sổ lớn khi nhấp vào nút "Open Video"
            openLargeWindow();
        });

        StackPane root = new StackPane(openButton);
        root.setAlignment(Pos.CENTER);
        primaryStage.setScene(new Scene(root, 200, 200));
        primaryStage.show();

        // Nhận video từ client
        receiveVideo();
    }

    private void openLargeWindow() {
        if (videoWindow != null && videoWindow.isShowing()) {
            videoWindow.requestFocus();
            return;
        }

        videoWindow = new Stage();
        videoWindow.setTitle("Large Window");
        Button playButton = new Button("Play");
        playButton.setOnAction(event -> {
            // Phát video khi nhấp vào nút "Play"
            playVideo();
        });

        StackPane root = new StackPane(playButton);
        root.setAlignment(Pos.CENTER);
        videoWindow.setScene(new Scene(root, 400, 400));
        videoWindow.initOwner(primaryStage);
        videoWindow.initModality(Modality.WINDOW_MODAL);
        videoWindow.show();
    }

    private void playVideo() {
        if (videoPath == null || videoPath.isEmpty()) {
            showAlert("No video selected.");
            return;
        }

        try {
            Media media = new Media(new File(videoPath).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            videoWindow.close();
        } catch (Exception e) {
            showAlert("Error playing video.");
        }
    }

    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void receiveVideo() {
        Thread receiveThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                System.out.println("Server started. Listening on port " + SERVER_PORT);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                    DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                    FileOutputStream fos = new FileOutputStream("server-video.mp4");

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    
                    while ((bytesRead = dis.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }

                    fos.close();
                    dis.close();
                    clientSocket.close();
                    System.out.println("Video received from client.");

                    Platform.runLater(() -> {
                        videoPath = "server-video.mp4";
                        openLargeWindow();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        receiveThread.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // Đảm bảo đóng cửa sổ khi ứng dụng kết thúc
        if (videoWindow != null) {
            videoWindow.close();
        }
    }
}

