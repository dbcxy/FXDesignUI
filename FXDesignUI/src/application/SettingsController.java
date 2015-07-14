package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import model.graph.ILayoutParam;

import org.apache.log4j.Logger;

public class SettingsController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(SettingsController.class);

	
	
	@Override
	public void draw(GraphicsContext gc) {
		
	}

	@Override
	public void initialize(URL url, ResourceBundle rBundle) {
		logger.info("Settings Dialog Opened");
	}
	
	@FXML
	protected void okClick(ActionEvent event) {
		
	}
	
	@FXML
	protected void cancelClick(ActionEvent event) {
		Node  source = (Node)  event.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
	}
}
