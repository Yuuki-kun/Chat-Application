package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ClientSendMp3 extends Application {

    private Socket socket;
    private ObjectOutputStream outputStream;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Sender");

        Button sendButton = new Button("Send File");
        sendButton.setOnAction(event -> sendFile());

        VBox root = new VBox(10);
        root.getChildren().add(sendButton);

        primaryStage.setScene(new Scene(root, 200, 100));
        primaryStage.show();
    }

    private void sendFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                byte[] fileData = readBytesFromFile(selectedFile);

                FileData fileDataObject = new FileData(selectedFile.getName(), fileData);

                socket = new Socket("localhost", 1234); // Thay "server_ip_address" bằng địa chỉ IP của server
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(fileDataObject);
                outputStream.flush();

                System.out.println("File sent successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null)
                        outputStream.close();
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] readBytesFromFile(File file) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return outputStream.toByteArray();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
