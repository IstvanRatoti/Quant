package quant.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import quant.core.Activity;
import quant.main.Main;

public class MainWindowCont
{
	@FXML
	public ScrollPane actListScroll;	// When too many activities are shown.
	@FXML
	public Button addBtn;
	@FXML
	public Button optionsBtn;
	@FXML
	public VBox actList;
	
	@FXML
	protected void initialize()
	{
		actListScroll.setFitToWidth(true);
		actListScroll.setContent(actList);
		actListScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);	// Always shows the scroll bar, so it wont look slly when it appears.
		
		for(Pair<SimpleStringProperty, Activity> actPair : Main.appData)	// Adds the activities from the database.
			addActBox(actPair);
		
		addBtn.setOnAction(e -> {	// Button action, opens a new details window for a new activity. Also passes the parent object for modifying.
									FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/quant/gui/DetailsWindow.fxml"));
			
									Parent detailsWindow = null;
									try
									{
										detailsWindow = loader.load();
									}
									catch (Exception e1)
									{
										e1.printStackTrace();
									}
			
									Scene detailsScene = new Scene(detailsWindow);
									detailsScene.setUserData(this);		// Data passing here.
			
									Stage detailsStage = new Stage();
			
									detailsStage.setTitle("New Activity");
									detailsStage.setScene(detailsScene);
									detailsStage.show();
								});
		
		optionsBtn.setOnAction(e -> System.out.println("Options windows dummy."));	// Dummy for options, to be implemented.
	}
	
	// Adds a new activity entry. Needed for adding new entries outside the initialization.
	public void addActBox(Pair<SimpleStringProperty, Activity> actPair)
	{
		Activity act = actPair.getValue();
		
		HBox actBox = new HBox();
		actBox.setId(new Integer(act.getActId()).toString());
		
		Button actDetailsBtn = new Button("Details");
		HBox.setMargin(actDetailsBtn, new Insets(5, 5, 5, 5));
		actDetailsBtn.setAlignment(Pos.BASELINE_LEFT);
		actDetailsBtn.setOnAction(e -> 	{	// Button action, opens a new details window for an existing activity. Sets the data for the new window.
											FXMLLoader loader = new FXMLLoader(getClass().getResource("/quant/gui/DetailsWindow.fxml"));
											
											Parent detailsWindow = null;
											try
											{
												detailsWindow = loader.load();
											}
											catch (Exception e1)
											{
												e1.printStackTrace();
											}
											
											DetailsWindowCont detailsWindowController = loader.getController();
											detailsWindowController.setData(act);	// Data for the new window.
											
											Scene detailsScene = new Scene(detailsWindow);
											
											Stage detailsStage = new Stage();
											
											detailsStage.setTitle("Activity Details");
											detailsStage.setScene(detailsScene);
											detailsStage.show();
										});
		
		Label actName = new Label();
		HBox.setMargin(actName, new Insets(10, 0, 0, 0));
		actName.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(actName, Priority.ALWAYS);
		actName.setAlignment(Pos.BASELINE_LEFT);
		actName.textProperty().bind(actPair.getKey());	//Binding, so we can modify it from outside.
		
		Button actRemoveBtn = new Button("Remove");		// Dummy button.
		HBox.setMargin(actRemoveBtn, new Insets(5, 5, 5, 5));
		actRemoveBtn.setAlignment(Pos.BASELINE_LEFT);
		actRemoveBtn.setOnAction(e -> actList.getChildren().remove(actBox));	// Simple remove Button.
		
		actBox.getChildren().addAll(actDetailsBtn, actName,actRemoveBtn);
		
		actList.getChildren().add(actBox);
	}
	
}