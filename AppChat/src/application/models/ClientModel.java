package application.models;

import java.io.IOException;

import accounttype.AccountType;
import application.controller.FriendInboxController;
import application.views.ViewFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import request.LoginRequest;
import request.Request;
import request.RequestType;

public class ClientModel {
	
	private static ClientModel model;
	private final ViewFactory viewFactory;
	private AccountType logginAccountType = AccountType.CLIENT;
	private boolean loginSuccessfully = false;
	private Client client;
	
	private ClientModel() {
		this.viewFactory = new ViewFactory(logginAccountType);
		this.client = new Client("localhost", 1234);
	}
	
	public static synchronized ClientModel getInstance() {
		if(model==null) {
			model = new ClientModel();
		}
		return model;
	}
	public ViewFactory getViewFactory() {
		return viewFactory;
	}


	public void connectServer(String servername, int PORT) {
		if(!servername.equals("") && servername!=null)
			client = new Client(servername, PORT);
	}
	
	public void sendLoginRequest(String username, String password) {
		Request rq = new LoginRequest(RequestType.LOGIN, username, password);
		rq.sendRequest();
	}
	
	public Client getClient() {
		return client;
	}
	
	public AccountType getLoginAccoutType() {
		return this.logginAccountType;
	}
	
	public boolean getLoginSuccessfully() {
		return this.loginSuccessfully;
	}
	

}
