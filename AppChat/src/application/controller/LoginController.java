package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.models.Model;
import application.views.AccountType;
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
    private JFXButton login_btn;

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
		Model.getInstance().connectSQL("TTTTT", "sa", "reallyStrongPwd123");
		Stage stage = (Stage) login_btn.getScene().getWindow();
		Model.getInstance().evaluateLoginType(username_tf.getText(), password_tf.getText());
		
		if (Model.getInstance().getLoginSuccessfully()) {
			if (Model.getInstance().getLoginAccoutType() == AccountType.CLIENT) {
				Model.getInstance().getViewFactory().closeStage(stage);
				Model.getInstance().getViewFactory().showClientWindow();
				System.out.println("SHOW CLIENT WINDOW");
			} else {
				System.out.println("ADMIN LOGIN.");
			}
		}else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Login failed");
	        alert.setContentText("Username or password does not exit.\nPlease try again.");
	        alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/resources/styles/LoginSide.css").toExternalForm());
	        alert.showAndWait();
		}
	}
	
}
