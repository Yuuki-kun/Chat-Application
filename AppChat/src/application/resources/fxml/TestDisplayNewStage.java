package application.resources.fxml;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TestDisplayNewStage extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Tạo nút để mở cửa sổ mới
        Button openButton = new Button("Open New Window");
        openButton.setOnAction(e -> openNewWindow());

        // Đặt cấu trúc giao diện
        StackPane root = new StackPane();
        root.getChildren().add(openButton);

        // Tạo Scene và hiển thị
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Window");
        primaryStage.show();
    }

    private void openNewWindow() {
        // Tạo một cửa sổ mới với cha là cửa sổ hiện tại (primaryStage)
        Stage newStage = new Stage();
        newStage.initOwner(primaryStage);

        // Đặt cấu trúc giao diện cho cửa sổ mới
        StackPane newRoot = new StackPane();
        Scene newScene = new Scene(newRoot, 200, 150);
        newStage.setScene(newScene);
        newStage.setTitle("New Window");

        // Xử lý sự kiện khi cửa sổ mới được đóng
//        newStage.setOnCloseRequest(e -> {
//            // Cho phép thao tác trên cửa sổ gốc khi cửa sổ mới được đóng
//            primaryStage.setDisable(false);
//        });
//
//        // Vô hiệu hóa cửa sổ gốc khi cửa sổ mới được hiển thị
//        primaryStage.setDisable(true);
        	newStage.initOwner(primaryStage);
        	newStage.initModality(Modality.WINDOW_MODAL);
        // Hiển thị cửa sổ mới
        newStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
