package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.models.ClientModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import request.FriendRequest;
import request.Request;
import request.RequestType;

public class AddFriendBoxController implements Initializable{

    @FXML
    private Button adFriendBtn;

    @FXML
    private ImageView friend_image_view;

    @FXML
    private Label friend_name_label;

    @FXML
    private Label status_lbl;
	
    private String clientName;
    private String clientId;
    private HBox clientHBox;
    
    public AddFriendBoxController(String clientId, String clientName) {
    		this.clientName = clientName;
    		this.clientId = clientId;
//    		this.clientHBox = h;
    }
   
    public void addFriend() {
    		//send friend request
    		ClientModel.getInstance().getViewFactory().getClientController().sendFriendRequest();
    		System.out.println("Friend with id = "+clientId);
    		adFriendBtn.setVisible(false);
    		status_lbl.setText("Sent");
    		
    		//send friend request with id = clientId
    		Request addf = new FriendRequest(RequestType.SEND_FRIEND_REQUEST, clientId);
    		
    		try {
				ClientModel.getInstance().getClient().getOut().writeObject(addf);
				ClientModel.getInstance().getViewFactory().getClientController().getSentRequestID().add(clientId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
	
	public void setName() {
		this.friend_name_label.setText(clientName);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		adFriendBtn.setOnAction(event -> addFriend());
	}
	
	public void disableAddButton() {
		adFriendBtn.setVisible(false);
		status_lbl.setText("Sent");

	}

}
