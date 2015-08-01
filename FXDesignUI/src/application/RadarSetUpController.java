package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.AppConfig;
import model.graph.ILayoutParam;

import org.apache.log4j.Logger;

import utils.Constance;

public class RadarSetUpController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(DisplaySetUpController.class);
	
	@FXML AnchorPane RadarSetup;
	
	@FXML ComboBox<String> comboModes;
	@FXML ComboBox<String> comboPolarization;
	
	@FXML TextField freqSel;
	@FXML TextField bite;
	
	AppConfig appConfig = AppConfig.getInstance();
	
	double initialX,initialY;

	@Override
	public void draw(GraphicsContext gc) {
		
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		logger.info("Radar Setup Dialog Opened");
		addDraggableNode(RadarSetup);
		initDialog();
		loadDefaultPreference();		
	}	

	@FXML
	protected void okClick(ActionEvent event) {
		saveData();
		appConfig.saveRadarSetupData();
		if(appConfig.isRadarSetupSaved()) {
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
		comboModes.getItems().clear();
		comboModes.getItems().addAll("Clear Weather","Moderate Rain","Heavy Rain");
		
		comboPolarization.getItems().clear();
		comboPolarization.getItems().addAll("Horizontal","Vertical","Circular");
		
	}

	private void loadDefaultPreference() {
		comboModes.setValue(Constance.PREF.MODE_OP);
		comboPolarization.setValue(Constance.PREF.CHG_POL);
		freqSel.setText(Constance.PREF.FREQ_SEL);
		bite.setText(Constance.PREF.BITE);
	}

	private void saveData() {
		Constance.PREF.MODE_OP = comboModes.getValue();
		Constance.PREF.CHG_POL = comboPolarization.getValue();
		Constance.PREF.FREQ_SEL = freqSel.getText();
		Constance.PREF.BITE = bite.getText();		
	}

	private void closeSettings(ActionEvent event) {
		Node  source = (Node)  event.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
	    logger.info("Radar Setup Dialog Closed");
	}


}
