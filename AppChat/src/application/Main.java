package application;
	
import application.models.ClientModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		ClientModel.getInstance().getViewFactory().showOpenWindow();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
