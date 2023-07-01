package server.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataDriver {

	private Connection conn;
	private PreparedStatement statement;
	private ResultSet resultSet;

	public DataDriver(String databaseName, String username, String password) {
		// connect sql
//		conn = connect to sql;
		 try {
	            String url = "jdbc:sqlserver://localhost:1433;databaseName=" + databaseName+";encrypt=true; trustServerCertificate=true";
	            conn = DriverManager.getConnection(url, username, password);
	            System.out.println("Connected to SQL Server");
	        } catch (SQLException e) {
	            System.out.println("Failed to connect to SQL Server");
	            e.printStackTrace();
	        }
	}

	public ResultSet getExistUserResultSet(String username, String password) {

		// use PrepareStatement execute query

		try {
			String query = "SELECT * FROM account WHERE username = ? AND password = ?";
			statement = conn.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, password);

			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultSet;
	}
	public PreparedStatement getStatement() {
		return statement;
	}
	//lay danh sach id ban be cua usernameid
	public ArrayList<String> getUserFriendID(String userid) throws SQLException {
		String query = "SELECT frienduserid FROM has_friend WHERE userid = '"+userid+"'";
		Statement statement = conn.createStatement();
		resultSet = statement.executeQuery(query);
		
		ArrayList<String> userfrId = new ArrayList<>();
		ArrayList<String> listFriend = new ArrayList<>();

		
		while(resultSet.next()) {
			userfrId.add(resultSet.getString("frienduserid"));
			System.out.println("user friend id = "+resultSet.getString("frienduserid"));
		}
		
		
		for(String userFriendID : userfrId) {
			resultSet = statement.executeQuery("SELECT name FROM users WHERE userid = '"+userFriendID+"'");
			if(resultSet.next()) {
				listFriend.add(resultSet.getString("name"));
			}
		}
		
		return listFriend;
	}
	
	public boolean checkExistedUsername(String username) {
		/*
		doi tuong conn da duoc khoi tao san, su dung this.conn de su dung doi tuong connection
		kiem tra username co ton tai trong column "username" cua table "users" hay chua
		tra ve true neu da ton tai
		Note: test cau truy van trong mot lop ben ngoai project nay truoc, chi code vao day
		*/
	
		//Your code here
		return false;
	}
	
	public Connection getConn() {
		return conn;
	}
}
