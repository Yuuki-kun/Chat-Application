package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VideoClient2 {
    public static void main(String[] args) {
        String serverHost = "localhost"; // Server host address
        int serverPort = 1234; // Server port number
        String videoFilePath = "/Users/tongcongminh/Downloads/vde.mp4"; // Path to the video file

        try (Socket socket = new Socket(serverHost, serverPort);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             FileInputStream fis = new FileInputStream(videoFilePath)) {

            // Read the video data from the file
            byte[] videoData = fis.readAllBytes();

            // Send the video data to the server
            oos.writeObject(videoData);

            System.out.println("Video sent to the server successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
