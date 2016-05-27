package quant.connector;

import java.sql.*;

/*
 * This class is used to connect to the database, and it stores the credentials.
 */
public class DBConnection
{
	protected Connection conn;
	private String dbUrl;
	private String user;
	private String pass;
	
	public DBConnection(String dbUrl, String user, String pass)
	{
		this.setDbUrl(dbUrl);
		this.setUser(user);
		this.setPass(pass);
		conn = connectDB(dbUrl, user, pass);
	}
	
	public Connection connectDB(String dbUrl, String user, String pass)
	{
		Connection conn = null;
		
		try
		{
			conn = DriverManager.getConnection(dbUrl, user, pass);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return conn;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
}
