package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TransparentStageExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Tạo một StackPane để chứa các thành phần trong sân khấu
        StackPane root = new StackPane();
        
        // Thiết lập mức độ trong suốt cho sân khấu
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setOpacity(1); // Giá trị 0.0 là hoàn toàn trong suốt, 1.0 là không trong suốt
        
        // Tạo một Scene với sân khấu và chèn nó vào StackPane
        Scene scene = new Scene(root, 400, 300);
        
        // Thiết lập màu nền trong suốt cho Scene
        scene.setFill(Color.TRANSPARENT);
        
        // Thiết lập Scene cho sân khấu
        primaryStage.setScene(scene);
        
        // Hiển thị sân khấu
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
