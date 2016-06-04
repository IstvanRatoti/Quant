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
	
	/*
	 * connects to the DB, returns the established connection.
	 */
	
	public Connection connectDB(String dbUrl, String user, String pass)
	{
		Connection conn = null;
		
		try
		{
			conn = DriverManager.getConnection(dbUrl, user, pass);
		}
		catch (SQLException e)
		{
			System.err.println("Error in connecting to database, printing stack trace.");
			e.printStackTrace();
		}
		
		return conn;
	}
	
	/*
	 * closes the connection to the database
	 */
	public void closeConnection()
	{
		try
		{
			this.conn.close();
		}
		catch (SQLException e)
		{
			System.err.println("Error in closing connection, printing stack trace.");
			e.printStackTrace();
		}
	}
}
