package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FriendInboxController implements Initializable {
	@FXML
	private VBox inbox_vbox;

	@FXML
	private Button addhbox;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//		HBox h;
//		try {
//			h = FXMLLoader.load(getClass().getResource("/test/guiobjectjson/testHBox.fxml"));
//			Platform.runLater(()-> inbox_vbox.getChildren().add(h));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	public void addChat(HBox hbox) {
//		HBox h = null;
//		try {
//			h = FXMLLoader.load(getClass().getResource("/test/guiobjectjson/testHBox.fxml"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println("HB: "+hbox);
		
		Platform.runLater(()->inbox_vbox.getChildren().add(hbox));
	}
	
	public void addhb() {

		System.out.println("KICH HOAT");
		Label l = new Label("AALALALALALLA");
		inbox_vbox.getChildren().add(l);
		
		inbox_vbox.requestLayout();
		// hoặc
		inbox_vbox.applyCss();
		
		System.out.println(inbox_vbox);
		
	}

	public Button getAddhbox() {
		return addhbox;
	}

}
