package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.models.ClientModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ClientMenuController implements Initializable{
    @FXML
    private JFXButton group_btn;

    @FXML
    private JFXButton message_btn;

    
    public void show() {
    		ClientModel.getInstance().getViewFactory().sh();
    }
    public void hide() {
		ClientModel.getInstance().getViewFactory().hi();

    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		message_btn.setOnAction(event -> show());
		group_btn.setOnAction(event -> hide());
	}
    
}
