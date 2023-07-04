package server.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

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

	
	public ArrayList<String> evaluateLoginType(String username, String password) {
		ResultSet resultSet = datadriver.getExistUserResultSet(username, password);
		String userID = null;
		String usernameFromDB = null;
		ArrayList<String> IDandName = new ArrayList<>();

		try {
			if(resultSet.next()) {
				System.out.println("TYPE = "+resultSet.getString("type"));
				if(resultSet.getString("type").equals("client")) {
					this.logginAccountType=AccountType.CLIENT;
					userID = resultSet.getString("userid");
					System.out.println("CLIENT");
				}else if(resultSet.getString("type").equals("admin")){
					System.out.println("ADIN");
					this.logginAccountType=AccountType.ADMIN;
					userID = resultSet.getString("userid");
				}else {
					this.loginSuccessfully = false;
				}
				
				
				this.loginSuccessfully = true;
				
				Statement stm = datadriver.getConn().createStatement();
				
				resultSet = stm.executeQuery("SELECT name FROM users WHERE userid = '"+userID+"'");
				if(resultSet.next()) {
					
					usernameFromDB = resultSet.getString("name");
				}
				
				IDandName.add(userID);
				IDandName.add(usernameFromDB);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return IDandName;
		
	}

	public DataDriver getDatadriver() {
		return datadriver;
	}
	
	public Map<String, String> getFriendList(String userid) throws SQLException{
		return datadriver.getUserFriendID(userid);
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
