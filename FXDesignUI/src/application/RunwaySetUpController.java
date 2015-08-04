package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import utils.Utils;

public class RunwaySetUpController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(DisplaySetUpController.class);
	
	@FXML AnchorPane RunwaySetup;
	
	@FXML ComboBox<String> comboRunway;
	@FXML ComboBox<String> comboLR;
	
	AppConfig appConfig = AppConfig.getInstance();
	
	double initialX,initialY;

	@Override
	public void draw(GraphicsContext gc) {
		
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		logger.info("Runway Setup Dialog Opened");
		addDraggableNode(RunwaySetup);
		initDialog();
		loadDefault();		
	}	

	@FXML
	protected void okClick(ActionEvent event) {
		appConfig.validateSystemSetupData();
		if(appConfig.isSystemSetupValid()) {
			saveData();
			closeSettings(event);
			AppConfig.getInstance().getFxmlController().flipGraphOnRunwaySel(Constance.PREF.SEL_RUNWAY);
			AppConfig.getInstance().getFxmlController().initUIComponents(Constance.PREF.LEFT_RIGHT);
			AppConfig.getInstance().getFxmlController().notifyChanges();
		}
	}
	
	@FXML
	protected void cancelClick(ActionEvent event) {
		loadDefault();
		saveData();
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
		
		comboLR.getItems().clear();
		comboLR.getItems().addAll("Left","Right");
		comboLR.setDisable(true);
		
		comboRunway.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if(newValue.contains("1") || newValue.contains("2"))
					comboLR.setValue("Left");
				else if(newValue.contains("3") || newValue.contains("4"))
					comboLR.setValue("Right");						
			}
		});
		
	}

	private void loadDefault() {
		comboRunway.setValue(Constance.PREF.SEL_RUNWAY);
		comboLR.setValue(Constance.PREF.LEFT_RIGHT);
	}

	private void saveData() {
		Constance.PREF.SEL_RUNWAY = comboRunway.getValue();
		Constance.PREF.LEFT_RIGHT = comboLR.getValue();		
	}

	private void closeSettings(ActionEvent event) {
		Node  source = (Node)  event.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
	    logger.info("Runway Setup Dialog Closed");
	}

}