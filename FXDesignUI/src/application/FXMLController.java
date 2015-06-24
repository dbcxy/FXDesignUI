package application;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import model.DataObserver;
import model.drawable.Plot;
import model.drawable.Track;
import model.drawing.AzimuthChart;
import model.drawing.ElevationChart;
import model.drawing.ILayoutParam;

import org.apache.log4j.Logger;

import utils.Constance;
import views.Console;
import views.ResizableCanvas;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.stage.Stage;

public class FXMLController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(FXMLController.class);
	
	@FXML private MenuBar fxMenuBar;
	
	@FXML private Label actiontarget;
	
	@FXML private TextField textTime;
	@FXML private TextField textDate;
	@FXML private TextArea consoleOutput;
	@FXML private TextField consoleInput;
	
	@FXML private Button btn_display5NM;
	@FXML private Button btn_display10NM;
	@FXML private Button btn_display20NM;
	@FXML private Button btn_display40NM;
	
	@FXML private ScrollPane chartTopScroll;
	@FXML private ScrollPane chartBottomScroll;
	@FXML private Pane chartTop;
	@FXML private Pane chartBottom;
	
	@FXML ResizableCanvas cTopL1;
	@FXML ResizableCanvas cTopL2;
	@FXML ResizableCanvas cTopL3;
	
	@FXML ResizableCanvas cBtmL1;
	@FXML ResizableCanvas cBtmL2;
	@FXML ResizableCanvas cBtmL3;
	
	@FXML Slider sliderZoomTop;
	@FXML Slider sliderZoomBottom;
	

	//1Nautical Miles = 18.5KMs
	//Graph Scale Since WIDTH_OFF = 880px
	final int PX = 15;// Assuming 40NM in so many pixels
	int NM;
	int ADJUST;
	double HEIGHT_OFF;
	double WIDTH_OFF;
	boolean isClose = false;
	private double pressedX, pressedY;
	
	ElevationChart mElevationChart;
	AzimuthChart mAzimuthChart;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {		
		initCanvasLayout();
		actiontarget.setText("Start the System....");
	}
	
	@FXML 
    protected void onStartAction(ActionEvent event) {
		initSystem();		
		initTopChart();
		initBottomChart();
		actiontarget.setText("System Loaded!");

        //TESTING
//        long startTime = System.currentTimeMillis();
//        //drawing all pixel with Rect size 1x1
//        Test.gcDrawEveryPixelRect(cBtmL1);//470ms-528ms 
//        Test.g2dImgDrawEveryPixelRect(cBtmL1);//about 201ms 
//        //drawing/filling single Rect size 100x100
//        Test.gcDrawSingleRect(cBtmL1);//about 5ms 
//        Test.g2dImgDrawSingleRect(cBtmL1);//about 51ms 
//        long endTime = System.currentTimeMillis();
//        System.out.println("TotalTime: "+(endTime - startTime));  
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
    	isClose = true;
    	stage.close();
    	logger.info("APPLICATION CLOSED");
    }
    
    private void initCanvasLayout() {
    	cTopL1.widthProperty().bind(chartTop.widthProperty());
		cTopL1.heightProperty().bind(chartTop.heightProperty());
		
		cTopL2.widthProperty().bind(chartTop.widthProperty());
		cTopL2.heightProperty().bind(chartTop.heightProperty());
		
//		cTopL3.widthProperty().bind(chartTop.widthProperty());
//		cTopL3.heightProperty().bind(chartTop.heightProperty());
		
		cBtmL1.widthProperty().bind(chartBottom.widthProperty());
		cBtmL1.heightProperty().bind(chartBottom.heightProperty());
		
		cBtmL2.widthProperty().bind(chartBottom.widthProperty());
		cBtmL2.heightProperty().bind(chartBottom.heightProperty());
		
//		cBtmL3.widthProperty().bind(chartBottom.widthProperty());
//		cBtmL3.heightProperty().bind(chartBottom.heightProperty());
      
    }
    
    private void initSystem() {
    	recoverLayoutChanges();
    	initTimeDate();
    	initConsole();
    }
    
    private void recoverLayoutChanges() {
		cTopL1.setScaledDimension(false);
		cTopL2.setScaledDimension(false);
		cTopL3.setScaledDimension(false);
		
		cBtmL1.setScaledDimension(false);
		cBtmL2.setScaledDimension(false);
		cBtmL3.setScaledDimension(false);
    }
    
    private void initTopChart() {
		setNMparameter(10);
        drawGraphTop(cTopL1);
        updateObjects(cTopL2);
        initZoomSlider(sliderZoomTop, chartTop, chartTopScroll, cTopL1, cTopL2, cTopL3);
    	logger.info("Top Chart initialization");
	}
    
    private void initBottomChart() {
        drawGraphBottom(cBtmL1);
        updateObjects(cBtmL2);
        initZoomSlider(sliderZoomBottom, chartBottom, chartBottomScroll);
        logger.info("Bottom Chart initialization");
    }
    
    private void initZoomSlider(final Slider slider, final Pane pane, final ScrollPane scrollPane,
    		ResizableCanvas...canvas) {
    	slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				
				pane.setScaleX(newValue.doubleValue());
				pane.setScaleY(newValue.doubleValue());
				
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
				
				if(newValue.intValue()==1) {
					pane.setScaleX(1.0);
					pane.setScaleY(1.0);
					pane.setTranslateX(0.0);
					pane.setTranslateY(0.0);
					pane.setTranslateZ(0.0);
				}
				
				scrollPane.setPrefWidth(pane.getWidth()*pane.getScaleX());
	    		scrollPane.setPrefHeight(pane.getHeight()*pane.getScaleY());
	    		pane.resize(scrollPane.getPrefWidth(),
	    				scrollPane.getPrefHeight());
	    		
	    		System.out.println("Pane: "+pane.getWidth()+","+pane.getHeight());
//	    		for(int i=0;i<canvas.length;i++){
//	    			canvas[i].setScaledWidth(scrollPane.getPrefWidth());
//	    			canvas[i].setScaledHeight(scrollPane.getPrefHeight());
//	    		}
	    		
	    		new Timer().schedule(new TimerTask() {

	    			        @Override
	    			        public void run() {
	    						Platform.runLater(new Runnable() { 
	    						      
	    					    	@Override 
	    					    	public void run() {			    		
	    					    		for(int i=0;i<canvas.length;i++){
	    					    			pane.getChildren().remove(i);
	    					    			pane.getChildren().add(new ResizableCanvas());
	    					    		}
	    					    		
//	    								mElevationChart.invalidate();
//	    								mAzimuthChart.invalidate();
//	    								updateObjects(cTopL2);
//	    								updateObjects(cBtmL2);
	    					    	}
	    					    });
	    			        }
	    			    }, 2000);
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
				while(!isClose) {
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
	
	private void invalidate() {		
		drawGraphTop(cTopL1);
		drawGraphBottom(cBtmL1);
	}
	
	private void setNMparameter(int valNM) {
		int index = 1;
		btn_display5NM.setDefaultButton(false);
		btn_display10NM.setDefaultButton(false);
		btn_display20NM.setDefaultButton(false);
		btn_display40NM.setDefaultButton(false);
		switch (valNM) {
		case 40:
			index = 1;
			btn_display40NM.setDefaultButton(true);
			break;
			
		case 20:
			index = 2;
			btn_display20NM.setDefaultButton(true);
			break;
			
		case 10:
			index = 4;
			btn_display10NM.setDefaultButton(true);
			break;
			
		case 5:
			index = 8;
			btn_display5NM.setDefaultButton(true);
			break;

		default:
			break;
		}

		NM = PX * index; 
		ADJUST = NM + index;
	}
	
    private void drawGraphTop(ResizableCanvas canvas) {    	
    	mElevationChart = ElevationChart.getInstance();
    	mElevationChart.init(canvas);
    	mElevationChart.drawBackground();
    	mElevationChart.drawElevationLine(20);
    	mElevationChart.drawLandingStrip(NM, 6.5);
    	mElevationChart.drawDistanceGrid(NM, ADJUST);
    	mElevationChart.drawText();

    }
    
    private void updateObjects(ResizableCanvas canvas) {
    	Track mTrack1 = new Track();
    	Track mTrack2 = new Track();
    	Track mTrack3 = new Track();
    	Plot mPlot1 = new Plot();
    	Plot mPlot2 = new Plot();
    	GraphicsContext gc = canvas.getGraphicsContext2D();
    	
    	final double WIDTH_OFF = canvas.getScaledWidth()-OFFSET;
    	
    	//update shape
        DoubleProperty x  = new SimpleDoubleProperty();
        DoubleProperty y  = new SimpleDoubleProperty();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getScaledWidth(), canvas.getScaledHeight());
            	mTrack1.setXY(x.doubleValue(), y.doubleValue());
            	mTrack1.setText("AA10", "3200/100");
            	mTrack1.draw(gc);
            	
            	mTrack2.setXY(x.doubleValue()-50, y.doubleValue()+50);
            	mTrack2.setText("AA11", "32/10");
            	mTrack2.draw(gc);
            	
            	mTrack3.setXY( 50, 50);
            	mTrack3.setText("AB", "3/10");
            	mTrack3.draw(canvas.getGraphicsContext2D());
            	
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
    
	private void drawGraphBottom(ResizableCanvas canvas) {
		mAzimuthChart = AzimuthChart.getInstance();
		mAzimuthChart.init(canvas);
		mAzimuthChart.drawBackground();
		mAzimuthChart.drawAzimuthLine(10.5);
		mAzimuthChart.drawLandingStrip(NM);
		mAzimuthChart.drawDistanceGrid(NM, ADJUST);
		mAzimuthChart.drawText();
	}
	
	public void refreshCanvas(DataObserver dataObserver) {
		GraphicsContext gc = cTopL2.getGraphicsContext2D();
		gc.clearRect(0, 0, cTopL2.getWidth(), cTopL2.getHeight());
		dataObserver.getTrackDataList().draw(gc);
		dataObserver.getPlotDataList().draw(gc);
    	logger.info("Canvas Objects Redrawn");
	}

	@Override
	public void draw(GraphicsContext gc) {
		
	}
	
}
