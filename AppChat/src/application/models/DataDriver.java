package application.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataDriver {
	
	private Connection conn;
	private PreparedStatement statement;
	private ResultSet resultSet;
	
	public DataDriver() {
		//connect sql
		//conn = connect to sql
	}
	
	public ResultSet getExistUserResultSet(String username, String password) {
		
		//use PrepareStatement execute query
	    		
		return resultSet;
	}
}
