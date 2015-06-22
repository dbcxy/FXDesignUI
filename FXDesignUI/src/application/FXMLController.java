package application;

import java.net.URL;
import java.util.ResourceBundle;

import model.DataObserver;
import model.drawable.Plot;
import model.drawable.Track;
import model.drawing.AzimuthChart;
import model.drawing.ElevationChart;
import model.drawing.ILayoutParam;

import org.apache.log4j.Logger;

import views.ResizableCanvas;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class FXMLController implements Initializable,ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(FXMLController.class);

	//1Nautical Miles = 18.5KMs
	//Graph Scale Since WIDTH_OFF = 880px
	final int PX = 15;// Assuming 40NM in so many pixels
	int NM;
	int ADJUST;
	double HEIGHT_OFF;
	double WIDTH_OFF;
	
	@FXML private Label actiontarget;
	@FXML private Button btn_display5NM;
	@FXML private Button btn_display10NM;
	@FXML private Button btn_display20NM;
	@FXML private Button btn_display40NM;
	
	@FXML private Pane chartTop;
	@FXML private Pane chartBottom;
	
	@FXML ResizableCanvas cTopL1;
	@FXML Canvas cTopL2;
	
	@FXML Canvas cBtmL1;
	@FXML Canvas cBtmL2;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		cTopL1.widthProperty().bind(chartTop.widthProperty());
		cTopL1.heightProperty().bind(chartTop.heightProperty());		
      
	}
	
	@FXML 
    protected void onStartAction(ActionEvent event) {
        actiontarget.setText("Start Button Clicked");
        logger.info("Top Chart initialization");
		setNMparameter(10);
        drawGraphTop(cTopL1);
        updateObjects(cTopL2);
        cTopL2.toFront();

        logger.info("Bottom Chart initialization");
        drawGraphBottom(cBtmL1);
        updateObjects(cBtmL2);
        cBtmL2.toFront();
        

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
    	ElevationChart mElevationChart = new ElevationChart(canvas);
    	mElevationChart.drawBackground();
    	mElevationChart.drawElevationLine(20);
    	mElevationChart.drawLandingStrip(NM, 6.5);
    	mElevationChart.drawDistanceGrid(NM, ADJUST);
    	mElevationChart.drawText();

    }
    
    private void updateObjects(Canvas canvas) {
    	Track mTrack1 = new Track();
    	Track mTrack2 = new Track();
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
            	mTrack1.draw(gc);
            	
            	mTrack2.setXY(x.doubleValue()-50, y.doubleValue()+50);
            	mTrack2.setText("AA11", "32/10");
            	mTrack2.draw(gc);
            	
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
		AzimuthChart mAzimuthChart = new AzimuthChart(canvas);
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
