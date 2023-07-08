package application.controller;

import java.io.IOException;

import application.models.ClientModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import request.Request;
import request.RequestType;
import request.SeenStatus;

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
			System.out.println("Set v box cua "+this.clientname+" user id = "+this.userID);
			//neu click vao inboxview -> chuyen doi tuong can gui tin nhan sang doi tuong do
			ClientModel.getInstance().getViewFactory().getClientController().setSendToUserID(userID);
			
			//chuyen message view sang doi tuong duoc chon 
						ClientModel.getInstance().getViewFactory().getClientController().resetVBox(this.chatVBox);
						ClientModel.getInstance().getViewFactory().getClientController().setOriginVBox(chatVBox);
						
			//send request seen to server
			Request rq = new SeenStatus(RequestType.SEEN_STATUS, true, this.userID);
			try {
				ClientModel.getInstance().getClient().getOut().writeObject(rq);
			} catch (IOException e) {
				System.out.println("send 'seening' status error.");
				e.printStackTrace();
			}			
			//

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
	public String getClientname() {
		return clientname;
	}
}
