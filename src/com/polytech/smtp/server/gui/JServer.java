package com.polytech.smtp.server.gui;

import java.io.IOException;


import com.polytech.smtp.server.Server;
import com.polytech.smtp.utils.observer.Observable;
import com.polytech.smtp.utils.observer.Observer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JServer extends Application {
	private Stage primaryStage;
    private BorderPane rootLayout;
    private int port = 1024;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Serveur SMTP");
        
        showPersonOverview();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void showPersonOverview() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JServer.class.getResource("ServerOverview.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            Controller controller = loader.getController();
            TextArea t = controller.getTextArea();
            
            Server server = new Server(port);
    		server.addObserver(new Observer(){
    			@Override
    			public void update(Observable observable, Object o)
    			{
    				System.out.println(o);
    			}
    		});
    		controller.setServer(server);
    		
    		BooleanBinding portValid = Bindings.createBooleanBinding(() -> {
    			String text = controller.getFieldPort().getText();
    			if(text.equals(""))
    				return false;
    			else
    			{
    			    return text.matches("\\d*");
    			}
    		}, controller.getFieldPort().textProperty());
    		
    		BooleanBinding dirValid = Bindings.createBooleanBinding(() -> {
    			String text = controller.getFieldDir().getText();
    			return !text.equals("");
    		}, controller.getFieldDir().textProperty());
    		
    		BooleanBinding nameValid = Bindings.createBooleanBinding(() -> {
    			String text = controller.getFieldName().getText();
    			return !text.equals("");
    		}, controller.getFieldName().textProperty());
    		
    		BooleanProperty runningValid = new SimpleBooleanProperty();
    		
    		controller.getButtonStart().disableProperty().bind(runningValid.or(dirValid.not().or(portValid.not().or(nameValid).not())));
    		controller.getButtonStop().disableProperty().bind(runningValid.not());
    		controller.getFieldDir().disableProperty().bind(runningValid);
    		controller.getFieldPort().disableProperty().bind(runningValid);
    		controller.getFieldName().disableProperty().bind(runningValid);
    		controller.getButtonDir().disableProperty().bind(runningValid);
    		
    		Observer o = new Observer(){

				@Override
				public void update(Observable observable, Object o)
				{
					Platform.runLater(new Runnable(){

						@Override
						public void run()
						{
							if(o instanceof String)
								t.appendText((String)o);
							
							controller.getFieldDir().setText(server.getRootDir().getAbsolutePath());
							if(controller.getFieldPort().getText().equals(""))
								controller.getFieldPort().setText(server.getPort()+"");
							if(controller.getFieldName().getText().equals(""))
								controller.getFieldName().setText(server.getServerName());
							
							runningValid.set(server.isRunning());
							
							if(server.isRunning())
							{
								controller.getStatusLabel().setText("Running");
								controller.getStatusLabel().setTextFill(Color.GREEN);
							}
							else
							{
								controller.getStatusLabel().setText("Stopped");
								controller.getStatusLabel().setTextFill(Color.RED);
							}
						}
						
					});
				}
    		};
    		server.addObserver(o);
    		server.notifyObservers(null);
    		
    		controller.getButtonDir().setOnAction(new EventHandler<ActionEvent>()
    		{
				@Override
				public void handle(ActionEvent arg0)
				{
					controller.setDir(primaryStage);
				}
    		});
    		
    		controller.getButtonStart().setOnAction(new EventHandler<ActionEvent>()
    		{
				@Override
				public void handle(ActionEvent arg0)
				{
					controller.startServer();
				}
    		});
    		
    		controller.getButtonStop().setOnAction(new EventHandler<ActionEvent>()
    		{
				@Override
				public void handle(ActionEvent arg0)
				{
					controller.stopServer();
				}
    		});
    		
    		
    		controller.getFieldPort().textProperty().addListener(new ChangeListener<String>() {
    		    @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    		        if (!newValue.matches("\\d*")) {
    		        	controller.getFieldPort().setText(oldValue);
    		        }
    		        else
    		        {
    		        	controller.setPort(newValue);
    		        }
    		    }
    		});
    		
    		controller.getFieldName().textProperty().addListener(new ChangeListener<String>() {
    		    @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    		        controller.setName(newValue);
    		    }
    		});
    		
    		
    		
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
}
