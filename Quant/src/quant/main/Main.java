package quant.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import quant.connector.DBConnection;
import quant.connector.Query;
import quant.core.Activity;
import quant.gui.SettingsWindowCont;

public class Main extends Application
{
	public static DBConnection connection = null;
	public static List<Pair<SimpleStringProperty, Activity>> appData = null;			// Pair is used so we can bind the activity name we modify in the details window to the one in the main.
	public static SimpleBooleanProperty hasData = new SimpleBooleanProperty(false);

	@Override
	public void start(Stage primaryStage) throws IOException	// Standard main window initializer.
	{
		Parent mainWindow = FXMLLoader.load(getClass().getResource("/quant/gui/MainWindow.fxml"));
		Scene mainScene = new Scene(mainWindow);
        primaryStage.setTitle("Quant");
        primaryStage.setScene(mainScene);
        primaryStage.show();
	}

	public static void main(String[] args)
	{
		List<String> credentials = null;
		
		try
		{
			credentials = SettingsWindowCont.getCredentials();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			launch(args);
		}
		
		if(credentials != null)
		{
			try		// Initialize data before we launch the app, if we have credentials.
			{
				connection = new DBConnection("jdbc:mysql://" + credentials.get(0) + ":3306/quantdb", credentials.get(1), credentials.get(2));
				Query query = new Query("SELECT * FROM activities LEFT JOIN timetable ON activities.id = timetable.actId", connection, false);
				List<Activity> list = query.getData();
				
				Main.appData = new ArrayList<Pair<SimpleStringProperty, Activity>>();
				
				for(Activity act : list)
				{
					appData.add(new Pair<SimpleStringProperty, Activity>(new SimpleStringProperty(act.getName()), act));
				}
			}
			catch (SQLException e){}
		}
		
		launch(args);
	}
}
