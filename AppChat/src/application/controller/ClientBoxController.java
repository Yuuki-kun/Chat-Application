package application.controller;

import application.models.ClientModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ClientBoxController {
	private String clientname;
	private String userID;
	private HBox friend_box_hbox = null;
	private VBox chatVBox;
	public ClientBoxController(String clientname, String userID, HBox h, VBox v) {
		this.clientname = clientname;
		this.userID = userID;
		this.friend_box_hbox = h;
		this.chatVBox = v;
		

		
		this.friend_box_hbox.setOnMouseClicked(event ->{
			System.out.println("Set v box cua "+clientname);
						ClientModel.getInstance().getViewFactory().getClientController().resetVBox(this.chatVBox);
		});
	}
	
	public String getUserID() {
		return userID;
	}
	
	public HBox getFriend_box_hbox() {
		return friend_box_hbox;
	}
	
	public VBox getChatVBox() {
		return chatVBox;
	}
}
