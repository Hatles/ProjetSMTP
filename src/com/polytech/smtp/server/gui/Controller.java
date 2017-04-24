package com.polytech.smtp.server.gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


import com.polytech.smtp.server.Server;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller implements Initializable
{
	private Server server;
	
	//@FXML private Button myButton;
	@FXML private TextArea textArea;
	@FXML private TextField fieldDir;
	@FXML private TextField fieldPort;
	@FXML private TextField fieldName;
	@FXML private Button buttonDir;
	@FXML private Button buttonStart;
	@FXML private Button buttonStop;
	@FXML private Label labelStatus;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		//assert myButton != null : "fx:id=\"myButton\" was not injected: check your FXML file 'simple.fxml'.";
		assert textArea != null : "fx:id=\"textTimer\" was not injected: check your FXML file 'simple.fxml'.";
	}

	public TextArea getTextArea()
	{
		return textArea;
	}

	public TextField getFieldDir()
	{
		return fieldDir;
	}
	
	public TextField getFieldName()
	{
		return fieldName;
	}

	public TextField getFieldPort()
	{
		return fieldPort;
	}

	public Button getButtonDir()
	{
		return buttonDir;
	}

	public Button getButtonStart()
	{
		return buttonStart;
	}

	public Button getButtonStop()
	{
		return buttonStop;
	}
	
	public Label getStatusLabel()
	{
		return this.labelStatus;
	}
	
	public void setServer(Server s)
	{
		this.server = s;
	}
	
	public void setPort(String newValue)
	{
		try {
		      int port = Integer.parseInt(newValue);
		      server.setPort(port);
		} catch (NumberFormatException e) {
		      server.setPort(0);
		}
	}
	
	public void startServer()
	{
		server.start();
	}
	
	public void setDir(Stage primaryStage)
	{
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choose messages file");
		File defaultDirectory = server.getRootDir();
		chooser.setInitialDirectory(defaultDirectory);
		File selectedFile = chooser.showOpenDialog(primaryStage);
		
		if(selectedFile != null)
			server.setRootDir(selectedFile);
	}

	public void stopServer()
	{
		if(server.isRunning())
			server.stop();
	}
	
	public void setName(String name)
	{
		if(name != null && !name.equals(""))
		{
			server.setName(name);
		}
	}
}
