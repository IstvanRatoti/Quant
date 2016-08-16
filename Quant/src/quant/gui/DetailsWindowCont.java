package quant.gui;

import java.sql.Time;
import java.sql.Timestamp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import quant.connector.Query;
import quant.core.Activity;
import quant.main.Main;

public class DetailsWindowCont
{
	private int intType = 0;	// With this variable, we don't need to change the types back to numbers.
	private int actId = -2;		// Need this so we know which activity we edited, or if we created a new one. We use -2 because -1 means the activity is not in the database yet.
	
	@FXML
	public AnchorPane ap;	// Just here so we can access the stage through it.
	@FXML
	public TextField name = new TextField();
	@FXML
	public TextField place = new TextField();
	@FXML
	public ChoiceBox<String> type = new ChoiceBox<String>();
	@FXML
	public TextArea desc = new TextArea();
	@FXML
	public DatePicker date = new DatePicker();
	@FXML
	public Button acceptBtn;
	@FXML
	public Button cancelBtn;
	
	@FXML
	private void initialize()
	{
		this.type.setItems(FXCollections.observableArrayList("Obligatory", "Health", "Self-Improvement", "Recreational", "Charity", "Creative"));
		
		acceptBtn.setOnAction(e -> {	// Saves the data and closes the window.
										Activity actToStore = null;
										
										if(actId == -2)	// Creates a new activity and saves it.
										{
											actToStore = new Activity 	(
																			name.getText(), 
																			desc.getText(), 
																			this.intType, 
																			place.getText(), 
																			Timestamp.valueOf(date.getValue().atStartOfDay()), 
																			new Time(0)
																		);
											Pair<SimpleStringProperty, Activity> newPair = new Pair<SimpleStringProperty, Activity>(new SimpleStringProperty(actToStore.getName()), actToStore);
											Main.appData.add(newPair);
											
											((MainWindowCont) ap.getScene().getUserData()).addActBox(newPair);
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
													actToStore.getPlaceAndTimes().get(0).setDuration(new Time(0));
													actToStore.getPlaceAndTimes().get(0).setPlace(place.getText());
													actToStore.getPlaceAndTimes().get(0).setTimeAndDate(Timestamp.valueOf(date.getValue().atStartOfDay()));
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
										
										Stage stage = (Stage) ap.getScene().getWindow();
										stage.close();
									});
		
		cancelBtn.setOnAction(e -> {	// Closes the window, doesn't save anything.
			
										
										Stage stage = (Stage) ap.getScene().getWindow();
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
	}
	
	// Used to edit existing activities. Sets the data.
	public void setData(Activity act)
	{
		this.actId = act.getActId();
		
		this.name.setText(act.getName());
		this.desc.setText(act.getDescription());
		
		this.place.setText(act.getPlaceAndTimes().get(0).getPlace());
		this.date.setValue(act.getPlaceAndTimes().get(0).getTimeAndDate().toLocalDateTime().toLocalDate());
		
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
