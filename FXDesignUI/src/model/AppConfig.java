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
	
	boolean isValidDisplaySetup = false;
	boolean isValidSystemSetup = false;
	boolean isValidRadarSetup = false;
	
	List<HBox> itemDisplaySetupList = new ArrayList<HBox>();
	double[] itemDisplaySetupVal = new double[] {
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
	
	String[] itemSystemSetupVal = new String[] {
		Constance.PREF.SEL_RUNWAY,
		Constance.PREF.RANGE_UNITS,
		Constance.PREF.EL_AZ_UNITS,
		String.valueOf(Constance.PORT_AZ_PLOTS),
		String.valueOf(Constance.PORT_AZ_TRACKS),
		String.valueOf(Constance.PORT_EL_PLOTS),
		String.valueOf(Constance.PORT_EL_TRACKS),
		String.valueOf(Constance.PORT_VIDEO),
		String.valueOf(Constance.PORT_WRITE)
	};
	
	String[] itemRadarSetupVal = new String[] {
			Constance.PREF.MODE_OP,
			Constance.PREF.CHG_POL,
			Constance.PREF.FREQ_SEL,
			Constance.PREF.BITE
		};
	
	public FXMLController getFxmlController() {
		return fxmlController;
	}

	public void setFxmlController(FXMLController fxmlController) {
		this.fxmlController = fxmlController;
	}

	public void addDisplaySetup(HBox hbox) {
		itemDisplaySetupList.add(hbox);
	}
	
	public int getDisplaySetupListSize() {
		return itemDisplaySetupList.size();
	}
	
	public List<HBox> getDisplaySetupList() {
		return itemDisplaySetupList;
	}
	
	public double getDisplaySetupListValue(int index) {
		return itemDisplaySetupVal[index];
	}
	
	public void setDisplaySetupListValue(int index, double value) {
		itemDisplaySetupVal[index] = value;
	}
	
	public void setSystemSetupListValue(int index, String value) {
		itemSystemSetupVal[index] = value;
	}
	
	public boolean isDisplaySetupSaved() {
		return isValidDisplaySetup;
	}
	
	public boolean isSystemSetupSaved() {
		return isValidSystemSetup;
	}
	
	public boolean isRadarSetupSaved() {
		return isValidRadarSetup;
	}
	
	public void saveDisplaySetupData() {
		
		if(checkNullData()) {		
			//get TextField to save values
			int i = 0;
			isValidDisplaySetup = true;
			Constance.ELEVATION_MAX = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 12000, Constance.UNITS.getLENGTH());i++;
			Constance.AZIMUTH_MAX = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 12000, Constance.UNITS.getLENGTH());i++;
			
			Constance.ELEVATION.USL_ANGLE = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 30, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.GLIDE_ANGLE = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 10, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.GLIDE_MAX_DIST = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 40, Constance.UNITS.getLENGTH());i++;
			Constance.ELEVATION.GLIDE_FLAT_START_DIST = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 40, Constance.UNITS.getLENGTH());i++;
			Constance.ELEVATION.LSL_ANGLE = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 5, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.UAL_ANGLE = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 15, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.LAL_ANGLE = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 15, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.USaL_ANGLE = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 15, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.LSaL_ANGLE = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 15, Constance.UNITS.DEGREES);i++;
			Constance.ELEVATION.DH = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 1, Constance.UNITS.getHEIGHT());i++;
			
			Constance.AZIMUTH.LSL_ANGLE = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 15, Constance.UNITS.DEGREES);i++;
			Constance.AZIMUTH.RSL_ANGLE = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), -15, 0, Constance.UNITS.DEGREES);i++;
			Constance.AZIMUTH.RCLO = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 0.1, Constance.UNITS.getLENGTH());i++;
			Constance.AZIMUTH.LAL_ANGLE = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 15, Constance.UNITS.DEGREES);i++;
			Constance.AZIMUTH.RAL_ANGLE = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), -15, 0, Constance.UNITS.DEGREES);i++;
			Constance.AZIMUTH.LSaL = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 0.1, Constance.UNITS.getLENGTH());i++;
			Constance.AZIMUTH.RSaL = itemDisplaySetupVal[i] = validateDisplaySetupData(itemDisplaySetupVal[i], ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1))).getText(),
					((Label)(itemDisplaySetupList.get(i).getChildren().get(0))).getText(), 0, 0.1, Constance.UNITS.getLENGTH());
		
		} else
			openErrorDialog("Value can't be empty!");

		if(isValidDisplaySetup) {
			Constance.IS_DISPLAY_SETUP = true;
			logger.info("DisplaySetup Data Saved");
		}
	}
	
	public void saveSystemSetupData() {
		
		validateSystemSetupData();
		if(isValidSystemSetup) {
			Constance.IS_SYSTEM_SETUP = true;
			logger.info("SystemSetup Data Saved");
		}
	}
	
	public void saveRadarSetupData() {
		
		validateRadarSetupData();
		if(isValidRadarSetup) {
			Constance.IS_RADAR_SETUP = true;
			logger.info("RadarSetup Data Saved");
		}
	}
	
	public void openErrorDialog(String msg) {
		Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
		alert.setTitle("Error");
		alert.showAndWait();
	}
	
	private void validateRadarSetupData() {

		for(int i=0;i<itemRadarSetupVal.length;i++) {
			if(itemRadarSetupVal[i].isEmpty()) {		
				isValidRadarSetup = false;
				break;
			}
			isValidRadarSetup = true;
		}
		if(!isValidRadarSetup) {
			Alert alert = new Alert(AlertType.ERROR, "Value can't be NULL " + "!", ButtonType.OK);
			alert.setTitle("Alert");
			alert.showAndWait();
		}
		logger.info("RadarSetup Data Validated");
		
	}

	private void validateSystemSetupData() {

		for(int i=0;i<itemSystemSetupVal.length;i++) {
			if(itemSystemSetupVal[i].isEmpty()) {		
				isValidSystemSetup = false;
				break;
			}
			isValidSystemSetup = true;
		}
		if(!isValidSystemSetup) {
			Alert alert = new Alert(AlertType.ERROR, "Value can't be NULL " + "!", ButtonType.OK);
			alert.setTitle("Alert");
			alert.showAndWait();
		}
		logger.info("SystemSetup Data Validated");
		
	}

	private boolean checkNullData() {

		for(int i=0;i<itemDisplaySetupList.size();i++) {
			TextField tField = ((TextField)(itemDisplaySetupList.get(i).getChildren().get(1)));
			if(tField.getText().isEmpty()) {
				((Label)(itemDisplaySetupList.get(i).getChildren().get(3))).setTextFill(Color.RED);
				((Label)(itemDisplaySetupList.get(i).getChildren().get(3))).setText("*");
				return false;
			}
		}
		logger.info("DisplaySetup Data Null Check");
		return true;
	}
	
	private double validateDisplaySetupData(double actualVAl, String str, String title, double min, double max, String unit) {
		double val = actualVAl;
		double check = Double.valueOf(str);
		try {
			if((check <= max) && (check >= min)) {
				val = check;
			} else {
				isValidDisplaySetup = false;
				openErrorDialog(title+" value ranges from: "+min+" "+unit+" to "+max+" "+unit+". \nPlease make suitable corrections");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}
	
	
	
	

}
