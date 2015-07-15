package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.graph.ILayoutParam;

import org.apache.log4j.Logger;

public class PreferencesController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(SettingsController.class);
	
	@FXML ComboBox<String> comboModes;
	@FXML ComboBox<String> comboPolarization;
	@FXML ComboBox<String> comboRangeUnits;
	@FXML ComboBox<String> comboElAz;

	@Override
	public void draw(GraphicsContext gc) {
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		logger.info("Preferences Dialog Opened");
		
		comboModes.getItems().clear();
		comboModes.getItems().addAll("Clear Weather","Moderate Rain","Heavy Rain");
		
		comboPolarization.getItems().clear();
		comboPolarization.getItems().addAll("Horizontal","Vertical","Circular");
		
		comboRangeUnits.getItems().clear();
		comboRangeUnits.getItems().addAll("KM","NM");
		
		comboElAz.getItems().clear();
		comboElAz.getItems().addAll("Feet (ft)","Meters (mts)");
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
