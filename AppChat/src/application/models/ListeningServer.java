package application.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import accounttype.AccountType;
import application.controller.LoginController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import request.LoginRequest;
import request.LoginSuccessfully;
import request.Request;

public class ListeningServer implements Runnable {

	private ObjectOutputStream out;
	private ObjectInputStream in;

	public ListeningServer(ObjectOutputStream out, ObjectInputStream in) {
		this.out = out;
		this.in = in;
	}

	// listening message from server and handle it
	@Override
	public void run() {
		Request rq = null;
		while (ClientModel.getInstance().getClient().getSocket().isConnected()) {
			
			try {
				rq = (Request) in.readObject();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				ClientModel.getInstance().getClient().closeEverything();
				e.printStackTrace();
				break;
			}
			
			switch (rq.getRqType()) {
			case LOGIN_SUCCESSFULLY:
				System.out.println("Da nhan doi tuong login client");

				if (((LoginSuccessfully) rq).getAccountType() == AccountType.CLIENT) {

					Platform.runLater(() ->ClientModel.getInstance().getViewFactory().showClientWindow());
					Platform.runLater(() -> ClientModel.getInstance().getViewFactory().closeLoginWindow());
				}
				break;
			default:
				break;
			}
		}
	}
	

}
