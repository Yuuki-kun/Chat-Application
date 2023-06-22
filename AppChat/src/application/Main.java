package application;
	
import application.models.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
//		try {
//			Parent root = FXMLLoader.load(getClass().getResource("resources/fxml/Login.fxml"));
//			Scene scene = new Scene(root);
//			primaryStage.setScene(scene);
//			primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/application/resources/images/logo_small.png"))));
//			primaryStage.setResizable(false);
//			primaryStage.setTitle("Chat");
//			primaryStage.show();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		Model.getInstance().getViewFactory().showLoginWindow();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
