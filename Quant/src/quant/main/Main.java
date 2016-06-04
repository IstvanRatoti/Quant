package quant.main;

import quant.connector.DBConnection;
import quant.connector.Query;
import quant.core.Activity;

public class Main
{
	
	public static void main(String[] args)
	{
		DBConnection dBConnection = new DBConnection("jdbc:mysql://localhost:3306/quantdb", "root", "toor");
		
		Activity activity = new Query(dBConnection, "SELECT * FROM activities JOIN timetable ON activities.id = timetable.actId").getData();
		
		System.out.println(activity.getName());
		System.out.println(activity.getDescription());
		System.out.println(activity.getType());
		System.out.println(activity.getPlaceAndTime().getPlace());
		System.out.println(activity.getPlaceAndTime().getTimeAndDate().toString());
		
		
		//TODO: Initialize/Connect to Database first
		//TODO: Pass initial data to the App
		//TODO: Initialize GUI
	}
}
