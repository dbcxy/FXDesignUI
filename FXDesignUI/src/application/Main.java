package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	IControlManager mICManager;
	FXMLController mFXMLController;
	
	@Override
	public void start(Stage primaryStage) {
		
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
	
	public static void main(String[] args) {
		launch(args);
	}
}
