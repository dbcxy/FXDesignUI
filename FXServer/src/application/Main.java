package application;
	
import network.C2Server;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	static final Logger logger = Logger.getLogger(Main.class);
	
	FXMLLoader fxmlLoader = new FXMLLoader();
	
	@Override
	public void start(Stage primaryStage) {
		startNetworkTask();
//		initUI(primaryStage);
	}

	public static void main(String[] args) {
		loadLoggerSettings();
		launch(args);
	}
	
	private static void loadLoggerSettings() {
		PropertyConfigurator.configure("src/assets/log4j.properties");
		logger.info("APP LAUNCH");
	}
	
	private void startNetworkTask() {
		C2Server c2Server = new C2Server();
		c2Server.startMCUDPThreadServer();
		
	}
		
	private void initUI(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
}
