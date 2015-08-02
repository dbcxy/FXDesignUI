package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.AppConfig;
import model.graph.ILayoutParam;

import org.apache.log4j.Logger;

import utils.Constance;

public class DisplaySetUpController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(DisplaySetUpController.class);

	@FXML AnchorPane Settings;
	
	@FXML HBox MaximumElevation;
	@FXML HBox MaximumAzimuth;
	
	@FXML HBox ElevationUSL;
	@FXML HBox ElevationGlideAngle;
	@FXML HBox ElevationGlideMaxDist;
	@FXML HBox ElevationGlideFlatStartDist;
	@FXML HBox ElevationLSL;
	@FXML HBox ElevationUAL;
	@FXML HBox ElevationLAL;
	@FXML HBox ElevationUSaL;
	@FXML HBox ElevationLSaL;
	@FXML HBox ElevationDH;
	
	@FXML HBox AzimuthLSL;
	@FXML HBox AzimuthRSL;
	@FXML HBox AzimuthRCLO;
	@FXML HBox AzimuthLAL;
	@FXML HBox AzimuthRAL;
	@FXML HBox AzimuthLSaL;
	@FXML HBox AzimuthRSaL;

	AppConfig appConfig = AppConfig.getInstance();
	
	double initialX,initialY;
	
	@Override
	public void draw(GraphicsContext gc) {
		
	}

	@Override
	public void initialize(URL url, ResourceBundle rBundle) {
		logger.info("Settings Dialog Opened");
		addDraggableNode(Settings);
		loadToList();
		initDefaultValues();
		initUnits();
		
		Constance.IS_DISPLAY_SETUP = true;
	}
	
	@FXML
	protected void okClick(ActionEvent event) {
		appConfig.saveDisplaySetupData();
		if(appConfig.isDisplaySetupValid()) {
			closeSettings(event);
			AppConfig.getInstance().getFxmlController().notifyChanges();
		}
		
	}

	@FXML
	protected void cancelClick(ActionEvent event) {
		closeSettings(event);
	}

	private void closeSettings(ActionEvent event) {
		Node  source = (Node)  event.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
	    logger.info("Setting Dialog Closed");
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
	
	private void loadToList() {
		appConfig.getDisplaySetupList().clear();
		appConfig.addDisplaySetup(MaximumElevation);
		appConfig.addDisplaySetup(MaximumAzimuth);
		appConfig.addDisplaySetup(ElevationUSL);
		appConfig.addDisplaySetup(ElevationGlideAngle);
		appConfig.addDisplaySetup(ElevationGlideMaxDist);
		appConfig.addDisplaySetup(ElevationGlideFlatStartDist);
		appConfig.addDisplaySetup(ElevationLSL);
		appConfig.addDisplaySetup(ElevationUAL);
		appConfig.addDisplaySetup(ElevationLAL);
		appConfig.addDisplaySetup(ElevationUSaL);
		appConfig.addDisplaySetup(ElevationLSaL);
		appConfig.addDisplaySetup(ElevationDH);
		appConfig.addDisplaySetup(AzimuthLSL);
		appConfig.addDisplaySetup(AzimuthRSL);
		appConfig.addDisplaySetup(AzimuthRCLO);
		appConfig.addDisplaySetup(AzimuthLAL);
		appConfig.addDisplaySetup(AzimuthRAL);
		appConfig.addDisplaySetup(AzimuthLSaL);
		appConfig.addDisplaySetup(AzimuthRSaL);		
	}

	private void initDefaultValues() {
		
		//set Default label to NOTHING
		for(int i=0;i<appConfig.getDisplaySetupListSize();i++) {
			((Label)(appConfig.getDisplaySetupList().get(i).getChildren().get(3))).setText("");
		}
		
		//set TextField to their values
		for(int i=0;i<appConfig.getDisplaySetupListSize();i++) {
			((TextField)(appConfig.getDisplaySetupList().get(i).getChildren().get(1))).setText(String.valueOf(appConfig.getDisplaySetupListValue(i)));
		}
	}

	private void initUnits() {
		((Label) (MaximumElevation.getChildren().get(2))).setText(Constance.UNITS.getHEIGHT());
		
		((Label) (ElevationDH.getChildren().get(2))).setText(Constance.UNITS.getLENGTH());
		((Label) (ElevationGlideMaxDist.getChildren().get(2))).setText(Constance.UNITS.getLENGTH());
		((Label) (ElevationGlideFlatStartDist.getChildren().get(2))).setText(Constance.UNITS.getLENGTH());
		
		((Label) (AzimuthRCLO.getChildren().get(2))).setText(Constance.UNITS.getLENGTH());
		((Label) (AzimuthLSaL.getChildren().get(2))).setText(Constance.UNITS.getLENGTH());
		((Label) (AzimuthRSaL.getChildren().get(2))).setText(Constance.UNITS.getLENGTH());
		
	}	
}
