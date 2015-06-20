package application;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import model.drawable.Plot;
import model.drawable.Track;
import model.drawing.ElevationChart;
import model.drawing.ILayoutParam;

import org.apache.log4j.Logger;

import utils.Constance;
import utils.ModelDrawing;
import utils.Test;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
	
	@FXML Canvas cTopL1;
	@FXML Canvas cTopL2;
	@FXML Canvas cBtmL1;
	@FXML Canvas cBtmL2;
	

	volatile boolean running = true;
	Runnable mTask;
	Thread mThread;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
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
	
    private void drawGraphTop(Canvas canvas) {    	
    	ElevationChart mElevationChart = new ElevationChart(canvas);
    	mElevationChart.drawBackground();
    	mElevationChart.drawElevationLine(20);
    	mElevationChart.drawLandingStrip(NM, 6.5);
    	mElevationChart.drawDistancegrid(NM, ADJUST);
    	mElevationChart.drawText();

    }
    
    private void updateObjects(Canvas canvas) {
    	Track mTrack = new Track(canvas);
    	Plot mPlot = new Plot(canvas);

    	final double WIDTH_OFF = canvas.getWidth()-OFFSET;
    	
    	//update shape
        DoubleProperty x  = new SimpleDoubleProperty();
        DoubleProperty y  = new SimpleDoubleProperty();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            	mTrack.setXY(x.doubleValue(), y.doubleValue());
            	mTrack.setText("AA10", "3200/100");
            	mTrack.drawTrack();
            	
            	mPlot.setXY(x.doubleValue()-50, y.doubleValue()+50);
            	mPlot.setTitle("PLOT");
            	mPlot.drawPlot();
            	
            }
        };
        timer.start();
        
        mTask = new Runnable() {
			        	
			@Override
			public void run() {
				for(int i=0;i<WIDTH_OFF/2 && running;i++){
	                x.setValue(WIDTH_OFF-i);
	                y.setValue(OFFSET+i);
	                try {
						Thread.sleep(50);//Update or refresh rate
					} catch (InterruptedException e) {
						e.printStackTrace();
						running = false;
					}
        		}
			}
		};
		mThread = new Thread(mTask);
		mThread.start();
    }
    
	private void drawGraphBottom(Canvas canvas) {
		//init
		GraphicsContext gc = canvas.getGraphicsContext2D();
    	//offset for drawing
    	double width = canvas.getWidth();
    	double height = canvas.getHeight();
    	final double HEIGHT_OFF = height-OFFSET;
    	final double WIDTH_OFF = width-OFFSET;
    	
        //set BG
    	gc.clearRect(0, 0, width, height);
    	gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width,height);
        
        //custom graph
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);
        gc.scale(1, 1);
        ModelDrawing.drawLineAtAngle(gc, OFFSET, HEIGHT_OFF/2, WIDTH_OFF, -10.5);//cross line at top 10degrees
        ModelDrawing.drawLineAtAngle(gc, OFFSET, HEIGHT_OFF/2, WIDTH_OFF, 10.5);//cross line at bottom 10degrees
        
        //landing strip
        gc.setStroke(Color.AQUAMARINE);
        gc.strokeLine(OFFSET+NM,HEIGHT_OFF/2-OFFSET/2,OFFSET+OFFSET*NM,HEIGHT_OFF/2-OFFSET/2);//above center line
        gc.strokeLine(OFFSET+NM,HEIGHT_OFF/2+OFFSET/2,OFFSET+OFFSET*NM,HEIGHT_OFF/2+OFFSET/2);//below center line
        gc.setStroke(Color.RED);
        gc.strokeLine(OFFSET+NM,HEIGHT_OFF/2,OFFSET+(1+OFFSET)*NM,HEIGHT_OFF/2);//center line
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
        gc.setLineDashes(OFFSET/2);
        ModelDrawing.drawLineAtAngle(gc, OFFSET+NM, HEIGHT_OFF/2, OFFSET+(OFFSET-1)*NM, 5);//imaginary below line
        ModelDrawing.drawLineAtAngle(gc, OFFSET+NM, HEIGHT_OFF/2, OFFSET+(OFFSET-1)*NM, -5);//imaginary above line
        gc.setLineDashes(0);
        
        //inside grid
        gc.setLineWidth(1.5);    
        Point pTop = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF/2, ADJUST, -10);
        Point pBtm = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF/2, ADJUST, 10);
        for(int i=0;i<(WIDTH_OFF/NM)-1;i++){
        	if(i==0) {
        		
        		//dotted red line
        		gc.setStroke(Color.RED);
        		gc.setLineDashes(OFFSET/2);
        		gc.strokeLine(OFFSET+NM+2*OFFSET,pTop.getY(),OFFSET+NM+2*OFFSET,pBtm.getY());
        		gc.setLineDashes(0);
        		
        		//reset color
        		gc.setStroke(Color.CHARTREUSE);
        	} else if((i%5)==0) {
        		gc.setStroke(Color.YELLOW);
        	} else
        		gc.setStroke(Color.GREEN);
            gc.strokeLine(OFFSET+(i+1)*NM,pTop.getY(),OFFSET+(i+1)*NM,pBtm.getY());
            pBtm = ModelDrawing.getNextPointAtAngle(pBtm.getX(), pBtm.getY(), ADJUST, 10);
            pTop = ModelDrawing.getNextPointAtAngle(pTop.getX(), pTop.getY(), ADJUST, -10);
        }
        
        //Loading Text context
        int count = 0;
        gc.setFont(new Font("Sans Serif", 16));
        gc.setStroke(Color.RED);
        gc.strokeText("AZ Ang    : "+Constance.AZ_ANGLE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        gc.setStroke(Color.YELLOW);
        gc.strokeText("EL Tilt      : "+Constance.EL_TILT, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;count++;
        
        gc.setFont(new Font("Sans Serif", 12));
        gc.setStroke(Color.ALICEBLUE);
        gc.strokeText("Approach Angle   : "+Constance.APPROACH_ANGLE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        gc.setStroke(Color.AQUA);
        gc.strokeText("Offset                   : "+Constance.OFFSET, OFFSET, TEXT_OFFSET+HGAP*count);
        count = 0;
        
        gc.setFont(new Font("Sans Serif", 18));
        gc.setStroke(Color.YELLOW);
        gc.strokeText("-", OFFSET, HEIGHT_OFF/2+HGAP+OFFSET);
        gc.strokeText("+", OFFSET, HEIGHT_OFF/2-HGAP);
        
	}

	public void finish() {
		if(mThread !=null) {
			try {
				running = false;
				mThread.join();
				System.out.println("STOPPING");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
