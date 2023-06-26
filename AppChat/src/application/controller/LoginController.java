package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import accounttype.AccountType;
import application.models.ClientModel;
import application.models.ListeningServer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController implements Initializable{

    @FXML
    private Text forgot_pwd;

    @FXML
    private  JFXButton login_btn;

    @FXML
    private PasswordField password_tf;

    @FXML
    private Text signup;

    @FXML
    private TextField username_tf;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		login_btn.setOnAction(event -> onLogin());

	}
	
	public void onLogin() {
		ClientModel.getInstance().sendLoginRequest(username_tf.getText(), password_tf.getText());
	}
	
	public void closeLoginWindow() {
		Stage stage = (Stage) login_btn.getScene().getWindow();
		ClientModel.getInstance().getViewFactory().closeStage(stage);
	}
	
}
