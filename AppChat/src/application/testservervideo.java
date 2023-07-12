package application;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;


public class testservervideo extends Application {

    private static final int SERVER_PORT = 1234;
    private VBox root;
    private MediaPlayer mediaPlayer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        root = new VBox();

        primaryStage.setTitle("Video Server");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();

        Thread serverThread = new Thread(() -> {
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

                    // Phát video sau khi nhận được từ client
                    Platform.runLater(() -> playVideo("server-video.mp4"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        serverThread.start();
    }

    private void playVideo(String videoPath) {
        Media media = new Media(new File(videoPath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        root.getChildren().add(mediaView);

        mediaPlayer.setOnReady(() -> {
            mediaPlayer.play();
            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());
        });
    }
}





