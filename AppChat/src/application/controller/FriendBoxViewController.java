package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.robot.Robot;

public class FriendBoxViewController implements Initializable{

    @FXML
    private Button chooces_btn;

    @FXML
    private HBox friend_box_hbox;

    @FXML
    private ImageView friend_imageview;

    @FXML
    private Label friend_name_label;

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
					
				});		
				chatOption.setOnAction(event ->{
					//xu ly su kien chat
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
    
}