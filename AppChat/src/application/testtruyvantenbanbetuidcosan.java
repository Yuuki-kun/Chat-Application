package application;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class testtruyvantenbanbetuidcosan {
	public static void main(String[] args) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=AppChatV2;username=sa;password=reallyStrongPwd123;trustServerCertificate=true;encrypt=true");
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery("SELECT userfriendid FROM friend WHERE id = '"+"u1"+"'");
		
		ArrayList<String> friends = new ArrayList<>();

		while(rs.next()) {
			System.out.println(rs.getString("userfriendid"));
			friends.add(rs.getString("userfriendid"));
		}
		

		rs = stm.executeQuery("SELECT name FROM users WHERE id = '"+friends.get(0)+"'");
		if(rs.next()) {
			System.out.println(rs.getString("name"));
		}
		
		rs = stm.executeQuery("SELECT name FROM users WHERE id = '"+friends.get(1)+"'");
		if(rs.next()) {
			System.out.println(rs.getString("name"));
		}
		
	}
}
