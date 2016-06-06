package quant.connector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import quant.core.*;

public class Query
{
	private Statement stmt;
	private ResultSet rs;
	
	/*
	 * Queries the server with given sql statement.
	 */
	public Query(DBConnection dBConnection, String sql, boolean manipulation) throws SQLException
	{
		this.stmt = dBConnection.conn.createStatement();
		
		if (manipulation)
		{
			stmt.executeUpdate(sql);
			this.rs = null;
		}
		else
			this.rs = stmt.executeQuery(sql);
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
	public List<Activity> getData() throws SQLException
	{
		ResultSet rs = this.rs;
		List<Activity> actList = new ArrayList<Activity>();	//TODO: This is a size 10 list. It works for now, but it needs to be flexible.
		int currAct = 0;	// This integer is used to determine if the result set contains places and dates for the same activity.
		int i = -1;
		
		while(rs.next())
		{
			if((currAct == 0) || (currAct != rs.getInt("actId")))
			{
				i++;
				currAct = rs.getInt("actId");
				//System.out.println(currAct);
				if (currAct != 0)
				{
					actList.add(new Activity (	rs.getString("actName"), rs.getString("description"), rs.getInt("actType"),
												rs.getString("place"), rs.getTimestamp("actDate"), rs.getInt("activities.id")));
				}
				else
				{
					actList.add(new Activity (rs.getString("actName"), rs.getString("description"), rs.getInt("actType"), rs.getInt("activities.id")));
				}
			}
			else	// If its the same activity, just add a new place and time to its object.
			{
				actList.get(i).addPlaceAndTime(actList.get(i).new PlaceAndTime(rs.getString("place"), rs.getTimestamp("actDate")));
			}
		}
		
		return actList;
	}
	
	/*
	 * This function stores data from an activity object in the database. It is static because it creates the queries it needs.
	 */
	public static void storeData(Activity activity, DBConnection dBConnection) throws SQLException
	{
		Query actInsertQuery;	// First query. Need two because the first will insert the id we need to supply the third with.
		Query actIdQuery;		// This query will get us the id we need for the third one.
		
		String sqlActInsert =	"INSERT INTO activities(actName, actType, description) VALUES (\'"
																								+ activity.getName() + "\', "
																								+ activity.getIntType() + ", \'"
																								+ activity.getDescription() + "\'"
								+ ");";
		
		actInsertQuery = new Query(dBConnection, sqlActInsert, true);
		actInsertQuery.closeQuery();
		
		actIdQuery = new Query(dBConnection, "SELECT id FROM activities WHERE actName=\'" + activity.getName() + "\';", false);
		actIdQuery.rs.next();
		int actId = actIdQuery.rs.getInt("id");
		activity.setActId(actId);
		
		for(Activity.PlaceAndTime placeAndTime : activity.getPlaceAndTimes())
		{
			String sqlTimeInsert =	"INSERT INTO timetable(actId, actDate, place) VALUES ("
																							+ actId + ", \'"	//This will (hopefully) insert the correct actId.
																							+ placeAndTime.getTimeAndDate().toString() + "\', \'"
																							+ placeAndTime.getPlace()  + "\'"
									+ ");";
			
			Query timeInsertQuery = new Query(dBConnection, sqlTimeInsert, true);
			timeInsertQuery.closeQuery();
		}
		
		actIdQuery.closeQuery();
	}
	
	/*
	 * List variation of the storeData function.
	 */
	public static void storeData(List<Activity> actList, DBConnection dBConnection) throws SQLException
	{
		for(Activity activity : actList)
			storeData(activity, dBConnection);
	}
	
	/*
	 * Deletes the activity from the sql database. WARNING! Will NOT delete(if java could...) the object.
	 */
	public static void deleteData(Activity activity, DBConnection dBConnection) throws SQLException
	{
		String sql =	"DELETE activities, timetable FROM activities LEFT JOIN timetable ON activities.id=timetable.actId WHERE activities.id=\'"
						+ activity.getActId() + "\';";
		Query deleteQuery = new Query(dBConnection, sql, true);
		deleteQuery.closeQuery();
	}
	
	/*
	 * List variation of the deleteData function
	 */
	public static void deleteData(List<Activity> actList, DBConnection dBConnection) throws SQLException
	{
		for(Activity activity : actList)
			deleteData(activity, dBConnection);
	}
	
	public void closeQuery() throws SQLException
	{
		if (this.rs != null)
			this.rs.close();
		this.stmt.close();
	}
}
