package application.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import application.views.AccountType;
import application.views.ViewFactory;

public class Model {
	
	private static Model model;
	private final ViewFactory viewFactory;
	private AccountType logginAccountType = AccountType.CLIENT;
	private DataDriver datadriver;
	private boolean loginSuccessfully = false;
	
	private Model() {
		this.viewFactory = new ViewFactory(logginAccountType);
		
	}
	
	public void connectSQL(String dbname, String usrname, String pwd) {
		datadriver = new DataDriver(dbname,usrname,pwd);
	}
	
	public static synchronized Model getInstance() {
		if(model==null) {
			model = new Model();
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
	
	public AccountType getLoginAccoutType() {
		return this.logginAccountType;
	}
	
	public boolean getLoginSuccessfully() {
		return this.loginSuccessfully;
	}
}
