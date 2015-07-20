package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.AppConfig;
import model.graph.ILayoutParam;

import org.apache.log4j.Logger;

import utils.Constance;

public class SettingsController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(SettingsController.class);

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
	
	@Override
	public void draw(GraphicsContext gc) {
		
	}

	@Override
	public void initialize(URL url, ResourceBundle rBundle) {
		logger.info("Settings Dialog Opened");
		loadToList();
		initDefaultValues();
		initUnits();
		
		Constance.IS_CONFIG_SET = true;
	}
	
	private void loadToList() {
		appConfig.getSettingList().clear();
		appConfig.addSetting(MaximumElevation);
		appConfig.addSetting(MaximumAzimuth);
		appConfig.addSetting(ElevationUSL);
		appConfig.addSetting(ElevationGlideAngle);
		appConfig.addSetting(ElevationGlideMaxDist);
		appConfig.addSetting(ElevationGlideFlatStartDist);
		appConfig.addSetting(ElevationLSL);
		appConfig.addSetting(ElevationUAL);
		appConfig.addSetting(ElevationLAL);
		appConfig.addSetting(ElevationUSaL);
		appConfig.addSetting(ElevationLSaL);
		appConfig.addSetting(ElevationDH);
		appConfig.addSetting(AzimuthLSL);
		appConfig.addSetting(AzimuthRSL);
		appConfig.addSetting(AzimuthRCLO);
		appConfig.addSetting(AzimuthLAL);
		appConfig.addSetting(AzimuthRAL);
		appConfig.addSetting(AzimuthLSaL);
		appConfig.addSetting(AzimuthRSaL);		
	}

	private void initDefaultValues() {
		
		//set Default label to NOTHING
		for(int i=0;i<appConfig.getSettingListSize();i++) {
			((Label)(appConfig.getSettingList().get(i).getChildren().get(3))).setText("");
		}
		
		//set TextField to their values
		for(int i=0;i<appConfig.getSettingListSize();i++) {
			((TextField)(appConfig.getSettingList().get(i).getChildren().get(1))).setText(String.valueOf(appConfig.getSettingListValue(i)));
		}
	}

	private void initUnits() {
		((Label) (MaximumElevation.getChildren().get(2))).setText(Constance.UNITS.getHEIGHT());
		((Label) (MaximumAzimuth.getChildren().get(2))).setText(Constance.UNITS.getHEIGHT());
		
		((Label) (ElevationDH.getChildren().get(2))).setText(Constance.UNITS.getHEIGHT());
		((Label) (ElevationGlideMaxDist.getChildren().get(2))).setText(Constance.UNITS.getLENGTH());
		((Label) (ElevationGlideFlatStartDist.getChildren().get(2))).setText(Constance.UNITS.getLENGTH());
		
		((Label) (AzimuthRCLO.getChildren().get(2))).setText(Constance.UNITS.getHEIGHT());
		((Label) (AzimuthLSaL.getChildren().get(2))).setText(Constance.UNITS.getHEIGHT());
		((Label) (AzimuthRSaL.getChildren().get(2))).setText(Constance.UNITS.getHEIGHT());
		
	}

	@FXML
	protected void okClick(ActionEvent event) {
		appConfig.saveSettingsData();
		if(appConfig.isSettingSaved()) {
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
}
