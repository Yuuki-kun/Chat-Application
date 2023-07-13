package application;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerReceiveMp3 {

    private ServerSocket serverSocket;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started. Listening on port " + port + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClientHandler extends Thread {

        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())) {
                FileData fileData = (FileData) inputStream.readObject();

                saveFile(fileData.getFileName(), fileData.getFileData());

                System.out.println("File received and saved successfully.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void saveFile(String fileName, byte[] fileData) throws IOException {
            try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
                outputStream.write(fileData);
            }
        }
    }

    public static void main(String[] args) {
        ServerReceiveMp3 server = new ServerReceiveMp3();
        server.start(1234); // Chọn một cổng khác nếu cổng 1234 đã được sử dụng
    }
}

