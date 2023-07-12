package application;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientVideo extends Application {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Video Client");

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Video files (*.mp4)", "*.mp4");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Select Video");

        // Chọn video từ máy tính
        Stage fileStage = new Stage();
        fileStage.initOwner(primaryStage);
        fileStage.setTitle("File Selection");

        File selectedFile = fileChooser.showOpenDialog(fileStage);
        if (selectedFile != null) {
            sendVideo(selectedFile.getPath());
        } else {
            showAlert("No file selected.");
        }
    }

    private void sendVideo(String videoPath) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            FileInputStream fis = new FileInputStream(videoPath);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }

            fis.close();
            dos.close();
            socket.close();
            System.out.println("Video sent to server.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error sending video.");
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
}
