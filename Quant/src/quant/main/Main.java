package quant.main;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import quant.connector.DBConnection;
import quant.connector.Query;
import quant.core.Activity;

public class Main
{
	
	/*
	 * Gets available data from the database.
	 */
	public static void printStuff(DBConnection dBConnection) throws SQLException
	{
		Query query = new Query(dBConnection, "SELECT * FROM activities LEFT JOIN timetable ON activities.id = timetable.actId", false);
		List<Activity> actList = query.getData();
		
		for(Activity activity : actList)
		{
			System.out.println(activity.getName());
			System.out.println(activity.getDescription());
			System.out.println(activity.getType());
			if (activity.getPlaceAndTimes() != null)
			{
				for(Activity.PlaceAndTime placeAndTime : activity.getPlaceAndTimes())
				{
					System.out.println(placeAndTime.getPlace());
					System.out.println(placeAndTime.getTimeAndDate().toString());
				}
			}
		}
		
		query.closeQuery();
	}
	
	public static void main(String[] args)
	{
		// Printing stuff before we add some.
		DBConnection dBConnection = null;
		try
		{
			dBConnection = new DBConnection("jdbc:mysql://localhost:3306/quantdb", "root", "toor");
		}
		catch (SQLException e)
		{
			System.err.println("Error:" + e.toString() + "\nPrinting stack trace.");
			e.printStackTrace();
		}
		
		try
		{
			printStuff(dBConnection);
		}
		catch (SQLException e)
		{
			System.err.println("Error:" + e.toString() + "\nPrinting stack trace.");
			e.printStackTrace();
		}
		
		// Add some stuff to the database.
		Timestamp date = new Timestamp(0);
		Time duration = new Time(0);
		Time duration2 = new Time(1000);
		Activity actToStore = new Activity("inserted activity", "this one is from the java code", 0, "candyland", date, duration);
		actToStore.addPlaceAndTime(actToStore.new PlaceAndTime("neverland", date, duration2), false);
		
		try
		{
			Query.storeData(actToStore, dBConnection);
		}
		catch (SQLException e)
		{
			System.err.println("Error:" + e.toString() + "\nPrinting stack trace.");
			e.printStackTrace();
		}
		
		// Print it all again (should contain the new stuff again).
		try
		{
			printStuff(dBConnection);
		}
		catch (SQLException e)
		{
			System.err.println("Error:" + e.toString() + "\nPrinting stack trace.");
			e.printStackTrace();
		}
		
		try
		{
			Query.deleteData(actToStore, dBConnection);
		}
		catch (SQLException e)
		{
			System.err.println("Error:" + e.toString() + "\nPrinting stack trace.");
			e.printStackTrace();
		}
		
		// And again (should contain the original stuff).
		try
		{
			printStuff(dBConnection);
		}
		catch (SQLException e)
		{
			System.err.println("Error:" + e.toString() + "\nPrinting stack trace.");
			e.printStackTrace();
		}
		
		// Be precise, close it!
		try
		{
			dBConnection.closeConnection();
		}
		catch (SQLException e)
		{
			System.err.println("Error:" + e.toString() + "\nPrinting stack trace.");
			e.printStackTrace();
		}
		
		//TODO: Initialize/Connect to Database first
		//TODO: Pass initial data to the App
		//TODO: Initialize GUI
	}
}
