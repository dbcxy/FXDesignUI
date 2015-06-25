package application;
	
import model.DataObserver;
import network.IControlManager;
import network.TaskObserver;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Master extends Application {
	
	private static final Logger logger = Logger.getLogger(Master.class);
	
	IControlManager mICManager;
	FXMLController mFXMLController;
	
	@Override
	public void start(Stage primaryStage) {
		initStage(primaryStage);
//		startNetworkTask();
	}
	
	public static void main(String[] args) {
		loadLoggerSettings();
		launch(args);
	}
	
	private static void loadLoggerSettings() {
		PropertyConfigurator.configure("src/assets/log4j.properties");
		logger.info("Launch");
	}
	
	private void initStage(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.load(getClass().getResourceAsStream("Activity.fxml"));
			Parent root = (Parent) fxmlLoader.getRoot();
			mFXMLController = (FXMLController) fxmlLoader.getController();
			Scene scene = new Scene(root);
	        
			primaryStage.initStyle(StageStyle.UNDECORATED);
	        primaryStage.setMaximized(true);
	        primaryStage.setResizable(false);
	        primaryStage.setScene(scene);
	        primaryStage.show();   
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	private void startNetworkTask() {
//		mTask = new TaskObserver(new IControlManager() {
//			
//			@Override
//			public void manageData(DataObserver mDataObserver) {
//				mFXMLController.refreshCanvas(mDataObserver);
//				logger.info("Setting Data Observer");
//			}
//		});
//		tTask = new Thread(mTask);
//		tTask.start();
//	}
}
