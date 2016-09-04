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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import quant.connector.Query;
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
		if(Main.connection == null)
			addBtn.setDisable(true);
		
		actListScroll.setFitToWidth(true);
		actListScroll.setContent(actList);
		actListScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);	// Always shows the scroll bar, so it wont look slly when it appears.
		
		if( Main.appData != null)
		{
			for(Pair<SimpleStringProperty, Activity> actPair : Main.appData)	// Adds the activities from the database.
				addActBox(actPair);
		}
		
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
									
									detailsStage.initModality(Modality.WINDOW_MODAL);
									detailsStage.initOwner(addBtn.getScene().getWindow());

									detailsStage.show();
								});
		
		optionsBtn.setOnAction(e -> {
										FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/quant/gui/SettingsWindow.fxml"));
			
										Parent settingsWindow = null;
										try
										{
											settingsWindow = loader.load();
										}
										catch (Exception e1)
										{
											e1.printStackTrace();
										}

										Scene settingsScene = new Scene(settingsWindow);
										settingsScene.setUserData(this);

										Stage settingsStage = new Stage();

										settingsStage.setTitle("Options");
										settingsStage.setScene(settingsScene);
										
										settingsStage.initModality(Modality.WINDOW_MODAL);
										settingsStage.initOwner(optionsBtn.getScene().getWindow());
										
										settingsStage.showAndWait();
									});
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
											
											detailsStage.initModality(Modality.WINDOW_MODAL);		// These 2 make it impossible to choose the main window when
											detailsStage.initOwner(addBtn.getScene().getWindow());	// the details window is shown.
											
											detailsStage.show();
										});
		
		Label actName = new Label();
		HBox.setMargin(actName, new Insets(10, 0, 0, 0));
		actName.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(actName, Priority.ALWAYS);
		actName.setAlignment(Pos.BASELINE_LEFT);
		actName.textProperty().bind(actPair.getKey());	//Binding, so we can modify it from outside.
		
		Button actRemoveBtn = new Button("Remove");		// Button to remove the activity.
		HBox.setMargin(actRemoveBtn, new Insets(5, 5, 5, 5));
		actRemoveBtn.setAlignment(Pos.BASELINE_LEFT);
		actRemoveBtn.setOnAction(e -> 	{
											actList.getChildren().remove(actBox);
											
											// This function should remove the activity from the appData too. But, we can't access it anyhow...interesting dilemma.
											
											try
											{	// Removes the activity from the database.
												Query.deleteData(act, Main.connection);
											} 
											catch (Exception e1)
											{
												e1.printStackTrace();
											}
										});
		
		actBox.getChildren().addAll(actDetailsBtn, actName,actRemoveBtn);
		
		actList.getChildren().add(actBox);
	}
	
}
