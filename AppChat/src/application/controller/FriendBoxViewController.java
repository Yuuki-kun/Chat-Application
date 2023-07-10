package application.controller;

import java.net.Authenticator.RequestorType;
import java.net.URL;
import java.util.ResourceBundle;

import application.models.ClientModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import request.Message;
import request.Request;
import request.RequestType;

public class FriendBoxViewController implements Initializable{

    @FXML
    private Button chooces_btn;

    @FXML
    private HBox friend_box_hbox;

    @FXML
    private ImageView friend_imageview;

    @FXML
    private Label friend_name_label;
    
    @FXML
    private Circle online_status_shape;
    
    @FXML
    private TextField search_friend_tf;
    
    
    private String UserID;
    
    private boolean isOnline = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.chooces_btn.setVisible(false);
		this.friend_box_hbox.setOnMouseEntered(event -> this.chooces_btn.setVisible(true));
		this.friend_box_hbox.setOnMouseExited(event -> this.chooces_btn.setVisible(false));
		
		setContextMenu();
		
	}
    
	public void setContextMenu() {
		//create context menu
		
				ContextMenu contextMenu = new ContextMenu();
		        MenuItem chatOption = new MenuItem("Chat");
		        SeparatorMenuItem separator = new SeparatorMenuItem();

		        MenuItem option2 = new MenuItem("Option 2");
		        MenuItem option3 = new MenuItem("Option 3");
				contextMenu.getItems().addAll(chatOption, separator, option2, option3);
				
				contextMenu.setOnHidden(event ->{
					friend_imageview.requestFocus();
				});	
				
				chatOption.setOnAction(event ->{
					//Open chat view
					System.out.println("Option = "+UserID+"; "+friend_name_label.getText());
					ClientModel.getInstance().getViewFactory().getClientController().showMessageView();
					ClientModel.getInstance().getViewFactory().getClientController().setSendToUserID(UserID);
					ClientModel.getInstance().getViewFactory().getClientController().setSendToName(friend_name_label.getText());
					
				});
				
				option2.setOnAction(event ->{
					System.out.println("context menu: "+option2.getText());
				});
				
				option3.setOnAction(event ->{
					System.out.println("context menu: "+option3.getText());
				});
				
		        contextMenu.setStyle("-fx-text-fill: #333333;"
		        		+ "-fx-pref-width:150;"
		        		+ "-fx-pref-height:100;"
		        		+ "-fx-background-color:linear-gradient(to top right, #f2e6ff, #d1d1fa);"
		        		+ "-fx-background-radius: 10;"
		        		);
		        
				
				this.chooces_btn.setOnMouseClicked(event -> contextMenu.show(chooces_btn, event.getScreenX(), event.getScreenY()));
	}
	
	public void setUserID(String userID) {
		UserID = userID;
	}
    
	public String getUserID() {
		return UserID;
	}
	
	public void setIsOnline(boolean h) {
		this.isOnline = h;
		this.online_status_shape.setVisible(true);
		if(h) {
			this.online_status_shape.setFill(Color.DARKGREEN);
		}else {
			this.online_status_shape.setFill(Color.DARKRED);
		}
	}
	
	public boolean getIsOnline() {
		return this.isOnline;
	}
	
}
