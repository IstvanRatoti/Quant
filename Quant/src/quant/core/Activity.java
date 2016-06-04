package quant.core;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Activity extends LifeObjective {
	
	private List<PlaceAndTime> placeAndTimes;
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
		
		public String getPlace() {
			return place;
		}
		
		public void setPlace(String place) {
			this.place = place;
		}
		
		
		public Timestamp getTimeAndDate() {
			return timeAndDate;
		}
		
		/* 
		 * Modifies or creates a date and time, requires a starting date and an end date.
		 * startDate is the array with the start dates, endDate is the end date array
		 * TODO: testing, exception handling.
		 
		public void setTimeAndDate(GregorianCalendar[] startDate, GregorianCalendar[] endDate) {
			for(int i=0;i<startDate.length;i++) {
				this.timeAndDate[i*2]=startDate[i];
				this.timeAndDate[i*2+1]=endDate[i];
			}
		}*/
		
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

	/*public int[] getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(int hours, int minutes) {
		this.travelTime[0] = hours;
		this.travelTime[1] = minutes;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}*/
	
	/*public int getRepeated() {
		return repeated;
	}

	public void setRepeated(int repeats) {
		this.repeated = repeats;
	}

	public GregorianCalendar getDueDate() {
		return dueDate;
	}

	public void setDueDate(GregorianCalendar dueDate) {
		this.dueDate = dueDate;
	}*/
}
