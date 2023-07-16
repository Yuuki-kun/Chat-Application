package application.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import server.model.ServerModel;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private Text textName;

    @FXML
    private Text textTinh;

    @FXML
    private Text textQuan;

    @FXML
    private Text textXa;

    @FXML
    private Text textDuong;

    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;
    
    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldTinh;

    @FXML
    private TextField textFieldQuan;

    @FXML
    private TextField textFieldXa;

    @FXML
    private TextField textFieldDuong;

    private boolean isEditMode = false;
    
    private String userId;
    
    public ProfileController(String userId) {
		this.userId = userId;
	}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      
        loadUserInfo(userId);
        editButton.setOnAction(event -> handleEditButton(event));
        saveButton.setOnAction(event -> handleSaveButton(event));
    }


    private void loadUserInfo(String userID) {
        try {
            Statement statement = ServerModel.getInstance().getDatadriver().getConn().createStatement();

            // Query to retrieve user information
            String query = "SELECT name FROM users WHERE userID = '" + userID + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                textName.setText(name);
            }
            resultSet.close();

            // Query to retrieve tinh information
            query = "SELECT tentinh FROM tinh WHERE userID = '" + userID + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String tinh = resultSet.getString("tentinh");
                textTinh.setText(tinh);
            }
            resultSet.close();

            // Query to retrieve quan information
            query = "SELECT tenhuyen FROM huyen WHERE userID = '" + userID + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String quan = resultSet.getString("tenhuyen");
                textQuan.setText(quan);
            }
            resultSet.close();

            // Query to retrieve xa information
            query = "SELECT tenxa FROM xa WHERE userID = '" + userID + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String xa = resultSet.getString("tenxa");
                textXa.setText(xa);
            }
            resultSet.close();

            // Query to retrieve duong information
            query = "SELECT tenduong FROM duong_pho WHERE userID = '" + userID + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String duong = resultSet.getString("tenduong");
                textDuong.setText(duong);
            }
            resultSet.close();

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleEditButton(ActionEvent event) {
        isEditMode = true;
        toggleEditMode();
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (isEditMode) {
        	
           
            saveUserInfo(userId);
            isEditMode = false;
            toggleEditMode();
        }
    }



    private void toggleEditMode() {
    	textFieldName.setVisible(isEditMode);
        textFieldTinh.setVisible(isEditMode);
        textFieldQuan.setVisible(isEditMode);
        textFieldXa.setVisible(isEditMode);
        textFieldDuong.setVisible(isEditMode);
        textTinh.setVisible(!isEditMode);
        textQuan.setVisible(!isEditMode);
        textXa.setVisible(!isEditMode);
        textDuong.setVisible(!isEditMode);
        editButton.setDisable(isEditMode);
        saveButton.setDisable(!isEditMode);
        if (isEditMode) {
        	textFieldName.setText(textName.getText());
            textFieldTinh.setText(textTinh.getText());
            textFieldQuan.setText(textQuan.getText());
            textFieldXa.setText(textXa.getText());
            textFieldDuong.setText(textDuong.getText());
        }
    }

    private void saveUserInfo(String userID) {
        try {
            // Update name information
            String nameValue = textFieldName.getText();
            if (!nameValue.equals(textName.getText())) {
                String updateNameQuery = "UPDATE users SET name = ? WHERE userID = ?";
                PreparedStatement updateNameStatement = ServerModel.getInstance().getDatadriver().getConn().prepareStatement(updateNameQuery);
                updateNameStatement.setString(1, nameValue);
                updateNameStatement.setString(2, userID);
                updateNameStatement.executeUpdate();
                updateNameStatement.close();
            }

            // Update tinh information
            String tinhValue = textFieldTinh.getText();
            if (!tinhValue.equals(textTinh.getText())) {
                String updateTinhQuery = "UPDATE tinh SET tentinh = ? WHERE userID = ?";
                PreparedStatement updateTinhStatement = ServerModel.getInstance().getDatadriver().getConn().prepareStatement(updateTinhQuery);
                updateTinhStatement.setString(1, tinhValue);
                updateTinhStatement.setString(2, userID);
                updateTinhStatement.executeUpdate();
                updateTinhStatement.close();
            }

            // Update quan information
            String quanValue = textFieldQuan.getText();
            if (!quanValue.equals(textQuan.getText())) {
                String updateQuanQuery = "UPDATE huyen SET tenhuyen = ? WHERE userID = ?";
                PreparedStatement updateQuanStatement = ServerModel.getInstance().getDatadriver().getConn().prepareStatement(updateQuanQuery);
                updateQuanStatement.setString(1, quanValue);
                updateQuanStatement.setString(2, userID);
                updateQuanStatement.executeUpdate();
                updateQuanStatement.close();
            }

            // Update xa information
            String xaValue = textFieldXa.getText();
            if (!xaValue.equals(textXa.getText())) {
                String updateXaQuery = "UPDATE xa SET tenxa = ? WHERE userID = ?";
                PreparedStatement updateXaStatement = ServerModel.getInstance().getDatadriver().getConn().prepareStatement(updateXaQuery);
                updateXaStatement.setString(1, xaValue);
                updateXaStatement.setString(2, userID);
                updateXaStatement.executeUpdate();
                updateXaStatement.close();
            }

            // Update duong information
            String duongValue = textFieldDuong.getText();
            if (!duongValue.equals(textDuong.getText())) {
                String updateDuongQuery = "UPDATE duong_pho SET tenduong = ? WHERE userID = ?";
                PreparedStatement updateDuongStatement = ServerModel.getInstance().getDatadriver().getConn().prepareStatement(updateDuongQuery);
                updateDuongStatement.setString(1, duongValue);
                updateDuongStatement.setString(2, userID);
                updateDuongStatement.executeUpdate();
                updateDuongStatement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
