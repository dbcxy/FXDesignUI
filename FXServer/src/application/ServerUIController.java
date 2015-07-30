package application;

import java.net.URL;
import java.util.ResourceBundle;

import network.C2Server;
import network.C2Server.Notifier;
import materialdesignbutton.MaterialDesignButtonWidget;
import materialdesignbutton.MaterialDesignButtonWidget.VAL;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ServerUIController implements Initializable, Notifier{

	@FXML AnchorPane ServerUI;
	
	@FXML TextField targetSpeed;
	@FXML TextField initRange;
	@FXML TextField initAz;
	@FXML TextField scanTime;
	@FXML TextField noOfScans;
	
	@FXML TextArea serverLog;
	
	@FXML MaterialDesignButtonWidget startBtn;	
	
	double initialX,initialY;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		targetSpeed.setText(String.valueOf(C2Server.TARGET_SPEED));
		initRange.setText(String.valueOf(C2Server.INIT_RANGE));
		initAz.setText(String.valueOf(C2Server.INIT_AZ));
		scanTime.setText(String.valueOf(C2Server.SCAN_TIME));
		noOfScans.setText(String.valueOf(C2Server.NO_SCANS));		
		
		startBtn.widgetSetVal(VAL.GREEN);
		addDraggableNode(ServerUI);	
		notifyData("-----------------------Server Launched---------------------");
	}
	
	@FXML
	protected void startClick(ActionEvent event) {
		String ts = targetSpeed.getText();
		String irng = initRange.getText();
		String iaz = initAz.getText();
		String st = scanTime.getText();
		String nos = noOfScans.getText();
		
		if(ts.isEmpty() || irng.isEmpty() || iaz.isEmpty() || st.isEmpty() || nos.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR, " Text Field Can't be empty ", ButtonType.OK);
			alert.showAndWait();
		} else {
			if(validateTargetSpeed(ts) &&
					validateRange(irng) &&
					validateAz(iaz) &&
					validateScanTime(st) &&
					validateNoOfScans(nos))	{
				startNetworkTask();
				updateStartButton(1);
			}
		}
	}
	
	@FXML
	protected void closeStage(ActionEvent event) {
		Node  source = (Node)  event.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
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

	private void startNetworkTask() {
		C2Server c2Server = new C2Server();
		c2Server.startMCUDPThreadServer();
		
		
	}
	
	private boolean validateTargetSpeed(String ts) {
		double tspeed = Double.parseDouble(ts);
		if(tspeed >= 0){
			C2Server.TARGET_SPEED = (int) tspeed;
			return true;
		}
		Alert alert = new Alert(AlertType.ERROR, " Target Speed Can't be a negative value!! ", ButtonType.OK);
		alert.showAndWait();
		return false;
	}
	
	private boolean validateRange(String irng) {
		double rng = Double.parseDouble(irng);
		if(rng >= 0) {
			C2Server.INIT_RANGE = (int) rng;
			return true;
		}
		Alert alert = new Alert(AlertType.ERROR, " Range Can't be a negative value!! Default Range is 0KM - 40KM ", ButtonType.OK);
		alert.showAndWait();
		return false;	
	}
	
	private boolean validateAz(String az) {
		double azm = Double.parseDouble(az);
		if((azm >= -10) && (azm <= 10)) {
			C2Server.INIT_AZ = azm;
			return true;
		}
		Alert alert = new Alert(AlertType.ERROR, " Azimuth range is between 10 degrees to -10 degrees!! ", ButtonType.OK);
		alert.showAndWait();
		return false;	
	}
	
	private boolean validateScanTime(String st) {
		double sct = Double.parseDouble(st);
		if(sct >= 0) {
			C2Server.SCAN_TIME = sct;
			return true;
		}
		Alert alert = new Alert(AlertType.ERROR, " Scan Time Can't be a negative value!! Allowed range is 0s to 5s ", ButtonType.OK);
		alert.showAndWait();
		return false;
	}
	
	private boolean validateNoOfScans(String nsc) {
		double nos = Double.parseDouble(nsc);
		if(nos >= 0) {
			C2Server.NO_SCANS = (int) nos;
			return true;
		}
		Alert alert = new Alert(AlertType.ERROR, " No of Scans Can't be a negative value!! ", ButtonType.OK);
		alert.showAndWait();
		return false;		
	}

	@Override
	public void notifyData(String str) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				serverLog.appendText(str+"\n");
			}
		});		
	}

	@Override
	public void updateStartButton(int i) {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				switch (i) {
				case 0:
					startBtn.setText("START");
					startBtn.widgetSetVal(VAL.GREEN);
					break;
					
				case 1:
					startBtn.setText("STOP");
					startBtn.widgetSetVal(VAL.RED);
					break;
				default:
					break;
				}
			}
		});		
	}

}
