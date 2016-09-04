package quant.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import quant.connector.DBConnection;
import quant.connector.Query;
import quant.core.Activity;
import quant.main.Main;

public class SettingsWindowCont
{
	@FXML
	public AnchorPane ap;
	@FXML
	public TextField server;
	@FXML
	public TextField username;
	@FXML
	public PasswordField password;
	
	@FXML
	public Button okBtn;
	@FXML
	public Button cancelBtn;
	
	@FXML
	protected void initialize()
	{	
		List<String> credentials = null;
		
		try
		{
			credentials = getCredentials();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		if (credentials != null)
		{
			this.server.setText(credentials.get(0));
			this.username.setText(credentials.get(1));
			this.password.setText(credentials.get(2));
		}
		
		okBtn.setOnAction(e -> {	// Closes the window and resets the connection.
									String serverString = "jdbc:mysql://" + server.getText() + ":3306/quantdb";
									String connectionError = null;
										
									try		// Initialize data before we launch the app.
									{
										Main.connection = new DBConnection(serverString, username.getText(), password.getText());
									}
									catch (SQLException e2)
									{
										connectionError = e2.toString();
									}
									
									if(connectionError != null)
									{	
										Alert connectionAlert = new Alert(AlertType.ERROR);
										connectionAlert.setTitle("Database connection error!");
										connectionAlert.setHeaderText(null);
										connectionAlert.setContentText("There was a problem with connecting to database:\n" + connectionError + "\nPlease verify the server information you provided.");
										
										connectionAlert.showAndWait();
									}
									else
									{
										if(Main.appData == null)
										{
											Query query = null;
											List<Activity> list = null;
											
											try
											{
												query = new Query("SELECT * FROM activities LEFT JOIN timetable ON activities.id = timetable.actId", Main.connection, false);
												list = query.getData();
											}
											catch (Exception e1)											
											{
												e1.printStackTrace();
											}
											
											Main.appData = new ArrayList<Pair<SimpleStringProperty, Activity>>();
										
											for(Activity act : list)
											{
												Pair<SimpleStringProperty, Activity> newAct = new Pair<SimpleStringProperty, Activity>(new SimpleStringProperty(act.getName()), act);
											
												Main.appData.add(newAct);
												((MainWindowCont) ap.getScene().getUserData()).addActBox(newAct);
												((MainWindowCont) ap.getScene().getUserData()).addBtn.setDisable(false);
											}
										}
										
										try
										{
											setCredentials(server.getText(), username.getText(), password.getText());
										}
										catch (Exception e1)
										{
											e1.printStackTrace();
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
	
	public static List<String> getCredentials() throws IOException
	{
		List<String> credentials = null;
		
		InputStream inputFile = null;
		BufferedReader buffer = null;
		
		try
		{
			inputFile = new FileInputStream("credentials.dat");
			InputStreamReader inputStream = new InputStreamReader(inputFile);
			buffer = new BufferedReader(inputStream);
		}
		catch (FileNotFoundException noFileEx)
		{
			return null;
		}
		
		credentials = new ArrayList<String>();
		
		String word = buffer.readLine();
		
		while (word != null)
		{
			credentials.add(word);
			word = buffer.readLine();
		}
		
		inputFile.close();
		
		return credentials;
	}
	
	public void setCredentials(String server, String username, String password) throws IOException
	{
		FileOutputStream outputFile = null;
		BufferedWriter buffer = null;
		
		try
		{
			outputFile = new FileOutputStream("credentials.dat");
			OutputStreamWriter outputStream = new OutputStreamWriter(outputFile);
			buffer = new BufferedWriter(outputStream);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return;
		}
		
		buffer.write(server);
		buffer.newLine();
		buffer.write(username);
		buffer.newLine();
		buffer.write(password);
		
		buffer.flush();
		
		outputFile.close();
	}
}
