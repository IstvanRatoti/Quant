package quant.connector;

import java.sql.*;

/*
 * This class is used to connect to the database, and it stores the credentials.
 */
public class DBConnection
{
	public Connection conn;
	private String dbUrl;
	private String user;
	private String pass;
	
	public DBConnection(String dbUrl, String user, String pass) throws SQLException
	{
		this.setDbUrl(dbUrl);
		this.setUser(user);
		this.setPass(pass);
		this.conn = connectDB(dbUrl, user, pass);
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
	 * Connects to the DB, returns the established connection.
	 */
	
	public Connection connectDB(String dbUrl, String user, String pass) throws SQLException
	{
		Connection conn = DriverManager.getConnection(dbUrl, user, pass);
		
		return conn;
	}
	
	/*
	 * Closes the connection to the database.
	 */
	public void closeConnection() throws SQLException
	{
		this.conn.close();
	}
}
