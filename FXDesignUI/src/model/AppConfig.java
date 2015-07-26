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
		
		if(checkNullData()) {		
			//get TextField to save values
			int i = 0;
			isValidSettings = true;
			Constance.ELEVATION_MAX = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 12000, Constance.UNITS.getLENGTH());i++;
			Constance.AZIMUTH_MAX = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 12000, Constance.UNITS.getLENGTH());i++;
			
			Constance.ELEVATION.USL_ANGLE = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 30, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.GLIDE_ANGLE = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 10, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.GLIDE_MAX_DIST = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 40, Constance.UNITS.getLENGTH());i++;
			Constance.ELEVATION.GLIDE_FLAT_START_DIST = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 40, Constance.UNITS.getLENGTH());i++;
			Constance.ELEVATION.LSL_ANGLE = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 5, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.UAL_ANGLE = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 15, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.LAL_ANGLE = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 15, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.USaL_ANGLE = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 15, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.LSaL_ANGLE = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 15, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.DH = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 1, Constance.UNITS.getHEIGHT());i++;
			
			Constance.AZIMUTH.LSL_ANGLE = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 15, Constance.UNITS.DEGREES);i++;
			Constance.AZIMUTH.RSL_ANGLE = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), -15, 0, Constance.UNITS.DEGREES);i++;
			Constance.AZIMUTH.RCLO = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 0.1, Constance.UNITS.getLENGTH());i++;
			Constance.AZIMUTH.LAL_ANGLE = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 15, Constance.UNITS.DEGREES);i++;
			Constance.AZIMUTH.RAL_ANGLE = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), -15, 0, Constance.UNITS.DEGREES);i++;
			Constance.AZIMUTH.LSaL = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 0.1, Constance.UNITS.getLENGTH());i++;
			Constance.AZIMUTH.RSaL = itemSettingVal[i] = validateSettingsData(itemSettingVal[i], ((TextField)(itemSettingList.get(i).getChildren().get(1))).getText(),
					((Label)(itemSettingList.get(i).getChildren().get(0))).getText(), 0, 0.1, Constance.UNITS.getLENGTH());
		
		} else
			openErrorDialog("Value can't be empty!");

		if(isValidSettings) {
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
	
	public void openErrorDialog(String msg) {
		Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
		alert.setTitle("Error");
		alert.showAndWait();
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

	private boolean checkNullData() {

		for(int i=0;i<itemSettingList.size();i++) {
			TextField tField = ((TextField)(itemSettingList.get(i).getChildren().get(1)));
			if(tField.getText().isEmpty()) {
				((Label)(itemSettingList.get(i).getChildren().get(3))).setTextFill(Color.RED);
				((Label)(itemSettingList.get(i).getChildren().get(3))).setText("*");
				return false;
			}
		}
		logger.info("Settings Data Null Check");
		return true;
	}
	
	private double validateSettingsData(double actualVAl, String str, String title, double min, double max, String unit) {
		double val = actualVAl;
		double check = Double.valueOf(str);
		try {
			if((check <= max) && (check >= min)) {
				val = check;
			} else {
				isValidSettings = false;
				openErrorDialog(title+" value ranges from: "+min+" "+unit+" to "+max+" "+unit+". \nPlease make suitable corrections");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}
	
	
	
	

}
