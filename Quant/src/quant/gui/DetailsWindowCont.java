package quant.gui;

import java.sql.Time;
import java.sql.Timestamp;

import javafx.beans.property.SimpleStringProperty;
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
										if(actId == -2)	// Creates a new activity and saves it.
										{
											Activity newAct = new Activity	(
																				name.getText(), 
																				desc.getText(), 
																				this.intType, 
																				place.getText(), 
																				Timestamp.valueOf(date.getValue().atStartOfDay()), 
																				new Time(0)
																			);
											Pair<SimpleStringProperty, Activity> newPair = new Pair<SimpleStringProperty, Activity>(new SimpleStringProperty(newAct.getName()), newAct);
											Main.appData.add(newPair);
											
											((MainWindowCont) ap.getScene().getUserData()).addActBox(newPair);
											Stage stage = (Stage) ap.getScene().getWindow();
											stage.close();
										}
										else	// Updates an existing activity.
										{
											for(Pair<SimpleStringProperty, Activity> actPair : Main.appData)
											{
												Activity act = actPair.getValue();
												
												if(actId == act.getActId())
												{
													actPair.getKey().set(name.getText());
													
													act.setName(name.getText());
													act.setDescription(desc.getText());
													act.setType(this.intType);
													act.getPlaceAndTimes().get(0).setDuration(new Time(0));
													act.getPlaceAndTimes().get(0).setPlace(place.getText());
													act.getPlaceAndTimes().get(0).setTimeAndDate(Timestamp.valueOf(date.getValue().atStartOfDay()));
													break;
												}
											}

											Stage stage = (Stage) ap.getScene().getWindow();
											stage.close();
										}
									});
		
		cancelBtn.setOnAction(e -> {	// Closes the window, doesn't save anything.
										Stage stage = (Stage) ap.getScene().getWindow();
										stage.close();
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
