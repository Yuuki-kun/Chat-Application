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
import javafx.scene.layout.VBox;
import request.Request;
import request.RequestType;
import request.ResponeFriendRq;

public class FriendRequestController implements Initializable{
		
    @FXML
    private Button FR_accept_btn;

    @FXML
    private Label status_lbl;
    
    @FXML
    private Button FR_refuse_btn;

    @FXML
    private ImageView friend_image_view;

    @FXML
    private Label friend_name_label;
    
    @FXML
    private VBox button_vbox;
    
	private String id;
	private String name;
	
	public FriendRequestController(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.FR_accept_btn.setOnAction(event -> acceptFR());
	}
	
	public void setName() {
		this.friend_name_label.setText(name);
	}	
	
	public void acceptFR() {
		//send accept rq with id to server
		
		try {
			if(id!=null) {
				
				//neu co thong bao ket ban moi -> xoa cac cai cu di
				
				Request acceptFR = new ResponeFriendRq(RequestType.ACCEPT_FR, id);
				ClientModel.getInstance().getClient().getOut().writeObject(acceptFR);
				this.button_vbox.setVisible(false);
				this.status_lbl.setVisible(true);
				this.status_lbl.setText("Accepted");
			}
		}catch (IOException e) {
			System.out.println("send accept rq error");
			e.printStackTrace();
		}
		
	}
	
	
}
