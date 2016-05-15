package quant.core;


import java.util.GregorianCalendar;

public class Activity extends LifeObjective {
	
	private PlaceAndTime placeAndTime;
	//private int[] travelTime;		unnecessary yet, keep it simple
	//private String link; don't bother with it yet	// Link with other events, TODO: clarify usage, type, etc.
	//private GregorianCalendar dueDate;	Juuust keep it simple
	/* this variable is for repeated events. Default is 7 because events are usually weekly.*/
	//private int repeated = 7;		Guess what, keep it simple!
	
	/* Data structure with this class is more similar to sql database structure.
	 * Should be easier to connect the two.
	 */
	public class PlaceAndTime
	{
		private String place;
		private GregorianCalendar[] timeAndDate;
		
		public String getPlace() {
			return place;
		}
		
		public void setPlace(String place) {
			this.place = place;
		}
		
		
		public GregorianCalendar[] getTimeAndDate() {
			return timeAndDate;
		}
		
		/* 
		 * Modifies or creates a date and time, requires a starting date and an end date.
		 * startDate is the array with the start dates, endDate is the end date array
		 * TODO: testing, exception handling.
		 */
		public void setTimeAndDate(GregorianCalendar[] startDate, GregorianCalendar[] endDate) {
			for(int i=0;i<startDate.length;i++) {
				this.timeAndDate[i*2]=startDate[i];
				this.timeAndDate[i*2+1]=endDate[i];
			}
		}
		
		/*
		 * For Unscheduled activities.
		 */
		public void setTimeAndDate() {
			this.timeAndDate=null;
		}
	}

	public PlaceAndTime getPlaceAndTime() {
		return placeAndTime;
	}

	public void setPlaceAndTime(PlaceAndTime placeAndTime) {
		this.placeAndTime = placeAndTime;
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
