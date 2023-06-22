package application.views;

import application.controller.ClientController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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
		createStage(loader);
	}
	
	public void showClientWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/fxml/Client.fxml"));
		ClientController clientController = new ClientController();
		loader.setController(clientController);
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
		stage.setScene(scene);
		stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/application/resources/images/logo_small.png"))));
		stage.setResizable(false);
		stage.setTitle("Chat");
		stage.show();
	}
	
	public void closeStage(Stage stage) {
		stage.close();
	}
	
}
