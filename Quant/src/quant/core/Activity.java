package quant.core;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Activity extends LifeObjective
{
	
	private List<PlaceAndTime> placeAndTimes;
	private int actId = -1;
	//private int[] travelTime;		unnecessary yet, keep it simple
	//private String link; don't bother with it yet	// Link with other events, TODO: clarify usage, type, etc.
	//private GregorianCalendar dueDate;	Juuust keep it simple
	/* this variable is for repeated events. Default is 7 because events are usually weekly.*/
	//private int repeated = 7;		Guess what, keep it simple!
	
	public Activity(String name, String description, int type, List<PlaceAndTime> placeAndTime)
	{
		setName(name);
		setDescription(description);
		setType(type);
		setPlaceAndTimes(placeAndTime);
	}
	
	public Activity(String name, String description, int type, String place, Timestamp timeAndDate)
	{
		setName(name);
		setDescription(description);
		setType(type);
		this.placeAndTimes = new ArrayList<PlaceAndTime>();
		this.placeAndTimes.add(new PlaceAndTime(place, timeAndDate));
	}
	
	public Activity(String name, String description, int type, String place)
	{
		setName(name);
		setDescription(description);
		setType(type);
		this.placeAndTimes = new ArrayList<PlaceAndTime>();
		this.placeAndTimes.add(new PlaceAndTime(place));
	}
	
	public Activity(String name, String description, int type, Timestamp timeAndDate)
	{
		setName(name);
		setDescription(description);
		setType(type);
		this.placeAndTimes = new ArrayList<PlaceAndTime>();
		this.placeAndTimes.add(new PlaceAndTime(timeAndDate));
	}
	
	public Activity(String name, String description, int type)
	{
		setName(name);
		setDescription(description);
		setType(type);
		this.placeAndTimes = null;
	}
	
	/*
	 * This constructor is for getting data from the db. We can identify each activity by the actId, and when the activity is not in the database,
	 * its actId will be -1. We can use this later to update our database.
	 */
	public Activity(String name, String description, int type, String place, Timestamp timeAndDate, int actId)
	{
		setName(name);
		setDescription(description);
		setType(type);
		setActId(actId);
		this.placeAndTimes = new ArrayList<PlaceAndTime>();
		this.placeAndTimes.add(new PlaceAndTime(place, timeAndDate));
	}
	
	public Activity(String name, String description, int type, int actId)
	{
		setName(name);
		setDescription(description);
		setType(type);
		setActId(actId);
		this.placeAndTimes = null;
	}
	
	/* Data structure with this class is more similar to sql database structure.
	 * Should be easier to connect the two.
	 */
	public class PlaceAndTime
	{
		private String place;
		private Timestamp timeAndDate;	// Temporarily not a date array, but it needs to be one!
		
		public PlaceAndTime(String place, Timestamp timeAndDate)	
		{
			setPlace(place);
			setTimeAndDate(timeAndDate);
		}
		
		/*
		 *  For Unscheduled activities.
		 */
		public PlaceAndTime(String place)
		{
			setPlace(place);
			setTimeAndDate();
		}
		
		public PlaceAndTime(Timestamp timeAndDate)
		{
			setPlace();
			setTimeAndDate(timeAndDate);
		}

		public String getPlace() {
			return place;
		}
		
		public void setPlace(String place) {
			this.place = place;
		}
		
		/*
		 * If no place is required.
		 */
		public void setPlace()
		{
			this.place = null;
		}
		
		public Timestamp getTimeAndDate() {
			return timeAndDate;
		}
		
		/*
		 * For Unscheduled activities.
		 */
		public void setTimeAndDate() {
			this.timeAndDate = null;
		}
		
		public void setTimeAndDate(Timestamp timeAndDate)
		{
			this.timeAndDate = timeAndDate;
		}
	}

	public List<PlaceAndTime> getPlaceAndTimes() {
		return placeAndTimes;
	}

	public void setPlaceAndTimes(List<PlaceAndTime> placeAndTimes) {
		this.placeAndTimes = placeAndTimes;
	}
	
	public void addPlaceAndTime(PlaceAndTime placeAndtTime)
	{
		if(this.placeAndTimes == null)
			this.placeAndTimes = new ArrayList<PlaceAndTime>();
		this.placeAndTimes.add(placeAndtTime);
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}
}
