package application;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class VideoServer2 {
    public static void main(String[] args) {
        int port = 1234; // Port number to listen on

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket clientSocket = serverSocket.accept();
             ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

            // Read the video object from the client
            Object videoObject = ois.readObject();

            // Assuming the video object is a byte array
            byte[] videoData = (byte[]) videoObject;

            // Write the video data to a file
            String filePath = "video.mp4";
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(videoData);
            }

            System.out.println("Video received and saved successfully!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
