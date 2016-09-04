package quant.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import quant.connector.Query;
import quant.core.Activity;
import quant.core.Activity.PlaceAndTime;
import quant.gui.util.PlaceAndTimeUtil;
import quant.main.Main;

public class DetailsWindowCont
{
	private int intType = 0;	// With this variable, we don't need to change the types back to numbers.
	private int actId = -2;		// Need this so we know which activity we edited, or if we created a new one. We use -2 because -1 means the activity is not in the database yet.
	
	@FXML
	public Label descLabel;
	@FXML
	public HBox descBox;
	
	@FXML
	public VBox baseBox;	// Just here so we can access the stage through it.
	@FXML
	public TextField name = new TextField();
	@FXML
	public ComboBox<String> type = new ComboBox<String>();
	@FXML
	public TextArea desc = new TextArea();
	@FXML
	public VBox detailsBox;
	@FXML
	public Button acceptBtn;
	@FXML
	public Button cancelBtn;
	
	@FXML
	MenuButton newEntryMenuBtn = new MenuButton();
	@FXML
	public MenuItem unscheduled = new MenuItem();
	@FXML
	public MenuItem scheduled = new MenuItem();
	@FXML
	public MenuItem deadline = new MenuItem();
	@FXML
	public MenuItem weekly = new MenuItem();
	@FXML
	public MenuItem daily = new MenuItem();
	
	@FXML
	private void initialize()
	{
		acceptBtn.setDisable(true);	// Accept Button on a new window will be disabled by default.
		
		this.type.setItems(FXCollections.observableArrayList("Obligatory", "Health", "Self-Improvement", "Recreational", "Charity", "Creative"));
		this.type.setValue("Obligatory");
		HBox.setMargin(descBox, new Insets(5, 0, 0, 0));
		
		acceptBtn.setOnAction(e -> {	// Saves the data and closes the window.
										Activity actToStore = null;
										
										List<Activity.PlaceAndTime> placeAndTimes = getPlaceAndTimeData();
										
										if(actId == -2)	// Creates a new activity and saves it.
										{
											actToStore = new Activity 	(
																			name.getText(), 
																			desc.getText(), 
																			this.intType, 
																			placeAndTimes	//TODO: temporary scheduleId to be stored.
																		);
											Pair<SimpleStringProperty, Activity> newPair = new Pair<SimpleStringProperty, Activity>(new SimpleStringProperty(actToStore.getName()), actToStore);
											Main.appData.add(newPair);
											
											((MainWindowCont) baseBox.getScene().getUserData()).addActBox(newPair);
										}
										else	// Updates an existing activity.
										{
											for(Pair<SimpleStringProperty, Activity> actPair : Main.appData)
											{
												actToStore = actPair.getValue();
												
												if(actId == actToStore.getActId())
												{
													actPair.getKey().set(name.getText());
													
													actToStore.setName(name.getText());
													actToStore.setDescription(desc.getText());
													actToStore.setType(this.intType);
													actToStore.setPlaceAndTimes(placeAndTimes);
													break;
												}
											}
										}
										
										try
										{
											Query.deleteData(actToStore, Main.connection);
											Query.storeData(actToStore, Main.connection);
										} 
										catch (Exception e1)
										{
											e1.printStackTrace();
										}
										
										Stage stage = (Stage) baseBox.getScene().getWindow();
										stage.close();
									});
		
		cancelBtn.setOnAction(e -> {	// Closes the window, doesn't save anything.
										Stage stage = (Stage) baseBox.getScene().getWindow();
										stage.close();
									});
		
		type.getSelectionModel().selectedItemProperty().addListener(		// Listens for changes in the ChoiceBox, and sets intType accordingly.
				(ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
																										switch(newValue)
																										{
																											case "Obligatory":
																												this.intType = 0;
																												break;
																											case "Health":
																												this.intType = 1;
																												break;
																											case "Self-Improvement":
																												this.intType = 2;
																												break;
																											case "Recreational":
																												this.intType = 3;
																												break;
																											case "Charity":
																												this.intType = 4;
																												break;
																											case "Creative":
																												this.intType = 5;
																												break;
																										}
																									});
		
		name.textProperty().addListener(				// This one makes sure that we can only add or change an activity if it has a name
				(observable, oldValue, newValue) ->	{
														if(newValue.isEmpty())
															acceptBtn.setDisable(true);
														else
															acceptBtn.setDisable(false);
													});
		
		unscheduled.setOnAction(e ->	{
											VBox scheduleBox = PlaceAndTimeUtil.createScheduleBox(1, detailsBox, null);
											detailsBox.getChildren().add(detailsBox.getChildren().indexOf(newEntryMenuBtn), scheduleBox);
										});
		scheduled.setOnAction(e ->	{
										VBox scheduleBox = PlaceAndTimeUtil.createScheduleBox(2, detailsBox, null);
										detailsBox.getChildren().add(detailsBox.getChildren().indexOf(newEntryMenuBtn), scheduleBox);
									});
		
		deadline.setOnAction(e ->	{
										VBox scheduleBox = PlaceAndTimeUtil.createScheduleBox(3, detailsBox, null);
										detailsBox.getChildren().add(detailsBox.getChildren().indexOf(newEntryMenuBtn), scheduleBox);
									});
		
		weekly.setOnAction(e ->	{
									VBox scheduleBox = PlaceAndTimeUtil.createScheduleBox(4, detailsBox, null);
									detailsBox.getChildren().add(detailsBox.getChildren().indexOf(newEntryMenuBtn), scheduleBox);
								});
		
		daily.setOnAction(e -> 	{
									VBox scheduleBox = PlaceAndTimeUtil.createScheduleBox(5, detailsBox, null);
									detailsBox.getChildren().add(detailsBox.getChildren().indexOf(newEntryMenuBtn), scheduleBox);
								});
		
	}
	
	private List<PlaceAndTime> getPlaceAndTimeData()	// Gets the data from our scheduleBoxes.
	{
		List<PlaceAndTime> placeAndTimes = null;
		Object placeAndTimeElement = null;
		int i = 1;
		
		// Goes through all the children of detailsBox after the type picker. Luckily, only the scheduleBox is a VBox.
		while((placeAndTimeElement = detailsBox.getChildren().get(detailsBox.getChildren().indexOf(type) + i)).getClass()==VBox.class)	
		{
			if(i==1)		// If we have at least one scheduleBox, create a new list.
				placeAndTimes = new ArrayList<Activity.PlaceAndTime>();
			
			PlaceAndTimeUtil placeAndTimeData =(PlaceAndTimeUtil) ((VBox) placeAndTimeElement).getUserData();	// The scheduleBox's user data contains the pretty PlaceAndTime object we need.
			placeAndTimeData.getData(); 
			placeAndTimes.add(placeAndTimeData.placeAndTime);	// Not very pretty...public class variable, so we can do this, but we should use setters and getters.
			
			i++;
		}
			
		return placeAndTimes;
	}

	// Used to edit existing activities. Sets the data.
	public void setData(Activity act)
	{
		this.actId = act.getActId();
		
		this.name.setText(act.getName());
		this.desc.setText(act.getDescription());
		
		if(act.getPlaceAndTimes() != null)	// Creates all scheduleBoxes if they are needed.
		{
			for(PlaceAndTime placeAndTime : act.getPlaceAndTimes())	
			{
				VBox scheduleBox =PlaceAndTimeUtil.createScheduleBox(PlaceAndTimeUtil.getScheduleId(placeAndTime), detailsBox, placeAndTime);
				detailsBox.getChildren().add(detailsBox.getChildren().indexOf(newEntryMenuBtn), scheduleBox);
			}
		}
		
		this.intType = act.getIntType();
		switch(act.getIntType())		// "Translates" the numbers stored in the database to human understandable types.
		{
			case 0:
				this.type.setValue("Obligatory");
				break;
			case 1:
				this.type.setValue("Health");
				break;
			case 2:
				this.type.setValue("Self-Improvement");
				break;
			case 3:
				this.type.setValue("Recreational");
				break;
			case 4:
				this.type.setValue("Charity");
				break;
			case 5:
				this.type.setValue("Creative");
				break;
		}
	}
}
