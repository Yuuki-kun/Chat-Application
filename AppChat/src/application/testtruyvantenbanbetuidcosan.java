package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class testtruyvantenbanbetuidcosan {
	public static void main(String[] args) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=testbanbechinhsua;username=sa;password=reallyStrongPwd123;trustServerCertificate=true;encrypt=true");
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery("SELECT userfriendid FROM friend WHERE id = '"+"a001"+"'");
		
		String userfid="";
		if(rs.next()) {
			userfid = rs.getString("userfriendid");
		}
		
		rs = stm.executeQuery("SELECT name FROM users WHERE id = '"+userfid+"'");

		if(rs.next()) {
			System.out.println(rs.getString("name"));
		}
		
	}
}
