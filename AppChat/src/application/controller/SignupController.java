package application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;

import application.models.ClientModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import request.Request;
import request.*;

public class SignupController implements Initializable{

	@FXML
	private TextField UserName;

	@FXML
	private PasswordField Password;

	@FXML
	private TextField Name;

	@FXML
	private TextField City;

	@FXML
	private TextField District;

	@FXML
	private TextField District2;

	@FXML
	private TextField Street;

	@FXML
	private JFXButton SignUp;

	
	
	public void onSignUp() {
		String uname = UserName.getText();
		String psw = Password.getText();
		String name = Name.getText();
		String city = City.getText();
		String district = District.getText();
		String district2 = District2.getText();
		String street = Street.getText();
		String client = "client";
		
		Request rq = new SignUp(RequestType.SIGN_UP,uname,psw,name,city,district,district2,street,client);
		
		try {
			ClientModel.getInstance().getClient().getOut().writeObject(rq);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		SignUp.setOnAction(event -> onSignUp());
	}
}
