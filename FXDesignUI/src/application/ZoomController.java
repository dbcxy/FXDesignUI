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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.AppConfig;
import model.graph.ILayoutParam;

import org.apache.log4j.Logger;

import utils.Constance;
import utils.Utils;
import views.ResizableCanvas;

public class ZoomController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(DisplaySetUpController.class);
	
	@FXML AnchorPane ZoomDialog;
	
	@FXML Pane chartZoom;
	@FXML ResizableCanvas cZoomL0;//Graph
	@FXML ResizableCanvas cZoomL1;//Video
	@FXML ResizableCanvas cZoomL2;//Track & Plots
	
		
	AppConfig appConfig = AppConfig.getInstance();
	
	double initialX,initialY;

	@Override
	public void draw(GraphicsContext gc) {
		
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		logger.info("Zoom Dialog Opened");
		addDraggableNode(ZoomDialog);
		initDialog();	
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
		cZoomL0.widthProperty().bind(chartZoom.widthProperty());
		cZoomL0.heightProperty().bind(chartZoom.heightProperty());
		
		cZoomL1.widthProperty().bind(chartZoom.widthProperty());
		cZoomL1.heightProperty().bind(chartZoom.heightProperty());
		
		cZoomL2.widthProperty().bind(chartZoom.widthProperty());
		cZoomL2.heightProperty().bind(chartZoom.heightProperty());
		
		cZoomL0.clear();
		cZoomL1.clear();
		cZoomL2.clear();
		
	}

	private void closeSettings(ActionEvent event) {
		Node  source = (Node)  event.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    stage.close();
	    logger.info("Zoom Dialog Closed");
	}

}