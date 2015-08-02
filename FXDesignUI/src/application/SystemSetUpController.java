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

public class SystemSetUpController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(DisplaySetUpController.class);
	
	@FXML AnchorPane SystemSetup;
	
	@FXML ComboBox<String> comboRunway;
	@FXML ComboBox<String> comboRangeUnits;
	@FXML ComboBox<String> comboElAz;
	
	@FXML TextField groupIP;
	@FXML TextField portAzPlot;
	@FXML TextField portAzTrack;
	@FXML TextField portElPlot;
	@FXML TextField portElTrack;
	@FXML TextField portVideo;
	@FXML TextField portSend;
	
	@FXML Label statusPort;
	
	AppConfig appConfig = AppConfig.getInstance();
	
	double initialX,initialY;

	@Override
	public void draw(GraphicsContext gc) {
		
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		logger.info("System Setup Dialog Opened");
		addDraggableNode(SystemSetup);
		initDialog();
		loadDefault();		
	}	

	@FXML
	protected void okClick(ActionEvent event) {
		appConfig.validateSystemSetupData();
		if(appConfig.isSystemSetupValid()) {
			saveData();
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
		
		comboRangeUnits.getItems().clear();
		comboRangeUnits.getItems().addAll("Kilometers (KM)","Nauticalmiles (NM)");
		
		comboElAz.getItems().clear();
		comboElAz.getItems().addAll("Feet (ft)","Meters (mts)");
		
		groupIP.setEditable(!Constance.IS_CONNECTED);
		portAzPlot.setEditable(!Constance.IS_CONNECTED);
		portAzTrack.setEditable(!Constance.IS_CONNECTED);
		portElPlot.setEditable(!Constance.IS_CONNECTED);
		portElTrack.setEditable(!Constance.IS_CONNECTED);
		portVideo.setEditable(!Constance.IS_CONNECTED);
		portSend.setEditable(!Constance.IS_CONNECTED);
		
		statusPort.setText("Port Number Changes Disabled: Connection Active");
		if(!Constance.IS_CONNECTED)
			statusPort.setText("Port Number Changes Enabled: Connection Inactive");
		
	}

	private void loadDefault() {
		comboRunway.setValue(Constance.PREF.SEL_RUNWAY);
		comboRangeUnits.setValue(Constance.PREF.RANGE_UNITS);	
		comboElAz.setValue(Constance.PREF.EL_AZ_UNITS);
		
		groupIP.setText(Constance.GROUP_ADDR);
		portAzPlot.setText(String.valueOf(Constance.PORT_AZ_PLOTS));
		portAzTrack.setText(String.valueOf(Constance.PORT_AZ_TRACKS));
		portElPlot.setText(String.valueOf(Constance.PORT_EL_PLOTS));
		portElTrack.setText(String.valueOf(Constance.PORT_EL_TRACKS));
		portVideo.setText(String.valueOf(Constance.PORT_VIDEO));
		portSend.setText(String.valueOf(Constance.PORT_WRITE));
	}

	private void saveData() {
		Constance.PREF.SEL_RUNWAY = comboRunway.getValue();
		Constance.PREF.RANGE_UNITS = comboRangeUnits.getValue();
		Constance.PREF.EL_AZ_UNITS = comboElAz.getValue();
		
		if(Utils.checkIPv4(groupIP.getText()))
			Constance.GROUP_ADDR =groupIP.getText();
		else
			AppConfig.getInstance().openErrorDialog("GROUP_ADDR: Invalid");
		Constance.PORT_AZ_PLOTS = AppConfig.getInstance().validateIntegerData(Constance.PORT_AZ_PLOTS, portAzPlot.getText(), 0, 15000, "PORT_AZ_PLOTS", "");
		Constance.PORT_AZ_TRACKS = AppConfig.getInstance().validateIntegerData(Constance.PORT_AZ_TRACKS, portAzTrack.getText(), 0, 15000, "PORT_AZ_TRACKS", "");
		Constance.PORT_EL_PLOTS = AppConfig.getInstance().validateIntegerData(Constance.PORT_EL_PLOTS, portElPlot.getText(), 0, 15000, "PORT_EL_PLOTS", "");
		Constance.PORT_EL_TRACKS = AppConfig.getInstance().validateIntegerData(Constance.PORT_EL_TRACKS, portElTrack.getText(), 0, 15000, "PORT_EL_TRACKS", "");
		Constance.PORT_VIDEO = AppConfig.getInstance().validateIntegerData(Constance.PORT_VIDEO, portVideo.getText(), 0, 15000, "PORT_VIDEO", "");
		Constance.PORT_WRITE = AppConfig.getInstance().validateIntegerData(Constance.PORT_WRITE, portSend.getText(), 0, 15000, "PORT_WRITE", "");
		
		Constance.UNITS.isKM = Constance.PREF.RANGE_UNITS.contains("KM");
		Constance.UNITS.isFPS = Constance.PREF.EL_AZ_UNITS.contains("ft");
		
	}

	private void closeSettings(ActionEvent event) {
		Node  source = (Node)  event.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
	    logger.info("System Setup Dialog Closed");
	}


}
