package model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import application.FXMLController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import utils.Constance;

public class AppConfig {
	
	private static final Logger logger = Logger.getLogger(AppConfig.class);
	private static AppConfig instance = null;
	
	protected AppConfig() {
		// Exists only to defeat instantiation.
	}
	
	public static AppConfig getInstance() {
	      if(instance == null) {
	         instance = new AppConfig();
	         logger.info("App Config Instantiated");
	      }
	      return instance;
	}
	
	FXMLController fxmlController;
	
	boolean isValidSettings = false;
	boolean isValidPreference = false;
	
	List<HBox> itemSettingList = new ArrayList<HBox>();
	double[] itemSettingVal = new double[] {
		Constance.ELEVATION_MAX,
		Constance.AZIMUTH_MAX,
		
		Constance.ELEVATION.USL_ANGLE,
		Constance.ELEVATION.GLIDE_ANGLE,
		Constance.ELEVATION.GLIDE_MAX_DIST,
		Constance.ELEVATION.GLIDE_FLAT_START_DIST,
		Constance.ELEVATION.LSL_ANGLE,
		Constance.ELEVATION.UAL_ANGLE,
		Constance.ELEVATION.LAL_ANGLE,
		Constance.ELEVATION.USaL_ANGLE,
		Constance.ELEVATION.LSaL_ANGLE,
		Constance.ELEVATION.DH,
		
		Constance.AZIMUTH.LSL_ANGLE,
		Constance.AZIMUTH.RSL_ANGLE,
		Constance.AZIMUTH.RCLO,
		Constance.AZIMUTH.LAL_ANGLE,
		Constance.AZIMUTH.RAL_ANGLE,
		Constance.AZIMUTH.LSaL,
		Constance.AZIMUTH.RSaL
	};
	
	String[] itemPrefVal = new String[] {
		Constance.PREF.SEL_RUNWAY,
		Constance.PREF.MODE_OP,
		Constance.PREF.CHG_POL,
		Constance.PREF.RANGE_UNITS,
		Constance.PREF.EL_AZ_UNITS,
		Constance.PREF.FREQ_SEL,
		Constance.PREF.BITE
	};
	
	public FXMLController getFxmlController() {
		return fxmlController;
	}

	public void setFxmlController(FXMLController fxmlController) {
		this.fxmlController = fxmlController;
	}

	public void addSetting(HBox hbox) {
		itemSettingList.add(hbox);
	}
	
	public int getSettingListSize() {
		return itemSettingList.size();
	}
	
	public List<HBox> getSettingList() {
		return itemSettingList;
	}
	
	public double getSettingListValue(int index) {
		return itemSettingVal[index];
	}
	
	public void setSettingListValue(int index, double value) {
		itemSettingVal[index] = value;
	}
	
	public void setPrefListValue(int index, String value) {
		itemPrefVal[index] = value;
	}
	
	public boolean isSettingSaved() {
		return isValidSettings;
	}
	
	public boolean isPreferenceSaved() {
		return isValidPreference;
	}
	
	public void saveSettingsData() {
		
		validateSettingsData();
		if(isValidSettings) {		
			//get TextField to save values
			int i = 0;
			Constance.ELEVATION_MAX = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.AZIMUTH_MAX = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			
			Constance.ELEVATION.USL_ANGLE = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.ELEVATION.GLIDE_ANGLE = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.ELEVATION.GLIDE_MAX_DIST = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.ELEVATION.GLIDE_FLAT_START_DIST = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.ELEVATION.LSL_ANGLE = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.ELEVATION.UAL_ANGLE = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.ELEVATION.LAL_ANGLE = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.ELEVATION.USaL_ANGLE = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.ELEVATION.LSaL_ANGLE = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.ELEVATION.DH = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			
			Constance.AZIMUTH.LSL_ANGLE = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.AZIMUTH.RSL_ANGLE = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.AZIMUTH.RCLO = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.AZIMUTH.LAL_ANGLE = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.AZIMUTH.RAL_ANGLE = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.AZIMUTH.LSaL = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());i++;
			Constance.AZIMUTH.RSaL = itemSettingVal[i] = Double.valueOf(((TextField)(itemSettingList.get(i).getChildren().get(1))).getText());

			
			Constance.IS_CONFIG_SET = true;
			logger.info("Settings Data Saved");
		}
	}
	
	public void savePreferenceData() {
		
		validatePreferenceData();
		if(isValidPreference) {
			Constance.IS_PREF_SET = true;
			logger.info("Preference Data Saved");
		}
	}

	private void validatePreferenceData() {

		for(int i=0;i<itemPrefVal.length;i++) {
			if(itemPrefVal[i].isEmpty()) {		
				isValidPreference = false;
				break;
			}
			isValidPreference = true;
		}
		if(!isValidPreference) {
			Alert alert = new Alert(AlertType.ERROR, "Value can't be NULL " + "!", ButtonType.OK);
			alert.setTitle("Alert");
			alert.showAndWait();
		}
		logger.info("Preference Data Validated");
		
	}

	private void validateSettingsData() {

		for(int i=0;i<itemSettingList.size();i++) {
			TextField tField = ((TextField)(itemSettingList.get(i).getChildren().get(1)));
			if(tField.getText().isEmpty()) {
				((Label)(itemSettingList.get(i).getChildren().get(3))).setTextFill(Color.RED);
				((Label)(itemSettingList.get(i).getChildren().get(3))).setText("*");
				isValidSettings = false;
				break;
			} else
				isValidSettings = true;
		}
		if(!isValidSettings) {
			Alert alert = new Alert(AlertType.ERROR, "Value can't be empty " + "!", ButtonType.OK);
			alert.setTitle("Alert");
			alert.showAndWait();
		}
		logger.info("Settings Data Validated");
	}
	
	

}
