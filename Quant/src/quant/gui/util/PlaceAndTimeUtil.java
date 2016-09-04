package quant.gui.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import quant.core.Activity;
import quant.core.Activity.PlaceAndTime;

public class PlaceAndTimeUtil	// Creates the schedule boxes, and contains information about them when its read out.
{
	public PlaceAndTime placeAndTime;
	public VBox scheduleBox;
	public int type;
	
	public PlaceAndTimeUtil(VBox scheduleBox, int type)		// Creates an object containing all the information required for the database.
	{
		Activity dummyAct = new Activity();
		
		this.placeAndTime = dummyAct.new PlaceAndTime(null, null, null, 0);
		this.scheduleBox = scheduleBox;
		this.type = type;
	}
	
	/*
	 * The main function called when creating new entries, or opening activity details with schedule entries.
	 * Creates the main VBox, the data object based on type. If data is null, its a new one, otherwise, it fills it in with data.
	 */
	public static VBox createScheduleBox(int type, VBox parent, PlaceAndTime data)
	{
		VBox scheduleBox = new VBox();
		
		PlaceAndTimeUtil placeAndTimeData = new PlaceAndTimeUtil(scheduleBox, type);
		
		scheduleBox.setUserData(placeAndTimeData);
		
		HBox dateBox = new HBox();		// Creates the date box with the parent VBox.
		dateBox.setMinHeight(30);
		dateBox.setPrefWidth(450);
		
		MenuButton addMenuBtn = new MenuButton("Add...");
		HBox.setMargin(addMenuBtn, new Insets(5, 5, 0, 0));
		
		MenuItem placeItem = new MenuItem("Place");
		MenuItem fromToItem = new MenuItem("From ... To");
		MenuItem fromItem = new MenuItem("From");
		MenuItem untilItem = new MenuItem("Until");
		MenuItem durationItem = new MenuItem("Duration");
		
		Button removeBtn = new Button("Remove");
		HBox.setMargin(removeBtn, new Insets(5, 5, 0, 0));
		
		Pane filler = new Pane();
		HBox.setHgrow(filler, Priority.ALWAYS);
		
		Label dateLabel = new Label();
		dateBox.getChildren().add(dateLabel);
		HBox.setMargin(dateLabel, new Insets(10, 5, 0, 0));
		
		switch(type)	// Creates the different types of schedules.
		{
		case 1:
			dateLabel.setText("Unscheduled");
			break;
		case 2:
			dateLabel.setText("Date:");
			dateLabel.setMinWidth(50);
			DatePicker date = new DatePicker();
			if(data != null)
				date.setValue(data.getTimeAndDate().toLocalDateTime().toLocalDate());
			else
				date.setValue(LocalDate.now());
			dateBox.getChildren().add(date);
			HBox.setMargin(date, new Insets(5, 5, 0, 0));
			break;
		case 3:
			dateLabel.setText("Deadline:");
			dateLabel.setMinWidth(50);
			DatePicker deadline = new DatePicker();
			if(data != null)
				deadline.setValue(data.getTimeAndDate().toLocalDateTime().toLocalDate());
			else
				deadline.setValue(LocalDate.now());
			HBox.setMargin(deadline, new Insets(5, 5, 0, 0));
			dateBox.getChildren().add(deadline);
			break;
		case 4:										// Weekly
			dateBox.getChildren().remove(dateLabel);
			CheckBox monday = new CheckBox("Mo");
			if( (data != null) && ((data.getScheduleId() & 1) == 1))
				monday.setSelected(true);
			HBox.setMargin(monday, new Insets(10, 5, 0, 0));
			CheckBox tuesday = new CheckBox("Tue");
			if( (data != null) && ((data.getScheduleId() & 2) == 2))
				tuesday.setSelected(true);
			HBox.setMargin(tuesday, new Insets(10, 5, 0, 0));
			CheckBox wednesday = new CheckBox("We");
			if( (data != null) && ((data.getScheduleId() & 4) == 4))
				wednesday.setSelected(true);
			HBox.setMargin(wednesday, new Insets(10, 5, 0, 0));
			CheckBox thursday = new CheckBox("Thu");
			if( (data != null) && ((data.getScheduleId() & 8) == 8))
				thursday.setSelected(true);
			HBox.setMargin(thursday, new Insets(10, 5, 0, 0));
			CheckBox friday = new CheckBox("Fri");
			if( (data != null) && ((data.getScheduleId() & 16) == 16))
				friday.setSelected(true);
			HBox.setMargin(friday, new Insets(10, 5, 0, 0));
			CheckBox saturday = new CheckBox("Sa");
			if( (data != null) && ((data.getScheduleId() & 32) == 32))
				saturday.setSelected(true);
			HBox.setMargin(saturday, new Insets(10, 5, 0, 0));
			CheckBox sunday = new CheckBox("Su");
			if( (data != null) && ((data.getScheduleId() & 64) == 64))
				sunday.setSelected(true);
			HBox.setMargin(sunday, new Insets(10, 5, 0, 0));
			dateBox.getChildren().addAll(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
			break;
		case 5:
			dateLabel.setText("Daily");
			break;
		}
		
		addMenuBtn.getItems().addAll(placeItem, fromToItem, fromItem, untilItem, durationItem);
		
		placeItem.setOnAction(e ->	{		// Creates a new placeBox when chosen. Also disables this button, so only one place can be set. Also sends info what to re-enable if removed.
										HBox placeBox = placeAndTimeData.createPlaceBox(scheduleBox, null);
										placeBox.setUserData(placeItem);
										scheduleBox.getChildren().add(scheduleBox.getChildren().indexOf(dateBox)+1 , placeBox);
										placeItem.setDisable(true);
									});
		
		fromToItem.setOnAction(e ->	{		// Creates a from...to schedule time. Also disables all other time variations. Also sends info what to re-enable if removed.
										HBox fromToBox = placeAndTimeData.createTimeBox(1, scheduleBox, null);
										List<MenuItem> menuItemList = new ArrayList<MenuItem>();
										menuItemList.add(fromToItem);
										menuItemList.add(fromItem);
										menuItemList.add(untilItem);
										menuItemList.add(durationItem);
										Pair<MenuItem, List<MenuItem>> menuItemPair = new Pair<MenuItem, List<MenuItem>>(null, menuItemList);
										fromToBox.setUserData(menuItemPair);
										scheduleBox.getChildren().add(fromToBox);
										fromToItem.setDisable(true);
										fromItem.setDisable(true);
										untilItem.setDisable(true);
										durationItem.setDisable(true);
									});
		
		fromItem.setOnAction(e ->	{		// Creates a from schedule time. Disables itself, until and from..to time variations. Also sends info what to re-enable if removed.
										HBox fromBox = placeAndTimeData.createTimeBox(2, scheduleBox, null);
										List<MenuItem> menuItemList = new ArrayList<MenuItem>();
										menuItemList.add(fromToItem);
										menuItemList.add(fromItem);
										menuItemList.add(untilItem);
										Pair<MenuItem, List<MenuItem>> menuItemPair = new Pair<MenuItem, List<MenuItem>>(durationItem, menuItemList);
										fromBox.setUserData(menuItemPair);
										if(durationItem.isDisable())
											scheduleBox.getChildren().add(scheduleBox.getChildren().size() - 1, fromBox);
										else
											scheduleBox.getChildren().add(fromBox);
										fromToItem.setDisable(true);
										fromItem.setDisable(true);
										untilItem.setDisable(true);
									});
		
		untilItem.setOnAction(e ->	{		// Exactly like from item.
										HBox untilBox = placeAndTimeData.createTimeBox(3, scheduleBox, null);
										List<MenuItem> menuItemList = new ArrayList<MenuItem>();
										menuItemList.add(fromToItem);
										menuItemList.add(fromItem);
										menuItemList.add(untilItem);
										Pair<MenuItem, List<MenuItem>> menuItemPair = new Pair<MenuItem, List<MenuItem>>(durationItem, menuItemList);
										untilBox.setUserData(menuItemPair);
										if(durationItem.isDisable())
											scheduleBox.getChildren().add(scheduleBox.getChildren().size() - 1, untilBox);
										else
											scheduleBox.getChildren().add(untilBox);
										fromToItem.setDisable(true);
										untilItem.setDisable(true);
										fromItem.setDisable(true);
									});
		
		durationItem.setOnAction(e ->	{		// Creates durationBox, and disables itself and from...to time schedules. Re-enable information is sent to.
											HBox durationBox = placeAndTimeData.createTimeBox(4, scheduleBox, null);
											List<MenuItem> menuItemList = new ArrayList<MenuItem>();
											menuItemList.add(durationItem);
											menuItemList.add(fromToItem);
											menuItemList.add(fromItem);
											menuItemList.add(null);
											menuItemList.add(null);
											Pair<MenuItem, List<MenuItem>> menuItemPair = new Pair<MenuItem, List<MenuItem>>(null, menuItemList);
											durationBox.setUserData(menuItemPair);
											scheduleBox.getChildren().add(durationBox);
											fromToItem.setDisable(true);
											durationItem.setDisable(true);
											
										});
		
		removeBtn.setOnAction(e ->	{
										parent.getChildren().remove(scheduleBox);
									});
		
		scheduleBox.getChildren().add(dateBox);
		dateBox.getChildren().addAll(filler, addMenuBtn, removeBtn);
		
		if(data != null)	// The part that creates existing schedule elements.
		{
			if(data.getPlace() != null) // Creates the place box. Easy. Same code as the place menu item onAction code.
			{
				HBox placeBox = placeAndTimeData.createPlaceBox(scheduleBox, data.getPlace());
				placeBox.setUserData(placeItem);
				scheduleBox.getChildren().add(scheduleBox.getChildren().indexOf(dateBox)+1 , placeBox);
				placeItem.setDisable(true);
			}
			
			if(data.getTimeAndDate().getTime() > 0)	// This one decides if we have a date or time stored.
			{
				if ((data.getScheduleId() & 128) == 128)		// Creates a from box. Code is the same as the from menu item onAction code.
				{
					HBox fromBox = placeAndTimeData.createTimeBox(2, scheduleBox, data);
					List<MenuItem> menuItemList = new ArrayList<MenuItem>();
					menuItemList.add(fromToItem);
					menuItemList.add(fromItem);
					menuItemList.add(untilItem);
					Pair<MenuItem, List<MenuItem>> menuItemPair = new Pair<MenuItem, List<MenuItem>>(durationItem, menuItemList);
					fromBox.setUserData(menuItemPair);
					if(durationItem.isDisable())
						scheduleBox.getChildren().add(scheduleBox.getChildren().size() - 1, fromBox);
					else
						scheduleBox.getChildren().add(fromBox);
					fromToItem.setDisable(true);
					fromItem.setDisable(true);
					untilItem.setDisable(true);
					
					if(data.getDuration() != null)		// Creates a duration too, if it exists.
					{
						HBox durationBox = placeAndTimeData.createTimeBox(4, scheduleBox, data);
						List<MenuItem> menuItemList2 = new ArrayList<MenuItem>();
						menuItemList2.add(durationItem);
						menuItemList2.add(fromToItem);
						menuItemList2.add(fromItem);
						menuItemList2.add(null);
						menuItemList2.add(null);
						Pair<MenuItem, List<MenuItem>> menuItemPair2 = new Pair<MenuItem, List<MenuItem>>(null, menuItemList);
						durationBox.setUserData(menuItemPair2);
						scheduleBox.getChildren().add(durationBox);
						fromToItem.setDisable(true);
						durationItem.setDisable(true);
					}
				}
				else if ((data.getScheduleId() & 256) == 256)	// Creates an until box. Code is the same as the until menu item onAction code.
					{
					HBox untilBox = placeAndTimeData.createTimeBox(3, scheduleBox, data);
					List<MenuItem> menuItemList = new ArrayList<MenuItem>();
					menuItemList.add(fromToItem);
					menuItemList.add(fromItem);
					menuItemList.add(untilItem);
					Pair<MenuItem, List<MenuItem>> menuItemPair = new Pair<MenuItem, List<MenuItem>>(durationItem, menuItemList);
					untilBox.setUserData(menuItemPair);
					if(durationItem.isDisable())
						scheduleBox.getChildren().add(scheduleBox.getChildren().size() - 1, untilBox);
					else
						scheduleBox.getChildren().add(untilBox);
					fromToItem.setDisable(true);
					untilItem.setDisable(true);
					fromItem.setDisable(true);
					
					if(data.getDuration() != null)	// Again, creates a duration too, if it exists.
					{
						HBox durationBox = placeAndTimeData.createTimeBox(4, scheduleBox, data);
						List<MenuItem> menuItemList2 = new ArrayList<MenuItem>();
						menuItemList2.add(durationItem);
						menuItemList2.add(fromToItem);
						menuItemList2.add(fromItem);
						menuItemList2.add(null);
						menuItemList2.add(null);
						Pair<MenuItem, List<MenuItem>> menuItemPair2 = new Pair<MenuItem, List<MenuItem>>(null, menuItemList);
						durationBox.setUserData(menuItemPair2);
						scheduleBox.getChildren().add(durationBox);
						fromToItem.setDisable(true);
						durationItem.setDisable(true);
					}
				}
				else if(data.getDuration() != null && (data.getTimeAndDate().toLocalDateTime().getHour() != 0))	// Creates a from...to box. Same code as onAction, again.
				{
					HBox fromToBox = placeAndTimeData.createTimeBox(1, scheduleBox, data);
					List<MenuItem> menuItemList = new ArrayList<MenuItem>();
					menuItemList.add(fromToItem);
					menuItemList.add(fromItem);
					menuItemList.add(untilItem);
					menuItemList.add(durationItem);
					Pair<MenuItem, List<MenuItem>> menuItemPair = new Pair<MenuItem, List<MenuItem>>(null, menuItemList);
					fromToBox.setUserData(menuItemPair);
					scheduleBox.getChildren().add(fromToBox);
					fromToItem.setDisable(true);
					fromItem.setDisable(true);
					untilItem.setDisable(true);
					durationItem.setDisable(true);
				}
				else if(data.getDuration() != null)		// Only a durationBox. Guess what, same code!
				{
					HBox durationBox = placeAndTimeData.createTimeBox(4, scheduleBox, data);
					List<MenuItem> menuItemList = new ArrayList<MenuItem>();
					menuItemList.add(durationItem);
					menuItemList.add(fromToItem);
					menuItemList.add(fromItem);
					menuItemList.add(null);
					menuItemList.add(null);
					Pair<MenuItem, List<MenuItem>> menuItemPair = new Pair<MenuItem, List<MenuItem>>(null, menuItemList);
					durationBox.setUserData(menuItemPair);
					scheduleBox.getChildren().add(durationBox);
					fromToItem.setDisable(true);
					durationItem.setDisable(true);
				}
			}
			else if(data.getDuration() != null)		// If no date and time is set, creates only a duration.
			{
				HBox durationBox = placeAndTimeData.createTimeBox(4, scheduleBox, data);
				List<MenuItem> menuItemList = new ArrayList<MenuItem>();
				menuItemList.add(durationItem);
				menuItemList.add(fromToItem);
				menuItemList.add(fromItem);
				menuItemList.add(null);		// These two are only needed so we know this is a durationBox.
				menuItemList.add(null);
				Pair<MenuItem, List<MenuItem>> menuItemPair = new Pair<MenuItem, List<MenuItem>>(null, menuItemList);
				durationBox.setUserData(menuItemPair);
				scheduleBox.getChildren().add(durationBox);
				fromToItem.setDisable(true);
				durationItem.setDisable(true);
			}
		}
		
		return scheduleBox;
	}
	
	private HBox createPlaceBox(VBox parent, String placeData)		// Creates a placeBox. Code is pretty self-explanatory.
	{
		HBox placeBox = new HBox();
		placeBox .setMinHeight(30);
		placeBox.setPrefWidth(360);
		
		Label placeLabel = new Label("Place:");
		placeLabel.setMinWidth(50);
		HBox.setMargin(placeLabel, new Insets(10, 5, 0, 0));
		
		TextField place = new TextField();
		if (placeData != null)
			place.setText(placeData);
		else
			place.setText("home");
		
		HBox.setMargin(place, new Insets(5, 5, 0, 0));
		
		Button removeBtn = new Button("Remove");
		HBox.setMargin(removeBtn, new Insets(5, 5, 0, 0));
		
		removeBtn.setOnAction(e ->	{
										parent.getChildren().remove(placeBox);
										MenuItem placeItem = (MenuItem) placeBox.getUserData();
										placeItem.setDisable(false);
									});
		
		placeBox.getChildren().addAll(placeLabel, place, removeBtn);
		
		return placeBox;
	}
	
	private HBox createTimeBox(int selection, VBox parent, PlaceAndTime data)	// Creates a timeBox.
	{
		HBox timeBox = new HBox();
		timeBox.setMinHeight(30);
		timeBox.setPrefWidth(360);
		
		Label timeLabel1 = new Label();
		timeLabel1.setMinWidth(50);
		HBox.setMargin(timeLabel1, new Insets(10, 5, 0, 0));
		
		Button removeBtn = new Button("Remove");
		HBox.setMargin(removeBtn, new Insets(5, 0, 0, 0));
				
		DigitalClock time1 = this.new DigitalClock();
		if(data != null && selection != 4)	// Handles setting all data except durations.
		{
			time1.setClock(data.getTimeAndDate().toLocalDateTime().getHour(), data.getTimeAndDate().toLocalDateTime().getMinute());
		}
		else if(data != null)	// Sets the duration clock.
			time1.setClock(data.getDuration());
		else
			time1.setDefault();
		
		timeBox.getChildren().addAll(timeLabel1, time1);
		
		switch(selection)	// Creates the appropriate time schedule type.
		{
			case 1:
				timeLabel1.setText("From:");
				Label timeLabel2 = new Label("to");
				HBox.setMargin(timeLabel2, new Insets(10, 5, 0, 0));
				DigitalClock time2 = this.new DigitalClock();
				if(data != null)
					time2.setClock(data.getDuration());
				else
					time2.setDefault();
				timeBox.getChildren().addAll(timeLabel2, time2);
				break;
			case 2:
				timeLabel1.setText("From:");
				break;
			case 3:
				timeLabel1.setText("Until:");
				break;
			case 4:
				timeLabel1.setText("Duration:");
				break;
		}
		
		removeBtn.setOnAction(e ->	{	// Uses data provided when creating the box, so we re-enable those that can be, but not those that should not be.
										parent.getChildren().remove(timeBox);
										@SuppressWarnings("unchecked")
										Pair<MenuItem, List<MenuItem>> menuItemPair = (Pair<MenuItem, List<MenuItem>>) timeBox.getUserData();
										List<MenuItem> menuItemList = menuItemPair.getValue();
										if(menuItemList.size() == 5)	// The tricky duration re-enable. 
										{
											menuItemList.get(0).setDisable(false);		// Re-enable duration item.
											if(!menuItemList.get(2).isDisable())	//Only re-enable from..to if from is not disabled (No from or until item present).
												menuItemList.get(1).setDisable(false);
										}
										else
										{
											for(MenuItem item : menuItemList)
												item.setDisable(false);
										}
										if((menuItemPair.getKey() != null)&&(menuItemPair.getKey().isDisable()))	// If its a from or until box (not null), and duration is disabled, re-enable from...to item.
												menuItemList.get(0).setDisable(true);
									});
		
		timeBox.getChildren().add(removeBtn);
		
		return timeBox;
	}
	
	/*
	 * Gets the data stored in our scheduleBox. Sets the object's variables.
	 */
	public void getData()
	{
		int i = 0;
		int scheduleId = 0;
		long timeAndDate = 0;
		
		for(Node dataBox : this.scheduleBox.getChildren())	// Iterates through all the schedule items.
		{
			if(i==0)	// Sets the date parameter. The first one is always the date.
			{
				Pair<Integer, Timestamp> datePair = this.getDate((HBox) dataBox);
				
				timeAndDate = datePair.getValue().getTime();
				scheduleId = datePair.getKey();
			}
			else		// The rest can be anything, so need to know what they are.
			{
				Node dataNode = ((HBox) dataBox).getChildren().get(1);	// The second node will always contain useful data.
				String labelString = ((Label) ((HBox) dataBox).getChildren().get(0)).getText();	// The first one will give us how to interpret is.
				
				if(dataNode.getClass() == DigitalClock.class)	// Time data.
				{	
					if(((HBox) dataBox).getChildren().size()>4)	// From...to type.
					{
						DigitalClock clock = ((DigitalClock) ((HBox) dataBox).getChildren().get(3));		// The "to" time. 
						long time = TimeUnit.MILLISECONDS.convert(clock.hours.getValue(), TimeUnit.HOURS);
						time += TimeUnit.MILLISECONDS.convert(clock.minutes.getValue(), TimeUnit.MINUTES);
						
						this.placeAndTime.setDuration(new Time(time));		// This one is stored in the duration variable (and later row).
						
						long time2 = TimeUnit.MILLISECONDS.convert(((DigitalClock) dataNode).hours.getValue(), TimeUnit.HOURS);
						time2 += TimeUnit.MILLISECONDS.convert(((DigitalClock) dataNode).minutes.getValue(), TimeUnit.MINUTES);
						
						timeAndDate += time2;	// The from is added to the Timestamp.
					}
					else if(labelString == "From:")		// From type.
					{
						long time2 = TimeUnit.MILLISECONDS.convert(((DigitalClock) dataNode).hours.getValue(), TimeUnit.HOURS);
						time2 += TimeUnit.MILLISECONDS.convert(((DigitalClock) dataNode).minutes.getValue(), TimeUnit.MINUTES);
						
						timeAndDate += time2;	// Added to the Timestamp as well.
						scheduleId += 128;		// This number identifies that its a From type.
					}
					else if(labelString == "Until:")	// Until type. same as from, but different scheduleId.
					{
						long time2 = TimeUnit.MILLISECONDS.convert(((DigitalClock) dataNode).hours.getValue(), TimeUnit.HOURS);
						time2 += TimeUnit.MILLISECONDS.convert(((DigitalClock) dataNode).minutes.getValue(), TimeUnit.MINUTES);
						
						timeAndDate += time2;
						scheduleId += 256;
					}
					else		// This will fetch the duration data.
					{
						DigitalClock clock = ((DigitalClock) ((HBox) dataBox).getChildren().get(1));
						long time = TimeUnit.MILLISECONDS.convert(clock.hours.getValue(), TimeUnit.HOURS);
						time += TimeUnit.MILLISECONDS.convert(clock.minutes.getValue(), TimeUnit.MINUTES);
						
						this.placeAndTime.setDuration(new Time(time));
					}
				}
				else	// If it doesn't have a clock, its a place.
				{
					this.placeAndTime.setPlace(((TextField) dataNode).getText());
				}
			}
			
			i++;
		}
		
		// Set the data.
		this.placeAndTime.setTimeAndDate(new Timestamp(timeAndDate));
		this.placeAndTime.setScheduleId(scheduleId);
	}
	
	private Pair<Integer, Timestamp> getDate(HBox dateBox)	// Gets the date and scheduleId out from our dateBox.
	{
		Timestamp date = new Timestamp(0);	// Sets the date to 1970-01-01 01:00
		int scheduleId = 0;
		
		switch(this.type)	
		{
			case 1:			// Unscheduled
				date.setHours(0);	// This one is needed to fix the shenanigans with the clock (-1 hours every time opened the details).
				break;
			case 2:			// Scheduled
				date = Timestamp.valueOf(((DatePicker) dateBox.getChildren().get(1)).getValue().atStartOfDay());
				break;
			case 3:			// Deadline
				date = Timestamp.valueOf(((DatePicker) dateBox.getChildren().get(1)).getValue().atStartOfDay());
				scheduleId = 512;	// Deadline scheduleId.
				break;
			case 4:			// Weekly
				for(int i = 0 ; i < 7 ; i++)
				{
					if(((CheckBox) dateBox.getChildren().get(i)).isSelected())
						scheduleId += Math.pow(2, i);	// Every day is a power of 2, starting with Monday = 1. This is how we know later exactly which days were chosen.
				}
				break;
			case 5:			// Daily
				scheduleId = 127;	// Daily scheduleId, same as all days of the weeks ticked.
				break;
		}
		
		return new Pair<Integer, Timestamp>(scheduleId, date);	// Return a pair with the date and the scheduleId.
	}
	
	public static int getScheduleId(PlaceAndTime placeAndTime)	// Figures out what type of schedule we have.
	{
		int scheduleId = placeAndTime.getScheduleId();
		
		if((scheduleId & 128) == 128)		// We don't care about from time type...
			scheduleId -= 128;
		if((scheduleId & 256) == 256)		// ...and until.
			scheduleId -= 256;
		
		if (scheduleId != 0)	// Returns the ones with specific scheduleId-s.
		{
			if ((scheduleId & 512) == 512)		// Deadline
				return 3;
			else if ((scheduleId & 127) == 127)	// Daily
				return 5;
			else								// Weekly
				return 4;
		}
		else	// And the ones having none.
		{
			if(placeAndTime.getTimeAndDate().getTime() < TimeUnit.MILLISECONDS.convert(2000, TimeUnit.DAYS))	// Ugly way to be sure its before 2016 (so unscheduled).
				return 1;						// Unscheduled
			else
				return 2;						// Scheduled
		}
	}
	
	public class DigitalClock extends HBox		// Custom class to handle times. Mostly Self-explanatory code.
	{
		public ComboBox<Integer> hours = new ComboBox<Integer>();
		public Label delimiter = new Label(":");
		public ComboBox<Integer> minutes = new ComboBox<Integer>();
		
		public DigitalClock()	// Creates a default Digital Clock
		{	
			hours.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
			minutes.setItems(FXCollections.observableArrayList(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55));
			this.hours.setMinSize(60, 20);
			this.delimiter.setMaxWidth(6);
			this.minutes.setMaxSize(60, 20);
			HBox.setMargin(hours, new Insets(5, 0, 0, 0));
			HBox.setMargin(delimiter, new Insets(7, 0, 0, 0));
			HBox.setMargin(minutes, new Insets(5, 5, 0, 0));
			
			this.setMaxSize(130, 30);
			this.getChildren().addAll(hours, delimiter, minutes);
		}
		
		public void setClock(Time time)
		{
			long millis = time.getTime();
			
			int seconds = (int) (millis/1000);
			int minutes = seconds/60;
			
			this.minutes.setValue(minutes%60);
			this.hours.setValue(minutes/60);
		}
		
		public void setClock(int hours, int minutes)
		{
			this.minutes.setValue(minutes);
			this.hours.setValue(hours);
		}
		
		public void setDefault()
		{
			this.hours.setValue(12);
			this.minutes.setValue(0);
		}
	}
}
