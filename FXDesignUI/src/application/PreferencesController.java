package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.AppConfig;
import model.graph.ILayoutParam;

import org.apache.log4j.Logger;

import utils.Constance;

public class PreferencesController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(SettingsController.class);
	
	@FXML AnchorPane Preference;
	
	@FXML ComboBox<String> comboRunway;
	@FXML ComboBox<String> comboModes;
	@FXML ComboBox<String> comboPolarization;
	@FXML ComboBox<String> comboRangeUnits;
	@FXML ComboBox<String> comboElAz;
	
	@FXML TextField freqSel;
	@FXML TextField bite;
	
	AppConfig appConfig = AppConfig.getInstance();
	
	double initialX,initialY;

	@Override
	public void draw(GraphicsContext gc) {
		
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		logger.info("Preferences Dialog Opened");
		addDraggableNode(Preference);
		initDialog();
		loadDefaultPreference();		
	}	

	@FXML
	protected void okClick(ActionEvent event) {
		saveData();
		appConfig.savePreferenceData();
		if(appConfig.isPreferenceSaved()) {
			closeSettings(event);
			AppConfig.getInstance().getFxmlController().notifyChanges();
		}
	}
	
	@FXML
	protected void cancelClick(ActionEvent event) {
		closeSettings(event);
	}
	
	private void addDraggableNode(final Node node) {

	    node.setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent me) {
	            if (me.getButton() != MouseButton.MIDDLE) {
	                initialX = me.getSceneX();
	                initialY = me.getSceneY();
	            }
	        }
	    });

	    node.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent me) {
	            if (me.getButton() != MouseButton.MIDDLE) {
	                node.getScene().getWindow().setX(me.getScreenX() - initialX);
	                node.getScene().getWindow().setY(me.getScreenY() - initialY);
	            }
	        }
	    });
	}
	
	private void initDialog() {
		comboRunway.getItems().clear();
		comboRunway.getItems().addAll(" 1 "," 2 "," 3 "," 4 ");
		
		comboModes.getItems().clear();
		comboModes.getItems().addAll("Clear Weather","Moderate Rain","Heavy Rain");
		
		comboPolarization.getItems().clear();
		comboPolarization.getItems().addAll("Horizontal","Vertical","Circular");
		
		comboRangeUnits.getItems().clear();
		comboRangeUnits.getItems().addAll("Kilometers (KM)","Nauticalmiles (NM)");
		
		comboElAz.getItems().clear();
		comboElAz.getItems().addAll("Feet (ft)","Meters (mts)");
		
	}

	private void loadDefaultPreference() {
		comboRunway.setValue(Constance.PREF.SEL_RUNWAY);
		comboModes.setValue(Constance.PREF.MODE_OP);
		comboPolarization.setValue(Constance.PREF.CHG_POL);
		comboRangeUnits.setValue(Constance.PREF.RANGE_UNITS);	
		comboElAz.setValue(Constance.PREF.EL_AZ_UNITS);
		freqSel.setText(Constance.PREF.FREQ_SEL);
		bite.setText(Constance.PREF.BITE);
	}

	private void saveData() {
		Constance.PREF.SEL_RUNWAY = comboRunway.getValue();
		Constance.PREF.MODE_OP = comboModes.getValue();
		Constance.PREF.CHG_POL = comboPolarization.getValue();
		Constance.PREF.RANGE_UNITS = comboRangeUnits.getValue();
		Constance.PREF.EL_AZ_UNITS = comboElAz.getValue();
		Constance.PREF.FREQ_SEL = freqSel.getText();
		Constance.PREF.BITE = bite.getText();
		
		Constance.UNITS.isKM = Constance.PREF.RANGE_UNITS.contains("KM");
		Constance.UNITS.isFPS = Constance.PREF.EL_AZ_UNITS.contains("ft");
		
	}

	private void closeSettings(ActionEvent event) {
		Node  source = (Node)  event.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
	    logger.info("Preference Dialog Closed");
	}


}
