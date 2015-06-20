package application;
	
import network.IControlManager;
import network.TaskObserver;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Master extends Application {
	
	private static final Logger logger = Logger.getLogger(Master.class);
	
	IControlManager mICManager;
	FXMLController mFXMLController;
	Thread mTask;
	
	@Override
	public void start(Stage primaryStage) {
		initStage(primaryStage);
	}
	
	public static void main(String[] args) {
		loadLoggerSettings();
		launch(args);
	}
	
	private static void loadLoggerSettings() {
		PropertyConfigurator.configure("res/log4j.properties");
		logger.info("Launch");
	}
	
	private void initStage(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Activity.fxml"));
			mFXMLController = new FXMLController();
	        Scene scene = new Scene(root);
	        
	        primaryStage.setTitle("FX Display Welcome");
	        primaryStage.setResizable(false);
	        primaryStage.setScene(scene);
	        primaryStage.show();	        
	        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            
	        	public void handle(WindowEvent we) {
	        		mFXMLController.finish();
	            }
	        });	        
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void startNetworkTask() {
		mTask = new Thread(new TaskObserver(new IControlManager() {
			
			@Override
			public void manageData() {
				//Call FXML Controller public methods
			}
		}));
		mTask.start();
	}
}
