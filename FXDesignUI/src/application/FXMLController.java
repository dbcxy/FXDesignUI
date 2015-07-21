package application;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import model.AppConfig;
import model.DataObserver;
import model.MatrixRef;
import model.drawable.Plot;
import model.drawable.Track;
import model.graph.AzimuthChart;
import model.graph.ElevationChart;
import model.graph.ILayoutParam;
import network.IControlManager;
import network.TaskObserver;

import org.apache.log4j.Logger;

import utils.Constance;
import utils.ModelDrawing;
import views.Console;
import views.ResizableCanvas;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(FXMLController.class);
	
	@FXML private MenuBar fxMenuBar;
	
	@FXML private Label actiontarget;
	
	@FXML private TextField textTime;
	@FXML private TextField textDate;
	@FXML private TextArea consoleOutput;
	@FXML private TextField consoleInput;
	
	@FXML private Button btn_startDisplay;
	@FXML private Button btn_display5NM;
	@FXML private Button btn_display10NM;
	@FXML private Button btn_display20NM;
	@FXML private Button btn_display40NM;
	
	@FXML private ScrollPane chartTopScroll;
	@FXML private ScrollPane chartBottomScroll;
	@FXML private Pane chartTop;
	@FXML private Pane chartBottom;
	
	@FXML ResizableCanvas cTopL0;
	@FXML ResizableCanvas cTopL1;
	@FXML ResizableCanvas cTopL2;
	@FXML ResizableCanvas cTopL3;
	
	@FXML ResizableCanvas cBtmL0;
	@FXML ResizableCanvas cBtmL1;
	@FXML ResizableCanvas cBtmL2;
	@FXML ResizableCanvas cBtmL3;
	
	@FXML Slider sliderZoomTop;
	@FXML Slider sliderZoomBottom;
	
	private double originalSizeX, originalSizeY;
	private double pressedX, pressedY;
	private boolean isAppRunning = false;
	private boolean isDrawn = false;
	
	MatrixRef matrixRef;
	
	TaskObserver tTask;
	ElevationChart mElevationChart;
	AzimuthChart mAzimuthChart;
		
	@Override
	public void initialize(URL url, ResourceBundle rb) {		
		initCanvasLayout();
		actiontarget.setText("Start the System....");
	}
	
	@FXML 
    protected void onStartAction(ActionEvent event) {
		startDisplayAction();
    }

	@FXML 
    protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("Button Clicked");
    }
    
    @FXML 
    protected void handle5NMButtonAction(ActionEvent event) {
        actiontarget.setText("Display Scale set to 5NM");
		setNMparameter(5);
		invalidate();
    }
    
    @FXML 
    protected void handle10NMButtonAction(ActionEvent event) {
        actiontarget.setText("Display Scale set to 10NM");
        setNMparameter(10);
		invalidate();
    }
    
    @FXML 
    protected void handle20NMButtonAction(ActionEvent event) {
        actiontarget.setText("Display Scale set to 20NM");
        setNMparameter(20);
		invalidate();
    }
    
    @FXML 
    protected void handle40NMButtonAction(ActionEvent event) {
        actiontarget.setText("Display Scale set to 40NM");
        setNMparameter(40);
		invalidate();
    }
    
    @FXML
    protected void menuCloseStage() {
    	Stage stage = (Stage) fxMenuBar.getScene().getWindow();
    	Constance.IS_CLOSE = true;
    	if(Constance.IS_CONNECTED) {
	    	if(Constance.UDPIP)
				try {
					tTask.InterruptableUDPThread(new DatagramSocket());
				} catch (SocketException e) {
					e.printStackTrace();
				}
	    	tTask.closeActiveConnection();
	    	tTask.interrupt();
    	}
    	stage.close();
    	logger.info("APPLICATION CLOSED");
    }
    
    @FXML
    protected void menuSettings() {    	
    	final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			fxmlLoader.load(getClass().getResourceAsStream("Settings.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Parent root = (Parent) fxmlLoader.getRoot();
        Scene dialogSettings = new Scene(root);
        dialog.setScene(dialogSettings);
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.centerOnScreen();
        dialog.show();
    }
    
    @FXML 
    protected void menuPreferences() {
    	final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			fxmlLoader.load(getClass().getResourceAsStream("Preferences.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Parent root = (Parent) fxmlLoader.getRoot();
        Scene dialogPreferences = new Scene(root);
        dialog.setScene(dialogPreferences);
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.centerOnScreen();
        dialog.show();    	
    }
    
    private void startShowingDisplay() {
    	
		if(!isAppRunning) {
			initSystem();
			initTopChart();
			initBottomChart();
//			startNetworkTask();
			isAppRunning = true;
			isDrawn = true;
			btn_startDisplay.setStyle("-fx-background-image: url(\"assets/img/stop.png\"); -fx-background-size: 50 50; -fx-background-repeat: no-repeat; -fx-background-position: center;");
			actiontarget.setText("System Loaded!");
		} else {
			cTopL1.draw();
			cTopL2.draw();
			cTopL3.draw();
			cBtmL1.draw();
			cBtmL2.draw();
			cBtmL3.draw();
//			stopNetworkTask();
			isAppRunning = false;
			isDrawn = false;
			btn_startDisplay.setStyle("-fx-background-image: url(\"assets/img/start.png\"); -fx-background-size: 50 50; -fx-background-repeat: no-repeat; -fx-background-position: center;");
			actiontarget.setText("Start System....");
		} 
    }
    
    private void startNetworkTask() {
		tTask = new TaskObserver(new IControlManager() {
			
			@Override
			public void manageData(DataObserver mDataObserver) {
				refreshCanvas(mDataObserver);
				logger.info("Setting Data Observer");
			}
		});
		tTask.start();
	}
    
    private void initCanvasLayout() {
    	    	
    	cTopL0.widthProperty().bind(chartTop.widthProperty());
		cTopL0.heightProperty().bind(chartTop.heightProperty());
    	
    	cTopL1.widthProperty().bind(chartTop.widthProperty());
		cTopL1.heightProperty().bind(chartTop.heightProperty());
		
		cTopL2.widthProperty().bind(chartTop.widthProperty());
		cTopL2.heightProperty().bind(chartTop.heightProperty());
		
		cTopL3.widthProperty().bind(chartTop.widthProperty());
		cTopL3.heightProperty().bind(chartTop.heightProperty());
		
		cBtmL0.widthProperty().bind(chartBottom.widthProperty());
		cBtmL0.heightProperty().bind(chartBottom.heightProperty());
		
		cBtmL1.widthProperty().bind(chartBottom.widthProperty());
		cBtmL1.heightProperty().bind(chartBottom.heightProperty());
		
		cBtmL2.widthProperty().bind(chartBottom.widthProperty());
		cBtmL2.heightProperty().bind(chartBottom.heightProperty());
		
		cBtmL3.widthProperty().bind(chartBottom.widthProperty());
		cBtmL3.heightProperty().bind(chartBottom.heightProperty());
		
    }
    
    private void initSystem() {
		initTimeDate();
    	initConsole();
    	initMatrixRef();
    }
        
    private void initTopChart() {
		setNMparameter(10);
		originalSizeX = chartTop.getWidth();
		originalSizeY = chartTop.getHeight();

		cTopL3.clear();
		cTopL2.clear();
		cTopL1.clear();
		cTopL0.clear();
		drawTextTop(cTopL0);
		drawGraphTop(cTopL1);
//        updateObjects(cTopL2);
        initPan(chartTop);
        initZoomSlider(sliderZoomTop, chartTop, chartTopScroll, "top");
    	logger.info("Top Chart initialization");
	}

	private void initBottomChart() {
		cBtmL3.clear();
		cBtmL2.clear();
		cBtmL1.clear();
		cBtmL0.clear();
		drawTextBottom(cBtmL0);
		drawGraphBottom(cBtmL1);
//        updateObjects(cBtmL2);
        initPan(chartBottom);
        initZoomSlider(sliderZoomBottom, chartBottom, chartBottomScroll, "down");
        logger.info("Bottom Chart initialization");
    }
	
	private void initPan(final Pane pane) {
		pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			
			@Override
		    public void handle(MouseEvent event) {
		          pressedX = event.getX();
		          pressedY = event.getY();
		        }
		});
		
		pane.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				pane.setTranslateX(pane.getTranslateX() + event.getX() - pressedX);
                pane.setTranslateY(pane.getTranslateY() + event.getY() - pressedY);
                event.consume();
			}
		});
	}
    
    private void initZoomSlider(final Slider slider, final Pane pane, final ScrollPane scrollPane,
    		String val) {
    	slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				
//				pane.setPrefWidth(originalSizeX*newValue.doubleValue()/2); 
//	    		pane.setPrefHeight(originalSizeY*newValue.doubleValue()/2);
				
				pane.setScaleX(newValue.doubleValue()/2);
				pane.setScaleY(newValue.doubleValue()/2);
				
				if(newValue.intValue()==2) {
					pane.setScaleX(1.0);
					pane.setScaleY(1.0);
					pane.setTranslateX(0.0);
					pane.setTranslateY(0.0);
					pane.setTranslateZ(0.0);
				}
	    		System.out.println("Pane: "+pane.getWidth()+","+pane.getHeight());
	    	
	    		new Timer().schedule(new TimerTask() {

	    			        @Override
	    			        public void run() {
	    						Platform.runLater(new Runnable() { 
	    						      
	    					    	@Override 
	    					    	public void run() {	
	    					    		switch (val) {
										case "top":
		    					    		cTopL3.clear();
		    					    		cTopL2.clear();
		    					    		cTopL1.clear();
		    					    		cTopL0.clear();
											
		    					    		drawTextTop(cTopL0);
		    					    		drawGraphTop(cTopL1);
											break;
											
										case "down":
											cBtmL3.clear();
		    					    		cBtmL2.clear();
		    					    		cBtmL1.clear();
		    					    		cBtmL0.clear();
		    					    		drawTextBottom(cBtmL0);
		    					    		drawGraphBottom(cBtmL1);
											break;

										default:
											break;
										}
	    					    	}
	    					    });
	    			        }
	    		}, 1000);
			}
		});		
	}

	private void initTimeDate() {
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		
    	textDate.setStyle("-fx-text-inner-color: brown;");
    	textTime.setStyle("-fx-text-inner-color: brown;");
    	    	    	
    	Task timedate = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				while(!Constance.IS_CLOSE) {
					Date date = new Date();
					updateTitle(dateFormat.format(date));
					updateMessage(timeFormat.format(date));
					try {
						Thread.sleep(Constance.MILLI_SECOND);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return null;
			}    		
		};
		textDate.textProperty().bind(timedate.titleProperty());
		textTime.textProperty().bind(timedate.messageProperty());
		new Thread(timedate).start();
    	
    }
    
    private void initConsole() {
    	Console mConsole = new Console(consoleOutput, consoleInput);
    	mConsole.println("Enter Operator ID : ");
    	mConsole.setOnMessageReceivedHandler(new Consumer<String>() {
			
			@Override
			public void accept(String value) {
				System.out.println(value);		
			}
		});
//    	mConsole.println("Enter password : ");
    }
    
    private void initMatrixRef() {
    	matrixRef = MatrixRef.getInstance();
    	matrixRef.setActualXY(chartTop.getWidth(), chartTop.getHeight());
    	matrixRef.setElevationVal(Constance.ELEVATION_MAX, Constance.ELEVATION_MIN);
    	matrixRef.setAzimuthVal(Constance.AZIMUTH_MAX, Constance.AZIMUTH_MIN);
    	matrixRef.setRangeVal(Constance.RANGE_MAX, Constance.RANGE_MIN);
    	matrixRef.setTouchDown(Constance.TOUCH_DOWN);
    }
	
	public void invalidate() {
		cTopL3.clear();
		cTopL2.clear();
		cTopL1.clear();
		cTopL0.clear();
		drawTextTop(cTopL0);
		drawGraphTop(cTopL1);
		logger.info("Top Chart Invalidated!");
		
		cBtmL3.clear();
		cBtmL2.clear();
		cBtmL1.clear();
		cBtmL0.clear();
		drawTextBottom(cBtmL0);
		drawGraphBottom(cBtmL1);
		logger.info("Bottom Chart Invalidated!");
	}
	
	private void setNMparameter(int valNM) {
		btn_display5NM.setDefaultButton(false);
		btn_display10NM.setDefaultButton(false);
		btn_display20NM.setDefaultButton(false);
		btn_display40NM.setDefaultButton(false);
		switch (valNM) {
		case 40:
			Constance.SCALE = " 40 "+Constance.UNITS.getLENGTH();
			matrixRef.setMaxRange(40);
			btn_display40NM.setDefaultButton(true);
			break;
			
		case 20:
			Constance.SCALE = " 20 "+Constance.UNITS.getLENGTH();
			matrixRef.setMaxRange(20);
			btn_display20NM.setDefaultButton(true);
			break;
			
		case 10:
			Constance.SCALE = " 10 "+Constance.UNITS.getLENGTH();
			matrixRef.setMaxRange(10);
			btn_display10NM.setDefaultButton(true);
			break;
			
		case 5:
			Constance.SCALE = " 5 "+Constance.UNITS.getLENGTH();
			matrixRef.setMaxRange(5);
			btn_display5NM.setDefaultButton(true);
			break;
		}
	}
	
    private void drawGraphTop(Canvas canvas) {    	
    	mElevationChart = new ElevationChart(canvas);
    	mElevationChart.drawElevationLine();
    	mElevationChart.drawLandingStrip();
    	mElevationChart.drawRedDistanceLine();
    	mElevationChart.drawDistanceGrid();
    	
    }
    
    private void drawTextTop(Canvas canvas) {
    	ElevationChart.drawText(canvas);
    }
    
    private void updateObjects(Canvas canvas) {
    	Track mTrack1 = new Track();
    	Track mTrack2 = new Track();
    	Track mTrack3 = new Track();
    	Plot mPlot1 = new Plot();
    	Plot mPlot2 = new Plot();
    	GraphicsContext gc = canvas.getGraphicsContext2D();
    	
    	final double WIDTH_OFF = canvas.getWidth()-OFFSET;
    	
    	//update shape
        DoubleProperty x  = new SimpleDoubleProperty();
        DoubleProperty y  = new SimpleDoubleProperty();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            	mTrack1.setXY(x.doubleValue(), y.doubleValue());
            	mTrack1.setText("AA10", "3200/100");
//            	mTrack1.draw(gc);
            	mTrack1.drawOnImage(canvas);
            	
            	mTrack2.setXY(x.doubleValue()-50, y.doubleValue()+50);
            	mTrack2.setText("AA11", "32/10");
//            	mTrack2.draw(gc);
//            	mTrack2.drawOnImage(canvas);
            	
            	mTrack3.setXY( 350, 150);
            	mTrack3.setText("ABC", "300/10");
//            	mTrack3.draw(gc);
//            	mTrack3.drawOnImage(canvas);
            	
            	mPlot1.setXY(x.doubleValue()-50, y.doubleValue()+50);
            	mPlot1.setTitle("PLOT2");
            	mPlot1.draw(gc);
            	
            	mPlot2.setXY(x.doubleValue()-30, y.doubleValue()+30);
            	mPlot2.setTitle("PLOT2");
            	mPlot2.draw(gc);
            	
            }
        };
        timer.start();
        
        Runnable mTask = new Runnable() {
			        	
			@Override
			public void run() {
				for(int i=0;i<WIDTH_OFF/2;i++){
	                x.setValue(WIDTH_OFF-i);
	                y.setValue(OFFSET+i);
	                try {
						Thread.sleep(50);//Update or refresh rate
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
        		}
			}
		};
		Thread mThread = new Thread(mTask);
		mThread.start();
    }
    
	private void drawGraphBottom(Canvas canvas) {
		mAzimuthChart = new AzimuthChart(canvas);
		mAzimuthChart.drawAzimuthLine();
		mAzimuthChart.drawLandingStrip();//till NM, and offset of 100ft / as of now 5px
		mAzimuthChart.drawRedDistanceLine();//200mts range offset from TD
		mAzimuthChart.drawDistanceGrid();

	}
	
	private void drawTextBottom(Canvas canvas) {
    	AzimuthChart.drawText(canvas);
    }
	
	public void refreshCanvas(DataObserver dataObserver) {
		Platform.runLater(new Runnable() { 
		      
	    	@Override 
	    	public void run() {	
	    		GraphicsContext gc = cTopL2.getGraphicsContext2D();
	    		gc.clearRect(0, 0, cTopL2.getWidth(), cTopL2.getHeight());
	    		dataObserver.getTrackDataList().draw(gc);
	    		dataObserver.getPlotDataList().draw(gc);
	        	logger.info("Canvas Objects Redrawn");
	    	}
		});
	}

	@Override
	public void draw(GraphicsContext gc) {
		
	}
	
	public void startDisplayAction() {
		if(!Constance.IS_CONFIG_SET) {
			Alert alert = new Alert(AlertType.INFORMATION, "Do you like to configure display settings for the radar now " + "?", ButtonType.YES, ButtonType.NO);
			alert.setTitle("Alert");
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				menuSettings();
				actiontarget.setTextFill(Color.RED);
				actiontarget.setText("Please set the Configuration Parameters for Display!");
				actiontarget.setTextFill(Color.AQUAMARINE);
			} else {
				startShowingDisplay();
			}
		} else if(!Constance.IS_PREF_SET) {
			Alert alert = new Alert(AlertType.INFORMATION, "Choose your preference for display settings. Do you want to change now ? ", ButtonType.YES, ButtonType.NO);
			alert.setTitle("Alert");
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				menuPreferences();
				actiontarget.setTextFill(Color.RED);
				actiontarget.setText("Please set the Preference accordingly!");
				actiontarget.setTextFill(Color.AQUAMARINE);
			} else {
				startShowingDisplay();
			}
		} else {
			startShowingDisplay();
		}
	}
	
	public boolean isAppRunning() {
		return isAppRunning;
	}
	
	public boolean isDrawn() {
		return isDrawn;
	}
	
	public void notifyChanges() {
		if(!isAppRunning)
			startDisplayAction();
		else if(isDrawn)
			invalidate();
	}

}
