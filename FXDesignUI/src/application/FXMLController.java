package application;

import java.net.URL;
import java.util.ResourceBundle;

import utils.Constance;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light.Point;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class FXMLController implements Initializable{
	
	final int HGAP = 20;
	final int OFFSET = 10;
	final int TEXT_OFFSET = 3*OFFSET;
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
	Thread mPlot;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		setNMparameter(10);
        drawGraphTop(cTopL1);
        updateObjects(cTopL2);
        cTopL2.toFront();
        
        drawGraphBottom(cBtmL1);
        updateObjects(cBtmL2);
        cBtmL2.toFront();
		
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
    	//init
    	GraphicsContext gc = canvas.getGraphicsContext2D();
    	//offset for drawing
    	double width = canvas.getWidth();
    	double height = canvas.getHeight();
    	final double HEIGHT_OFF = height-OFFSET;
    	final double WIDTH_OFF = width-OFFSET;
    	
    	
        //set BG & boundary
    	gc.clearRect(0, 0, width, height);
    	gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width,height);
        
        //custom graph
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);
        gc.scale(1, 1);
        gc.strokeLine(OFFSET,HEIGHT_OFF,WIDTH_OFF,HEIGHT_OFF);//flat line
        lineAtAngle(gc, OFFSET, HEIGHT_OFF, WIDTH_OFF+2*OFFSET, -20);//cross line at 20degrees
        
        //landing strip
        gc.setStroke(Color.AQUAMARINE);
        lineAtAngle(gc, OFFSET+NM, HEIGHT_OFF, OFFSET+(OFFSET-1)*NM, -5);//below center line
        lineAtAngle(gc, OFFSET+NM, HEIGHT_OFF, OFFSET+(OFFSET-1)*NM, -8);//above center line
        gc.setStroke(Color.RED);
        lineAtAngle(gc, OFFSET+NM, HEIGHT_OFF, OFFSET+OFFSET*NM, -6.5);//center red line
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
        gc.setLineDashes(OFFSET/2);
        lineAtAngle(gc, OFFSET+NM, HEIGHT_OFF, OFFSET+(OFFSET-1)*NM, -3);//imaginary below line
//        lineAtAngle(gc, OFFSET+NM, HEIGHT_OFF/2, OFFSET+(OFFSET-1)*NM, -5);//imaginary above line
        gc.setLineDashes(0);
        
        //inside grid
        gc.setFont(new Font("Sans Serif", 16));
        gc.setLineWidth(1.5);       
        Point p = getNextPoint(OFFSET, HEIGHT_OFF, ADJUST, -20);
        for(int i=0;i<(WIDTH_OFF/NM)-1;i++){
        	if(i==0) {
        		//write text
        		gc.setStroke(Color.YELLOW);
        		gc.strokeText(" TD ", OFFSET+(i+1)*NM, HEIGHT_OFF+OFFSET);
        		
        		//dotted red line
        		gc.setStroke(Color.RED);
        		gc.setLineDashes(OFFSET/2);
        		gc.strokeLine(OFFSET+NM+2*OFFSET,HEIGHT_OFF,p.getX()+2*OFFSET,p.getY()-OFFSET/2);
        		gc.setLineDashes(0);
        		
        		//reset color
        		gc.setStroke(Color.CHARTREUSE);
        	} else if((i%5)==0) {
        		//write text NM
        		gc.setStroke(Color.YELLOW);
        		gc.strokeText(i+"NM", OFFSET+(i+1)*NM, HEIGHT_OFF+OFFSET);
        	} else
        		gc.setStroke(Color.GREEN);

        	//draw green lines
            gc.strokeLine(OFFSET+(i+1)*NM,HEIGHT_OFF,p.getX(),p.getY());
            p = getNextPoint(p.getX(), p.getY(), ADJUST, -20);
        }
        
        //Loading Text context
        int count = 0;
        gc.setFont(new Font("Sans Serif", 16));
        gc.setStroke(Color.RED);
        gc.strokeText("EL Ang     : "+Constance.EL_ANGLE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        gc.setStroke(Color.YELLOW);
        gc.strokeText("AZ Tilt      : "+Constance.AZ_TILT, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;count++;
        
        gc.setFont(new Font("Sans Serif", 12));
        gc.setStroke(Color.ALICEBLUE);
        gc.strokeText("Glide Slope          : "+Constance.GLIDE_SLOPE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Safety Slope        : "+Constance.SAFETY_SLOPE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Safety Height      : "+Constance.SAFETY_HEIGHT, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;count++;
        gc.setStroke(Color.AQUA);
        gc.strokeText("Distance          : "+Constance.DISTANCE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Height            : "+Constance.HEIGHT, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        
        gc.setFont(new Font("Sans Serif", 14));
        gc.setStroke(Color.CADETBLUE);
        count = 0;
        gc.strokeText("Channel   : "+Constance.CHANNEL, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Control    : "+Constance.CONTROL, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Route      : "+Constance.ROUTE, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("RWY        : "+Constance.RWY, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Scale       : "+Constance.SCALE, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        
        gc.setFont(new Font("Sans Serif", 14));
        gc.setStroke(Color.GREENYELLOW);
        count = 0;
        gc.strokeText("System Perform     : ", 2*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("System Setting      : ", 2*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("System Logbook    : ", 2*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.setStroke(Color.YELLOW);
        count = 0;
        gc.strokeText(Constance.NULL, 2.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText(Constance.NULL, 2.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText(Constance.NULL, 2.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count=0;
    }
    
    private void updateObjects(Canvas canvas) {
    	//init
    	GraphicsContext gc = canvas.getGraphicsContext2D();
    	//offset for drawing
    	double width = canvas.getWidth();
    	double height = canvas.getHeight();
    	final double HEIGHT_OFF = height-OFFSET;
    	final double WIDTH_OFF = width-OFFSET;
    	
    	//update shape
        DoubleProperty x  = new SimpleDoubleProperty();
        DoubleProperty y  = new SimpleDoubleProperty();
//        Timeline timeline = new Timeline(
//            new KeyFrame(Duration.seconds(0),//Start X,Y at T
//                    new KeyValue(x, OFFSET),//Start X
//                    new KeyValue(y, OFFSET)//Start Y
//            ),
//            new KeyFrame(Duration.seconds(10),//End X,Y at T
//                    new KeyValue(x, WIDTH_OFF),//End X
//                    new KeyValue(y, HEIGHT_OFF)//End Y
//            )
//        );
//        timeline.setCycleCount(Timeline.INDEFINITE);//Loops
        
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	gc.clearRect(0, 0, width, height);
            	double myX = x.doubleValue();
            	double myY = y.doubleValue();
            	gc.setFill(Color.BISQUE);
            	gc.fillOval(myX-OFFSET, myY-OFFSET, HGAP, HGAP);
            	gc.setStroke(Color.WHITE);
            	gc.setLineWidth(2);
            	gc.strokeOval(myX-OFFSET, myY-OFFSET, HGAP, HGAP);
            	gc.strokeLine(myX,myY+HGAP,myX,myY-HGAP);
            	gc.strokeLine(myX+HGAP, myY, myX-HGAP, myY);
            	gc.setStroke(Color.CHOCOLATE);
            	lineAtAngle(gc, myX, myY, HGAP, -45);
            	Point p = getPointOfLineAtAngle(gc, myX, myY, HGAP, -45);
            	gc.strokeLine(p.getX(), p.getY(), p.getX()+2*TEXT_OFFSET, p.getY());
            	gc.setFont(new Font("Arial", 14));
            	gc.setStroke(Color.WHITE);
            	gc.strokeText("AA10", p.getX()+OFFSET, p.getY()-OFFSET);
            	gc.setStroke(Color.YELLOW);
            	gc.strokeText("3200/100", p.getX()+OFFSET, p.getY()+HGAP);
            }
        };
        timer.start();
//        timeline.play();
        
        mTask = new Runnable() {
			        	
			@Override
			public void run() {
				for(int i=0;i<WIDTH_OFF/2 && running;i++){
	                x.setValue(WIDTH_OFF-i);
	                y.setValue(OFFSET+i);
	                System.out.println("RUNNING");
	                try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
						running = false;
					}
        		}
			}
		};
        mPlot = new Thread(mTask);
        mPlot.start();
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
        lineAtAngle(gc, OFFSET, HEIGHT_OFF/2, WIDTH_OFF, -10.5);//cross line at top 10degrees
        lineAtAngle(gc, OFFSET, HEIGHT_OFF/2, WIDTH_OFF, 10.5);//cross line at bottom 10degrees
        
        //landing strip
        gc.setStroke(Color.AQUAMARINE);
        gc.strokeLine(OFFSET+NM,HEIGHT_OFF/2-OFFSET/2,OFFSET+OFFSET*NM,HEIGHT_OFF/2-OFFSET/2);//above center line
        gc.strokeLine(OFFSET+NM,HEIGHT_OFF/2+OFFSET/2,OFFSET+OFFSET*NM,HEIGHT_OFF/2+OFFSET/2);//below center line
        gc.setStroke(Color.RED);
        gc.strokeLine(OFFSET+NM,HEIGHT_OFF/2,OFFSET+(1+OFFSET)*NM,HEIGHT_OFF/2);//center line
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
        gc.setLineDashes(OFFSET/2);
        lineAtAngle(gc, OFFSET+NM, HEIGHT_OFF/2, OFFSET+(OFFSET-1)*NM, 5);//imaginary below line
        lineAtAngle(gc, OFFSET+NM, HEIGHT_OFF/2, OFFSET+(OFFSET-1)*NM, -5);//imaginary above line
        gc.setLineDashes(0);
        
        //inside grid
        gc.setLineWidth(1.5);    
        Point pTop = getNextPoint(OFFSET, HEIGHT_OFF/2, ADJUST, -10);
        Point pBtm = getNextPoint(OFFSET, HEIGHT_OFF/2, ADJUST, 10);
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
            pBtm = getNextPoint(pBtm.getX(), pBtm.getY(), ADJUST, 10);
            pTop = getNextPoint(pTop.getX(), pTop.getY(), ADJUST, -10);
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
	
	private void lineAtAngle(GraphicsContext gc, double x1,double y1,double length,double angle) {
		angle = angle * Math.PI / 180; 
	    gc.strokeLine(x1,y1,x1 + length * Math.cos(angle),y1 + length * Math.sin(angle));
	}
	
	private Point getPointOfLineAtAngle(GraphicsContext gc, double x1,double y1,double length,double angle) {
		angle = angle * Math.PI / 180; 
		double x2 = x1 + length * Math.cos(angle);
		double y2 = y1 + length * Math.sin(angle);
	    gc.strokeLine(x1,y1,x2,y2);
	    return new Point(x2,y2,0,null);
	}
	
	private Point getNextPoint(double x1, double y1, double len, double angle){
		angle = angle * Math.PI / 180;
		Point point = new Point();
		point.setX(x1+len*Math.cos(angle));
		point.setY(y1+len*Math.sin(angle));
		return point;
	}
//	
//	private Point calcIntersectionPoint(double x1, double y1, double x2, double y2){
//		Point point = new Point();
//		double s = Math.tan(-20);
//		point.setX(x2+s*(y1-y2));
//		point.setY(y2+s*(x2-x1));
//		return point;
//	}

	public void finish() {
		if(mPlot !=null) {
			try {
				running = false;
				mPlot.join();
				System.out.println("STOPPING");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
