package application;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class testclientvideo {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        String videoPath = "/Users/tongcongminh/Downloads/yt1s.com - SHORT MUSIC SOLO RECORD.mp4";
        sendVideo(videoPath);
    }

    private static void sendVideo(String videoPath) {
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
        }
    }
}