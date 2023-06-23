package application.views;

import application.controller.ClientController;
import application.controller.OpenWindowController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ViewFactory {
	
	private AccountType loginAccountType;
	
	public ViewFactory(AccountType loginAccountType) {
		this.loginAccountType = loginAccountType;
	}
	
	public AccountType getLoginAccountType() {
		return this.loginAccountType;
	}
	
	public void setLoginAccountType(AccountType loginAccountType) {
		this.loginAccountType = loginAccountType;
	}
	
	public void showLoginWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/fxml/login.fxml"));
		Scene scene = null;
		try {
			scene = new Scene(loader.load());
		}catch(Exception e) {
			e.printStackTrace();
		}
		Stage stage = new Stage();
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);
		stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/application/resources/images/logo_small.png"))));
		stage.setResizable(false);
		stage.setTitle("Chat");
		stage.show();
		transparentStageFadeIn(stage);
	}
	
	public void showClientWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/fxml/Client.fxml"));
		ClientController clientController = new ClientController();
		loader.setController(clientController);
		createStage(loader);
	}
	
	public void showOpenWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/fxml/OpenWindow.fxml"));
//		OpenWindowController clientController = new OpenWindowController();
//		loader.setController(clientController);
		createStage(loader);
	}
	
	
	public void createStage(FXMLLoader loader) {
		Scene scene = null;
		try {
			scene = new Scene(loader.load());
		}catch(Exception e) {
			e.printStackTrace();
		}
		Stage stage = new Stage();
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);
		stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/application/resources/images/logo_small.png"))));
		stage.setResizable(false);
		stage.setTitle("Chat");
		stage.show();
	}
	
	public void transparentStageFadeout(Stage stage) {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(stage.opacityProperty(), 0);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> stage.close());
        timeline.play();
	}
	
	public void transparentStageFadeIn(Stage stage) {
		stage.setOpacity(0);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(stage.opacityProperty(), 1);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
	}
	
	public void closeStage(Stage stage) {
		stage.close();
	}
	
}
