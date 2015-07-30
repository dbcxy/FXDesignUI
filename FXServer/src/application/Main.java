package application;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import config.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	static final Logger logger = Logger.getLogger(Main.class);
	
	FXMLLoader fxmlLoader = new FXMLLoader();
	
	@Override
	public void start(Stage primaryStage) {
		initUI(primaryStage);
	}

	public static void main(String[] args) {
		loadLoggerSettings();
		launch(args);
	}
	
	private static void loadLoggerSettings() {
		PropertyConfigurator.configure("src/assets/log4j.properties");
		logger.info("SERVER LAUNCH");
	}
		
	private void initUI(Stage primaryStage) {
		try {	
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.load(getClass().getResourceAsStream("ServerUI.fxml"));
			AppConfig.getInstance().setController((ServerUIController) fxmlLoader.getController());
			Parent root = (Parent) fxmlLoader.getRoot();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	        
			primaryStage.initStyle(StageStyle.UNDECORATED);
	        primaryStage.setResizable(false);
	        primaryStage.setScene(scene);
	        primaryStage.show();   
			
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
}
