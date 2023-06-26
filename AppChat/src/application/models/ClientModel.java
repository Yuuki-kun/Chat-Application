package application.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import accounttype.AccountType;
import application.views.ViewFactory;
import request.LoginRequest;
import request.Request;
import request.RequestType;
import server.model.DataDriver;

public class ClientModel {
	
	private static ClientModel model;
	private final ViewFactory viewFactory;
	private AccountType logginAccountType = AccountType.CLIENT;
	private DataDriver datadriver;
	private boolean loginSuccessfully = false;
	private Client client;
	
	private ClientModel() {
		this.viewFactory = new ViewFactory(logginAccountType);
		this.client = new Client("localhost", 1234);
	}
	
	public void connectSQL(String dbname, String usrname, String pwd) {
		datadriver = new DataDriver(dbname,usrname,pwd);
	}
	
	public static synchronized ClientModel getInstance() {
		if(model==null) {
			model = new ClientModel();
		}
		return model;
	}
	public ViewFactory getViewFactory() {
		return viewFactory;
	}
	
	public void evaluateLoginType(String username, String password) {
		ResultSet resultSet = datadriver.getExistUserResultSet(username, password);
		try {
			if(resultSet.next()) {
				if(resultSet.getInt("type")==1) {
					this.logginAccountType=AccountType.CLIENT;
					System.out.println("CLIENT");
				}else if(resultSet.getInt("type")==2){
					System.out.println("ADIN");
					this.logginAccountType=AccountType.ADMIN;
				}else {
					this.loginSuccessfully = false;
				}
				this.loginSuccessfully = true;

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void connectServer(String servername, int PORT) {
		if(!servername.equals("") && servername!=null)
			client = new Client(servername, PORT);
	}
	
	public void sendLoginRequest(String username, String password) {
		Request rq = new LoginRequest(RequestType.LOGIN, username, password);
		rq.sendRequest();
	}
	
	public Client getClient() {
		return client;
	}
	
	public AccountType getLoginAccoutType() {
		return this.logginAccountType;
	}
	
	public boolean getLoginSuccessfully() {
		return this.loginSuccessfully;
	}
}
