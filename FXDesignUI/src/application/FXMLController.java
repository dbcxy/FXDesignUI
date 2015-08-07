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

import materialdesignbutton.MaterialDesignButtonWidget;
import materialdesignbutton.MaterialDesignButtonWidget.VAL;
import model.DataObserver;
import model.MatrixRef;
import model.drawable.SketchItemizedOverlay;
import model.graph.AzimuthChart;
import model.graph.ElevationChart;
import model.graph.ILayoutParam;
import network.IControlManager;
import network.TaskObserver;

import org.apache.log4j.Logger;

import textpanel.TextPanelWidget;
import utils.Constance;
import utils.ModelDrawing;
import utils.ModelDrawing.FLIP;
import views.Console;
import views.ResizableCanvas;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(FXMLController.class);
	
	@FXML private MenuBar fxMenuBar;
	
	@FXML private GridPane MainView;
	@FXML private ScrollPane UIGraph;
	@FXML private ScrollPane UIControls;
	
	@FXML private TextPanelWidget AntennaControl;
	@FXML private MaterialDesignButtonWidget btn_antscan;
	@FXML private MaterialDesignButtonWidget btn_rwyselect;
	@FXML private MaterialDesignButtonWidget btn_linearpol;
	@FXML private MaterialDesignButtonWidget btn_circularpol;
	
	@FXML private TextPanelWidget RadarControl;
	@FXML private MaterialDesignButtonWidget btn_power;
	@FXML private MaterialDesignButtonWidget btn_txstandby;
	@FXML private MaterialDesignButtonWidget btn_txradiate;
	@FXML private MaterialDesignButtonWidget btn_radarsetup;
	
	@FXML private TextPanelWidget SystemControl;
	@FXML private MaterialDesignButtonWidget btn_masterslave;
	@FXML private MaterialDesignButtonWidget btn_systemtest;
	@FXML private MaterialDesignButtonWidget btn_routeselect;
	@FXML private MaterialDesignButtonWidget btn_systemsetup;
	
	@FXML private TextPanelWidget DisplayControl;
	@FXML private MaterialDesignButtonWidget btn_raw;
	@FXML private MaterialDesignButtonWidget btn_plot;
	@FXML private MaterialDesignButtonWidget btn_label;
	@FXML private MaterialDesignButtonWidget btn_history;
	@FXML private MaterialDesignButtonWidget btn_checkmarks;
	@FXML private MaterialDesignButtonWidget btn_displaysetup;	
	
	@FXML private TextPanelWidget DisplayScale;
	@FXML private MaterialDesignButtonWidget btn_display5;
	@FXML private MaterialDesignButtonWidget btn_display10;
	@FXML private MaterialDesignButtonWidget btn_display20;
	@FXML private MaterialDesignButtonWidget btn_display40;
	
	@FXML private Label actiontarget;
	
	@FXML private TextField textTime;
	@FXML private TextField textDate;
	@FXML private TextArea consoleOutput;
	@FXML private TextField consoleInput;
	
	@FXML private Button btn_startDisplay;
	
	@FXML private ScrollPane chartTopScroll;
	@FXML private ScrollPane chartBottomScroll;
	@FXML private Pane chartTop;
	@FXML private Pane chartBottom;
	
	@FXML ResizableCanvas cTopL0;//Text
	@FXML ResizableCanvas cTopL1;//Graph
	@FXML ResizableCanvas cTopL2;//Video
	@FXML ResizableCanvas cTopL3;//Plots & tracks (TopMost Layer)
	
	@FXML ResizableCanvas cBtmL0;//Text
	@FXML ResizableCanvas cBtmL1;//Graph
	@FXML ResizableCanvas cBtmL2;//Video
	@FXML ResizableCanvas cBtmL3;//Plots & Tracks (TopMost Layer)
	
	enum DATA { DEFAULT, RAW, PLOT, TRACK, LABEL };
	private boolean isAppRunning = false;
	private boolean isDrawn = false;
	private boolean vidRefresh = false;
	private boolean topRefresh = false;
	private boolean bttmRefresh = false;
	
	private int translateTop = 0;
	private int translateBttm = 0;
	
    int azIndex = 0;
    int elIndex = 0;
	
	ColumnConstraints graph;
	ColumnConstraints controls;
	
	MatrixRef matrixRef;
	AnimationTimer topAnimTimer;
	AnimationTimer bttmAnimTimer;
	AnimationTimer rawAnimTimer;
	
	TaskObserver tTask;
	DataObserver dataObserver;
	ElevationChart mElevationChart;
	AzimuthChart mAzimuthChart;
		
	@Override
	public void initialize(URL url, ResourceBundle rb) {		
		initCanvasLayout();
    	initControls();
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
    protected void handle5ButtonAction(ActionEvent event) {
        actiontarget.setText("Display Scale set to "+Constance.SCALE);
		setDisplayScale(5);
		invalidate();
    }
    
    @FXML 
    protected void handle10ButtonAction(ActionEvent event) {
        actiontarget.setText("Display Scale set to "+Constance.SCALE);
        setDisplayScale(10);
		invalidate();
    }
    
    @FXML 
    protected void handle20ButtonAction(ActionEvent event) {
        actiontarget.setText("Display Scale set to "+Constance.SCALE);
        setDisplayScale(20);
		invalidate();
    }
    
    @FXML 
    protected void handle40ButtonAction(ActionEvent event) {
        actiontarget.setText("Display Scale set to "+Constance.SCALE);
        setDisplayScale(40);
		invalidate();
    }
    
    @FXML
    protected void showRAW(ActionEvent event) {
    	Constance.SHOW_RAW = !Constance.SHOW_RAW;
    	setDataDisplay(DATA.RAW);
    }
    
    @FXML
    protected void showPLOT(ActionEvent event) {
    	Constance.SHOW_PLOT = !Constance.SHOW_PLOT;
    	setDataDisplay(DATA.PLOT);
    }
    
    @FXML
    protected void showTRACK(ActionEvent event) {
    	Constance.SHOW_TRACK = !Constance.SHOW_TRACK;
    	setDataDisplay(DATA.TRACK);
    }
    
    @FXML
    protected void showLABEL(ActionEvent event) {
    	Constance.SHOW_TRACK_LABEL = !Constance.SHOW_TRACK_LABEL;
    	setDataDisplay(DATA.LABEL);
    }
    
    @FXML
    protected void menuCloseStage() {
    	cleanUp();
    	Stage stage = (Stage) fxMenuBar.getScene().getWindow();
    	stage.close();
    	logger.info("APPLICATION CLOSED");
    }
    
    @FXML
    protected void onDisplaySetup(ActionEvent event) {    	
    	displaySetup();
    }
    
    @FXML 
    protected void onSystemSetup(ActionEvent event) {
    	systemSetup();  	
    }
    
    @FXML 
    protected void onRadarSetup(ActionEvent event) {
    	radarSetup();  	
    }
    
    @FXML 
    protected void onRunwaySetup(ActionEvent event) {
    	runwaySetup();  	
    }
    
    private void displaySetup() {
    	final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			fxmlLoader.load(getClass().getResourceAsStream("DisplaySetUp.fxml"));
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
    
    private void systemSetup() {
    	final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			fxmlLoader.load(getClass().getResourceAsStream("SystemSetUp.fxml"));
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
    
    private void radarSetup() {
    	final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			fxmlLoader.load(getClass().getResourceAsStream("RadarSetUp.fxml"));
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
    
    private void runwaySetup() {
    	final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			fxmlLoader.load(getClass().getResourceAsStream("RunwaySetUp.fxml"));
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
			startNetworkTask();
			isAppRunning = true;
			isDrawn = true;
			btn_startDisplay.setStyle("-fx-background-image: url(\"assets/img/stop.png\"); -fx-background-size: 50 50; -fx-background-repeat: no-repeat; -fx-background-position: center;");
			actiontarget.setText("System Loaded!");
		} else {
			cTopL1.draw();
			cTopL3.draw();
			cTopL2.draw();
			cBtmL1.draw();
			cBtmL3.draw();
			cBtmL2.draw();
			stopNetworkTask();
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
//				logger.info("Setting Data Observer");
			}
		});
		tTask.start();
	}
    
    private void stopNetworkTask() {
    	if(Constance.IS_CONNECTED) {
    		Constance.IS_CONNECTED = false;
	    	if(Constance.UDPIP) {
				try {
					tTask.InterruptableUDPThread(new DatagramSocket());
				} catch (SocketException e) {
					e.printStackTrace();
				}
	    	} 
	    	tTask.closeActiveConnection();
	    	tTask.interrupt();
    	}
    }
    
    private void cleanUp() {
		topAnimTimer.stop();
		bttmAnimTimer.stop();
		rawAnimTimer.stop();
		logger.info("Ended Animation Timers");
    	Constance.IS_CLOSE = true;
    }
    
    private void initCanvasLayout() {
    	
		//swapping graph positions
		graph = MainView.getColumnConstraints().get(0);
		controls = MainView.getColumnConstraints().get(1);
    	    	
    	cTopL0.widthProperty().bind(chartTop.widthProperty());
		cTopL0.heightProperty().bind(chartTop.heightProperty());
    	
    	cTopL1.widthProperty().bind(chartTop.widthProperty());
		cTopL1.heightProperty().bind(chartTop.heightProperty());
		
		cTopL3.widthProperty().bind(chartTop.widthProperty());
		cTopL3.heightProperty().bind(chartTop.heightProperty());
		
		cTopL2.widthProperty().bind(chartTop.widthProperty());
		cTopL2.heightProperty().bind(chartTop.heightProperty());
		
		cBtmL0.widthProperty().bind(chartBottom.widthProperty());
		cBtmL0.heightProperty().bind(chartBottom.heightProperty());
		
		cBtmL1.widthProperty().bind(chartBottom.widthProperty());
		cBtmL1.heightProperty().bind(chartBottom.heightProperty());
		
		cBtmL3.widthProperty().bind(chartBottom.widthProperty());
		cBtmL3.heightProperty().bind(chartBottom.heightProperty());
		
		cBtmL2.widthProperty().bind(chartBottom.widthProperty());
		cBtmL2.heightProperty().bind(chartBottom.heightProperty());
		
		AntennaControl.widgetSetPanelTitle("Antenna Control");
		RadarControl.widgetSetPanelTitle("Radar Control");
		SystemControl.widgetSetPanelTitle("System Control");
		DisplayControl.widgetSetPanelTitle("Display Control");
		DisplayScale.widgetSetPanelTitle("Display Scale");
		
    }
    
    private void initSystem() {
		initTimeDate();
    	initConsole();
    	initMatrixRef();
    	initAnimTimers();

		setDisplayScale(10);
		setDataDisplay(DATA.DEFAULT);
    }
        
    private void initTopChart() {
		cTopL2.clear();
		cTopL3.clear();
		cTopL1.clear();
		cTopL0.clear();
		drawGraphTop(cTopL1);
		drawTextTop(cTopL0, 0, 0);
    	logger.info("Top Chart initialization");
	}

	private void initBottomChart() {
		cBtmL2.clear();
		cBtmL3.clear();
		cBtmL1.clear();
		cBtmL0.clear();
		drawGraphBottom(cBtmL1);
		drawTextBottom(cBtmL0, 0, 0);
        logger.info("Bottom Chart initialization");
    }
	
	private void initControls() {
    	
    	btn_antscan.widgetSetVal(VAL.DEFAULT);
    	btn_rwyselect.widgetSetVal(VAL.DEFAULT);
    	btn_linearpol.widgetSetVal(VAL.DEFAULT);
    	btn_circularpol.widgetSetVal(VAL.DEFAULT);
    	
    	btn_power.widgetSetVal(VAL.DEFAULT);
    	btn_txstandby.widgetSetVal(VAL.DEFAULT);
    	btn_txradiate.widgetSetVal(VAL.DEFAULT);
    	btn_radarsetup.widgetSetVal(VAL.DEFAULT);
    	
    	btn_masterslave.widgetSetVal(VAL.DEFAULT);
    	btn_systemtest.widgetSetVal(VAL.DEFAULT);
    	btn_routeselect.widgetSetVal(VAL.DEFAULT);
    	btn_systemsetup.widgetSetVal(VAL.DEFAULT);
    	
    	btn_raw.widgetSetVal(VAL.DEFAULT);
    	btn_plot.widgetSetVal(VAL.DEFAULT);
    	btn_label.widgetSetVal(VAL.DEFAULT);
    	btn_history.widgetSetVal(VAL.DEFAULT);
    	btn_checkmarks.widgetSetVal(VAL.DEFAULT);
    	btn_displaysetup.widgetSetVal(VAL.DEFAULT);
    	
		btn_display5.widgetSetVal(VAL.DEFAULT);
		btn_display10.widgetSetVal(VAL.DEFAULT);
		btn_display20.widgetSetVal(VAL.DEFAULT);
		btn_display40.widgetSetVal(VAL.DEFAULT);
		
		btn_display5.setText(" 5 "+Constance.UNITS.getLENGTH());
		btn_display10.setText(" 10 "+Constance.UNITS.getLENGTH());
		btn_display20.setText(" 20 "+Constance.UNITS.getLENGTH());
		btn_display40.setText(" 40 "+Constance.UNITS.getLENGTH());
    }
	
	public void initUIComponents(String str) {
		MainView.getChildren().clear();		
		if(str.contains("Left")) {
			MainView.addColumn(0, UIGraph);
			MainView.getColumnConstraints().set(0, graph);
			MainView.addColumn(1, UIControls);
			MainView.getColumnConstraints().set(1, controls);
		} else {
			MainView.addColumn(1, UIGraph);
			MainView.getColumnConstraints().set(1, graph);
			MainView.addColumn(0, UIControls);
			MainView.getColumnConstraints().set(0, controls);
		}
	}
	
	public void flipGraphOnRunwaySel(String str) {
		mElevationChart.drawUnitsText();
		if(str.contains("3") || str.contains("4")) {
			translateTop = 650;
			ModelDrawing.flipCanvasDrawing(cTopL1, FLIP.R2L);
			ModelDrawing.flipCanvasDrawing(cTopL3, FLIP.R2L);
			ModelDrawing.flipCanvasDrawing(cTopL2, FLIP.R2L);
			
			translateBttm = 900;
			ModelDrawing.flipCanvasDrawing(cBtmL1, FLIP.R2L);
			ModelDrawing.flipCanvasDrawing(cBtmL3, FLIP.R2L);
			ModelDrawing.flipCanvasDrawing(cBtmL2, FLIP.R2L);
		} else if(str.contains("1") || str.contains("2")) {
			translateTop = -translateTop;
			ModelDrawing.flipCanvasDrawing(cTopL1, FLIP.L2R);
			ModelDrawing.flipCanvasDrawing(cTopL3, FLIP.L2R);
			ModelDrawing.flipCanvasDrawing(cTopL2, FLIP.L2R);
			
			translateBttm = -translateBttm;
			ModelDrawing.flipCanvasDrawing(cBtmL1, FLIP.L2R);
			ModelDrawing.flipCanvasDrawing(cBtmL3, FLIP.L2R);
			ModelDrawing.flipCanvasDrawing(cBtmL2, FLIP.L2R);
		}
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

    private void initAnimTimers() {
    	
    	new Timer().scheduleAtFixedRate(new TimerTask() {

	        @Override
	        public void run() {
				topRefresh = true;
				bttmRefresh = true;
				vidRefresh = true;
	        }
    	},500,500);
    	
//    	new Timer().scheduleAtFixedRate(new TimerTask() {
//
//	        @Override
//	        public void run() {
//				vidRefresh = true;
//	        }
//    	},500,500);
    	
    	//graph refresher
    	topAnimTimer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				if(Constance.IS_CONNECTED && dataObserver !=null && topRefresh) {
					//Plot & Tracks		    		
		    		GraphicsContext gc = cTopL3.getGraphicsContext2D();
		    		gc.clearRect(0, 0, cTopL3.getWidth(), cTopL3.getHeight());
		    		
		    		dataObserver.getElTrackDataList().drawTracks(gc);
		    		dataObserver.getElPlotDataList().drawPlots(gc);
		    		
//		    		dataObserver.getElTrackDataList().drawTracksImage(cTopL2);
//		    		dataObserver.getElPlotDataList().drawPlotsImage(cTopL2);
		    		topRefresh = false;
//		        	logger.info("Top Graph Objects Refreshed");
				}
			}
		};
		
    	bttmAnimTimer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				if(Constance.IS_CONNECTED && dataObserver !=null && bttmRefresh) {
					//Plot & Tracks		    		
		    		GraphicsContext gc = cBtmL3.getGraphicsContext2D();
		    		gc.clearRect(0, 0, cBtmL3.getWidth(), cBtmL3.getHeight());
		    		
		    		dataObserver.getAzTrackDataList().drawTracks(gc);
		    		dataObserver.getAzPlotDataList().drawPlots(gc);
		    		
//		    		dataObserver.getAzTrackDataList().drawTracksImage(cBtmL2);
//		    		dataObserver.getAzPlotDataList().drawPlotsImage(cBtmL2);
		    		bttmRefresh = false;
//		        	logger.info("Bottom Graph Objects Refreshed");
				}
			}
		};
		
		rawAnimTimer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				if(Constance.IS_CONNECTED && dataObserver !=null && vidRefresh) {
					//Video
//					dataObserver.getElVideoDataList().drawVideosImage(cTopL2);		    		
//		    		dataObserver.getAzVideoDataList().drawVideosImage(cBtmL2);
		    		
					GraphicsContext tgc = cTopL2.getGraphicsContext2D();
		    		GraphicsContext bgc = cBtmL2.getGraphicsContext2D();
		    		
		    		tgc.clearRect(0, 0, cTopL2.getWidth(), cTopL2.getHeight());
		    		bgc.clearRect(0, 0, cBtmL2.getWidth(), cBtmL2.getHeight());
		    		
		    		dataObserver.getElVideoDataList().drawVideos(tgc);		    		
		    		dataObserver.getAzVideoDataList().drawVideos(bgc);
//		    		
//		    		for(SketchItemizedOverlay elVid:dataObserver.getElVideoDataList())
//		    			elVid.drawVideos(tgc);
//		    		for(SketchItemizedOverlay azVid:dataObserver.getAzVideoDataList())
//		    			azVid.drawVideos(bgc);
		    		
		    		vidRefresh = false;
//		        	logger.info("Video Objects Refreshed");
				}
			}
		};
		
		//start animation timers
		topAnimTimer.start();
		bttmAnimTimer.start();
		rawAnimTimer.start();
    }
	
	public void invalidate() {
		cTopL2.clear();
		cTopL3.clear();
		cTopL1.clear();
		cTopL0.clear();
		drawGraphTop(cTopL1);
		drawTextTop(cTopL0, translateTop, 0);
		logger.info("Top Chart Invalidated!");
		
		cBtmL2.clear();
		cBtmL3.clear();
		cBtmL1.clear();
		cBtmL0.clear();
		drawGraphBottom(cBtmL1);
		drawTextBottom(cBtmL0, translateBttm, 0);
		logger.info("Bottom Chart Invalidated!");
	}
	
	private void setDisplayScale(int val) {
				
		switch (val) {
		case 40:
			Constance.SCALE = " 40 "+Constance.UNITS.getLENGTH();
			matrixRef.setVisibleRange(40);
			btn_display40.setText(Constance.SCALE);
			btn_display5.widgetSetVal(VAL.DEFAULT);
			btn_display10.widgetSetVal(VAL.DEFAULT);
			btn_display20.widgetSetVal(VAL.DEFAULT);
			btn_display40.widgetSetVal(VAL.GREEN);
			break;
			
		case 20:
			Constance.SCALE = " 20 "+Constance.UNITS.getLENGTH();
			matrixRef.setVisibleRange(20);
			btn_display20.setText(Constance.SCALE);
			btn_display5.widgetSetVal(VAL.DEFAULT);
			btn_display10.widgetSetVal(VAL.DEFAULT);
			btn_display20.widgetSetVal(VAL.GREEN);
			btn_display40.widgetSetVal(VAL.DEFAULT);
			break;
			
		case 10:
			Constance.SCALE = " 10 "+Constance.UNITS.getLENGTH();
			matrixRef.setVisibleRange(10);
			btn_display10.setText(Constance.SCALE);
			btn_display5.widgetSetVal(VAL.DEFAULT);
			btn_display10.widgetSetVal(VAL.GREEN);
			btn_display20.widgetSetVal(VAL.DEFAULT);
			btn_display40.widgetSetVal(VAL.DEFAULT);
			break;
			
		case 5:
			Constance.SCALE = " 5 "+Constance.UNITS.getLENGTH();
			matrixRef.setVisibleRange(5);
			btn_display5.setText(Constance.SCALE);
			btn_display5.widgetSetVal(VAL.GREEN);
			btn_display10.widgetSetVal(VAL.DEFAULT);
			btn_display20.widgetSetVal(VAL.DEFAULT);
			btn_display40.widgetSetVal(VAL.DEFAULT);
			break;
		}
	}
	
	private void setDataDisplay(DATA data) {
		switch (data) {
		case RAW:
			btn_raw.widgetSetVal(VAL.DEFAULT);
			if(Constance.SHOW_RAW)
				btn_raw.widgetSetVal(VAL.GREEN);
			break;
			
		case PLOT:
			btn_plot.widgetSetVal(VAL.DEFAULT);
			if(Constance.SHOW_RAW)
				btn_plot.widgetSetVal(VAL.GREEN);
			break;

		case TRACK:
			break;
			
		case LABEL:
			btn_label.widgetSetVal(VAL.DEFAULT);
			if(Constance.SHOW_RAW)
				btn_label.widgetSetVal(VAL.GREEN);
			break;
			
		default:
			btn_raw.widgetSetVal(VAL.DEFAULT);
			if(Constance.SHOW_RAW)
				btn_raw.widgetSetVal(VAL.GREEN);
			
			btn_plot.widgetSetVal(VAL.DEFAULT);
			if(Constance.SHOW_RAW)
				btn_plot.widgetSetVal(VAL.GREEN);
			
			btn_label.widgetSetVal(VAL.DEFAULT);
			if(Constance.SHOW_RAW)
				btn_label.widgetSetVal(VAL.GREEN);
			break;
		}
	}
	
    private void drawGraphTop(Canvas canvas) {    	
    	mElevationChart = new ElevationChart(canvas);
    	mElevationChart.drawElevationLine();
    	mElevationChart.drawLandingStrip();
    	mElevationChart.drawRedDistanceLine();
    	mElevationChart.drawDistanceGrid();
    	mElevationChart.drawUnitsText();
    }
    
    private void drawTextTop(Canvas canvas, int x, int y) {
    	mElevationChart.drawText(canvas,x,y);
    }
    

	private void drawGraphBottom(Canvas canvas) {
		mAzimuthChart = new AzimuthChart(canvas);
		mAzimuthChart.drawAzimuthLine();
		mAzimuthChart.drawLandingStrip();
		mAzimuthChart.drawRedDistanceLine();
		mAzimuthChart.drawDistanceGrid();

	}
	
	private void drawTextBottom(Canvas canvas, int x, int y) {
    	mAzimuthChart.drawText(canvas, x, y);
    }
	
	public void refreshCanvas(DataObserver dObserver) {
		dataObserver = dObserver;
	}

	@Override
	public void draw(GraphicsContext gc) {
		
	}
	
	public void startDisplayAction() {
		if(!Constance.IS_DISPLAY_SETUP) {
			Alert alert = new Alert(AlertType.INFORMATION, "Do you like to configure Display Setup for the radar now " + "?", ButtonType.YES, ButtonType.NO);
			alert.setTitle("Alert");
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				displaySetup();
				actiontarget.setTextFill(Color.RED);
				actiontarget.setText("Please perform the Display setup accordingly!");
				actiontarget.setTextFill(Color.AQUAMARINE);
			} else {
				startShowingDisplay();
			}
		} else if(!Constance.IS_SYSTEM_SETUP) {
			Alert alert = new Alert(AlertType.INFORMATION, "Choose your System Setup. Do you want to change now ? ", ButtonType.YES, ButtonType.NO);
			alert.setTitle("Alert");
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				systemSetup();
				actiontarget.setTextFill(Color.RED);
				actiontarget.setText("Please perform the System setup accordingly!");
				actiontarget.setTextFill(Color.AQUAMARINE);
			} else {
				startShowingDisplay();
			}
		} else if(!Constance.IS_RADAR_SETUP) {
			Alert alert = new Alert(AlertType.INFORMATION, "Choose your Radar Setup. Do you want to change now ? ", ButtonType.YES, ButtonType.NO);
			alert.setTitle("Alert");
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				systemSetup();
				actiontarget.setTextFill(Color.RED);
				actiontarget.setText("Please perform the Radar setup accordingly!");
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

