package server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import server.handler.ClientHandler;
import server.model.ServerModel;

public class ServerController implements Initializable{
	  @FXML
	    private TextField PORT_tf;

	    @FXML
	    private JFXButton connectDB_btn;

	    @FXML
	    private TextField dbname_tf;

	    @FXML
	    private TextField password_tf;

	    @FXML
	    private JFXButton startServer_btn;

	    @FXML
	    private TextField username_tf;

	
	private ServerSocket serverSocket;
	private int PORT = -1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		startServer_btn.setOnAction(event -> startServer());
		connectDB_btn.setOnAction(event -> {
			try {
				connectDB();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}
	
	public void startServer() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				PORT = Integer.parseInt(PORT_tf.getText());
				try {
					serverSocket = new ServerSocket(PORT);
					System.out.println("created server!");
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					while(!serverSocket.isClosed()) {
						System.out.println("server is listening...");
						Socket socket = serverSocket.accept();
						System.out.println(socket.getInetAddress()+" new client connected!");
						Thread clientHandler = new Thread(new ClientHandler(socket));
						clientHandler.start();

					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	public void connectDB() throws SQLException {
//		ServerModel.getInstance().connectSQL(dbname_tf.getText(), username_tf.getText(), password_tf.getText());
		ServerModel.getInstance().connectSQL("TTTTT", "sa", "reallyStrongPwd123");
		//co the dung duoc cau lenh nay
//		
//		Statement st = Model.getInstance().getConn().createStatement();
//		ResultSet rs = st.executeQuery( "SELECT * FROM newaccount");
//		
//		while(rs.next()) {
//			System.out.println(rs.getInt("id"));
//			System.out.println(rs.getString("username"));
//			System.out.println(rs.getString("password"));
//			System.out.println(rs.getInt("type"));
//		}
		
	}


}