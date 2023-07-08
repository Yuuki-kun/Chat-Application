package application;
	
import application.models.ClientModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		ClientModel.getInstance().getViewFactory().showOpenWindow();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
