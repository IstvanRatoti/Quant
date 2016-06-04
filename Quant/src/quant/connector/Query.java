package quant.connector;

import java.sql.*;
import quant.core.*;

public class Query
{
	private Statement stmt;
	private ResultSet rs;
	
	/*
	 * Queries the server with given sql statement.
	 */
	public Query(DBConnection dbconnection, String sql)
	{
		try
		{
			this.stmt = dbconnection.conn.createStatement();
		}
		catch (SQLException e)
		{
			System.err.println("Error in statement, printing stack trace.");
			e.printStackTrace();
		}
		
		try
		{
			this.rs = stmt.executeQuery(sql);
		}
		catch (SQLException e)
		{
			System.err.println("Error in result set, printing stack trace.");
			e.printStackTrace();
		}
	}
	
	public Statement getStatement()
	{
		return this.stmt;
	}

	public ResultSet getResultSet()
	{
		return this.rs;
	}
	
	/*
	 * Gets the data from the result set, and creates a new activity object.
	 */
	public Activity getData()
	{
		Activity activity = null;
		ResultSet rs = this.rs;
		int currAct = 0;	// This integer is used to determine if the result set contains places and dates for the same activity.
		
		try
		{	
			while(rs.next())
			{
				if((currAct == 0) || (currAct != rs.getInt("actId")))
				{
					currAct = rs.getInt("actId");
					activity = new Activity (	rs.getString("actName"), rs.getString("description"), rs.getInt("actType"),
												rs.getString("place"), rs.getTimestamp("actDate"));
				}
				else	// If its the same activity, just add a new place and time to its object.
				{
					activity.addPlaceAndTime(activity.new PlaceAndTime(rs.getString("place"), rs.getTimestamp("actDate")));
				}
			}
		}
		catch (SQLException e)
		{
			System.err.println("Error in result set reading, printing stack trace.");
			e.printStackTrace();
		}
		
		return activity;
	}
}
