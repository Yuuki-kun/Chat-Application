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

	private Connection connectToSQL() {
		Connection connection = null;
		try {
			
			String url = "jdbc:sqlserver://localhost:1433;databaseName = "; //Database name
			String username = ""; //UseName
			String password = ""; //Password

			connection = DriverManager.getConnection(url, username, password);

			System.out.println("Connected to SQL Server");
		} catch (SQLException e) {
			System.out.println("Failed to connect to SQL Server");
			e.printStackTrace();
		}
		return connection;
	}

	public DataDriver() {
		// connect sql
//		conn = connect to sql;
		conn = connectToSQL();
		
	}

	public ResultSet getExistUserResultSet(String username, String password) {

		// use PrepareStatement execute query

		try {
			String query = "SELECT * FROM users WHERE username = ? AND password = ?";
			statement = conn.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, password);

			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultSet;
	}
}
