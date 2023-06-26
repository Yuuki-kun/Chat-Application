package server.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import accounttype.AccountType;

public class ServerModel {
	
	private static ServerModel model;
	private AccountType logginAccountType = AccountType.CLIENT;
	private DataDriver datadriver;
	private boolean loginSuccessfully = false;
	
	private ServerModel() {		
	}
	
	public void connectSQL(String dbname, String usrname, String pwd) {
		datadriver = new DataDriver(dbname,usrname,pwd);
	}
	
	public static synchronized ServerModel getInstance() {
		if(model==null) {
			model = new ServerModel();
		}
		return model;
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

	
	
	public AccountType getLoginAccoutType() {
		return this.logginAccountType;
	}
	
	public boolean getLoginSuccessfully() {
		return this.loginSuccessfully;
	}
	
	public Connection getConn() {
		return datadriver.getConn();
	}
}
