package bankdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import evilbank.Accounts;

public class ConnectionTest {
	
	public Accounts readAcct(String acctNo) {
		
		return null;
	}

    public static void main(String args[]) throws SQLException {
        //URL of Oracle database server
        String url = "jdbc:oracle:thin:system/password@localhost"; 
      
        //properties for creating connection to Oracle database
        Properties props = new Properties();
        props.setProperty("user", "testuserdb");
        props.setProperty("password", "password");
      
        //creating connection to Oracle database using JDBC
        Connection conn = DriverManager.getConnection(url,props);

        String sql ="select sysdate as current_day from dual";

        //creating PreparedStatement object to execute query
        PreparedStatement preStatement = conn.prepareStatement(sql);
    
        ResultSet result = preStatement.executeQuery();
      
        while(result.next()){
            System.out.println("Current Date from Oracle : " +         result.getString("current_day"));
        }
        System.out.println("done");
      
    }
}
