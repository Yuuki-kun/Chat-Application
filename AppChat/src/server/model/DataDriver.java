package server.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
			String query = "SELECT * FROM newaccount WHERE username = ? AND password = ?";
			statement = conn.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, password);

			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultSet;
	}
	public Connection getConn() {
		return conn;
	}
}
