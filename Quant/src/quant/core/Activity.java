package quant.core;


import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Activity extends LifeObjective
{
	
	private List<PlaceAndTime> placeAndTimes;
	private int actId = -1;
	
	/*
	 * These constructors are for creating the object in the app. The actId (should) default to -1, that's how we know its not in the database.
	 */
	public Activity(String name, String description, int type, List<PlaceAndTime> placeAndTimes)
	{
		setName(name);
		setDescription(description);
		setType(type);
		setPlaceAndTimes(placeAndTimes);
	}
	
	public Activity(String name, String description, int type, PlaceAndTime placeAndTime)
	{
		setName(name);
		setDescription(description);
		setType(type);
		this.placeAndTimes = new ArrayList<PlaceAndTime>();
		this.placeAndTimes.add(placeAndTime);
	}
	
	public Activity(String name, String description, int type, String place, Timestamp timeAndDate, Time duration)
	{
		setName(name);
		setDescription(description);
		setType(type);
		this.placeAndTimes = new ArrayList<PlaceAndTime>();
		this.placeAndTimes.add(new PlaceAndTime(place, timeAndDate, duration));
	}
	
	public Activity(String name, String description, int type)
	{
		setName(name);
		setDescription(description);
		setType(type);
		this.placeAndTimes = null;
	}
	
	/*
	 * These constructor are for getting data from the db. We can identify each activity by the actId, and when the activity is not in the database,
	 * its actId will be -1. We can use this later to update our database.
	 */
	public Activity(String name, String description, int type, String place, Timestamp timeAndDate, Time duration, int actId)
	{
		setName(name);
		setDescription(description);
		setType(type);
		setActId(actId);
		this.placeAndTimes = new ArrayList<PlaceAndTime>();
		this.placeAndTimes.add(new PlaceAndTime(place, timeAndDate, duration));
	}
	
	public Activity(String name, String description, int type, int actId)
	{
		setName(name);
		setDescription(description);
		setType(type);
		setActId(actId);
		this.placeAndTimes = null;
	}
	
	/* 
	 * Data structure with this class is more similar to sql database structure.
	 * Should be easier to connect the two.
	 */
	public class PlaceAndTime
	{
		private String place;
		private Timestamp timeAndDate;
		private Time duration;
		
		public PlaceAndTime(String place, Timestamp timeAndDate, Time duration)	
		{
			setPlace(place);
			setTimeAndDate(timeAndDate);
			setDuration(duration);
		}

		public String getPlace() {
			return place;
		}
		
		public void setPlace(String place) {
			this.place = place;
		}
		
		public Timestamp getTimeAndDate() {
			return timeAndDate;
		}
		
		public void setTimeAndDate(Timestamp timeAndDate)
		{
			this.timeAndDate = timeAndDate;
		}

		public Time getDuration() {
			return duration;
		}

		public void setDuration(Time duration) {
			this.duration = duration;
		}

		public String getDurationString()
		{
			String returnString = "";
			
			if(this.duration == null)
				returnString = "NULL";
			else
				returnString = "\'" + this.duration.toString() + "\'";
			
			return returnString;
		}

		public String getPlaceString()
		{
			String returnString = "";
			
			if(this.place == null)
				returnString = "NULL";
			else
				returnString = "\'" + this.place + "\'";
			
			return returnString;
		}

		public String getTimeAndDateString()
		{
			String returnString = "";
			
			if(this.timeAndDate == null)
				returnString = "NULL";
			else
				returnString = "\'" + this.timeAndDate.toString() + "\'";
			
			return returnString;
		}
	}

	public List<PlaceAndTime> getPlaceAndTimes() {
		return placeAndTimes;
	}

	public void setPlaceAndTimes(List<PlaceAndTime> placeAndTimes) {
		this.placeAndTimes = placeAndTimes;
	}
	
	/*
	 * Use this only to add a new PlaceAndTime object to an existing Activity object. If adding a new date to a free activity,
	 *  this method will optionally add a record with no date or time, "keeping" the free portion of the activity too.
	 */
	public void addPlaceAndTime(PlaceAndTime placeAndTime, boolean isReplacing)
	{
		if(this.placeAndTimes == null)
		{
			this.placeAndTimes = new ArrayList<PlaceAndTime>();
			if(!isReplacing)
				this.placeAndTimes.add(new PlaceAndTime(null, null, null));
		}
		this.placeAndTimes.add(placeAndTime);
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}
}
