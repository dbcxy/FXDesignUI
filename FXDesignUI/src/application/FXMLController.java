package application;

import java.net.URL;
import java.util.ResourceBundle;

import utils.Constance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;

public class FXMLController implements Initializable{
	
	@FXML private Label actiontarget;
	
	@FXML private Canvas chartTop;
	@FXML private Canvas chartBottom;
	
	final int HGAP = 20;
	final int OFFSET = 10;
	//1Nautical Miles = 18.5KMs
	//Graph Scale Since WIDTH_OFF = 880px
	int NM = 15;// Must be multiple of 11px
	int ADJUST;
    
    @FXML 
    protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("Button Clicked");
    }
    

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		setNMparameter(10);
		
    	GraphicsContext gcTop = chartTop.getGraphicsContext2D();
        drawGraphTop(gcTop, chartTop);
        
        GraphicsContext gcBtm = chartBottom.getGraphicsContext2D();
        drawGraphBottom(gcBtm,chartBottom);
		
	}
	
	private void setNMparameter(int valNM) {
		int index = 1;
		switch (valNM) {
		case 40:
			index = 1;
			break;
			
		case 20:
			index = 2;
			break;
			
		case 10:
			index = 4;
			break;
			
		case 5:
			index = 8;
			break;

		default:
			break;
		}

		NM = NM * index; 
		ADJUST = NM + index;
	}
	
    private void drawGraphTop(GraphicsContext gc, Canvas canvas) {
    	//offset for drawing
    	double width = canvas.getWidth();
    	double height = canvas.getHeight();
    	final double HEIGHT_OFF = height-OFFSET;
    	final double WIDTH_OFF = width-OFFSET;
    	System.out.println("WIDTH_OFF: "+WIDTH_OFF+",HEIGHT_OFF: "+HEIGHT_OFF);
    	
        //set BG & boundary
    	gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width,height);
        
        //custom graph
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);
        gc.scale(1, 1);
        gc.strokeLine(OFFSET,HEIGHT_OFF,WIDTH_OFF,HEIGHT_OFF);//flat line
        lineAtAngle(gc, OFFSET, HEIGHT_OFF, width+OFFSET, -20);//cross line at 20degrees
        
        //inside grid
        gc.setLineWidth(1.5);       
        Point p = getNextPoint(OFFSET, HEIGHT_OFF, ADJUST, -20);
        for(int i=0;i<(WIDTH_OFF/NM)-1;i++){
        	if((i%5)==0)
        		gc.setStroke(Color.YELLOW);
        	else
        		gc.setStroke(Color.GREEN);
            gc.strokeLine(OFFSET+(i+1)*NM,HEIGHT_OFF,p.getX(),p.getY());
            p = getNextPoint(p.getX(), p.getY(), ADJUST, -20);
        }
        
        //Loading Text context
        int count = 0;
        gc.setFont(new Font("Sans Serif", 16));
        gc.setStroke(Color.RED);
        gc.strokeText("EL Ang     : "+Constance.EL_ANGLE, OFFSET, OFFSET+HGAP*count);
        count++;
        gc.setStroke(Color.YELLOW);
        gc.strokeText("AZ Tilt      : "+Constance.AZ_TILT, OFFSET, OFFSET+HGAP*count);
        count++;count++;
        
        gc.setFont(new Font("Sans Serif", 12));
        gc.setStroke(Color.ALICEBLUE);
        gc.strokeText("Glide Slope          : "+Constance.GLIDE_SLOPE, OFFSET, OFFSET+HGAP*count);
        count++;
        gc.strokeText("Safety Slope        : "+Constance.SAFETY_SLOPE, OFFSET, OFFSET+HGAP*count);
        count++;
        gc.strokeText("Safety Height      : "+Constance.SAFETY_HEIGHT, OFFSET, OFFSET+HGAP*count);
        count++;count++;
        gc.setStroke(Color.AQUA);
        gc.strokeText("Distance          : "+Constance.DISTANCE, OFFSET, OFFSET+HGAP*count);
        count++;
        gc.strokeText("Height            : "+Constance.HEIGHT, OFFSET, OFFSET+HGAP*count);
        count++;
        
        gc.setFont(new Font("Sans Serif", 14));
        gc.setStroke(Color.CADETBLUE);
        count = 0;
        gc.strokeText("Channel   : "+Constance.CHANNEL, OFFSET*HGAP, OFFSET+HGAP*count);
        count++;
        gc.strokeText("Control    : "+Constance.CONTROL, OFFSET*HGAP, OFFSET+HGAP*count);
        count++;
        gc.strokeText("Route      : "+Constance.ROUTE, OFFSET*HGAP, OFFSET+HGAP*count);
        count++;
        gc.strokeText("RWY        : "+Constance.RWY, OFFSET*HGAP, OFFSET+HGAP*count);
        count++;
        gc.strokeText("Scale       : "+Constance.SCALE, OFFSET*HGAP, OFFSET+HGAP*count);
        count++;
        
        gc.setFont(new Font("Sans Serif", 14));
        gc.setStroke(Color.GREENYELLOW);
        count = 0;
        gc.strokeText("System Perform     : ", 2*OFFSET*HGAP, OFFSET+HGAP*count);
        count++;
        gc.strokeText("System Setting      : ", 2*OFFSET*HGAP, OFFSET+HGAP*count);
        count++;
        gc.strokeText("System Logbook    : ", 2*OFFSET*HGAP, OFFSET+HGAP*count);
        count++;
        gc.setStroke(Color.YELLOW);
        count = 0;
        gc.strokeText(Constance.NULL, 2.75*OFFSET*HGAP, OFFSET+HGAP*count);
        count++;
        gc.strokeText(Constance.NULL, 2.75*OFFSET*HGAP, OFFSET+HGAP*count);
        count++;
        gc.strokeText(Constance.NULL, 2.75*OFFSET*HGAP, OFFSET+HGAP*count);
        count=0;
        
    }
    
	private void drawGraphBottom(GraphicsContext gc, Canvas canvas) {
    	//offset for drawing
    	double width = canvas.getWidth();
    	double height = canvas.getHeight();
    	final double HEIGHT_OFF = height-OFFSET;
    	final double WIDTH_OFF = width-OFFSET;
    	
        //set BG
    	gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width,height);
        
        //custom graph
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);
        gc.scale(1, 1);
//        gc.strokeLine(OFFSET,HEIGHT_OFF/2,WIDTH_OFF,HEIGHT_OFF/2);//center line
        lineAtAngle(gc, OFFSET, HEIGHT_OFF/2, WIDTH_OFF, -10.5);//cross line at top 10degrees
        lineAtAngle(gc, OFFSET, HEIGHT_OFF/2, WIDTH_OFF, 10.5);//cross line at bottom 10degrees
        
        //inside grid
        gc.setLineWidth(1.5);       
        Point pTop = getNextPoint(OFFSET, HEIGHT_OFF/2, ADJUST, -10);
        Point pBtm = getNextPoint(OFFSET, HEIGHT_OFF/2, ADJUST, 10);
        for(int i=0;i<(WIDTH_OFF/NM)-1;i++){
        	if((i%5)==0)
        		gc.setStroke(Color.YELLOW);
        	else
        		gc.setStroke(Color.GREEN);
            gc.strokeLine(OFFSET+(i+1)*NM,pTop.getY(),OFFSET+(i+1)*NM,pBtm.getY());
            pBtm = getNextPoint(pBtm.getX(), pBtm.getY(), ADJUST, 10);
            pTop = getNextPoint(pTop.getX(), pTop.getY(), ADJUST, -10);
        }
        
        //Loading Text context
        int count = 0;
        gc.setFont(new Font("Sans Serif", 16));
        gc.setStroke(Color.RED);
        gc.strokeText("AZ Ang    : "+Constance.AZ_ANGLE, OFFSET, OFFSET+HGAP*count);
        count++;
        gc.setStroke(Color.YELLOW);
        gc.strokeText("EL Tilt      : "+Constance.EL_TILT, OFFSET, OFFSET+HGAP*count);
        count++;count++;
        
        gc.setFont(new Font("Sans Serif", 12));
        gc.setStroke(Color.ALICEBLUE);
        gc.strokeText("Approach Angle   : "+Constance.APPROACH_ANGLE, OFFSET, OFFSET+HGAP*count);
        count++;
        gc.setStroke(Color.AQUA);
        gc.strokeText("Offset                   : "+Constance.OFFSET, OFFSET, OFFSET+HGAP*count);
        count = 0;
        
	}
	
	private void lineAtAngle(GraphicsContext gc, double x1,double y1,double length,double angle) {
		angle = angle * Math.PI / 180;
	    gc.moveTo(x1, y1);
	    gc.lineTo(x1 + length * Math.cos(angle), y1 + length * Math.sin(angle));
	    gc.stroke();
	}
	
	private Point getNextPoint(double x1, double y1, double len, double angle){
		angle = angle * Math.PI / 180;
		Point point = new Point();
		point.setX(x1+len*Math.cos(angle));
		point.setY(y1+len*Math.sin(angle));
		return point;
	}
	
	private Point calcIntersectionPoint(double x1, double y1, double x2, double y2){
		Point point = new Point();
		double s = Math.tan(-20);
		point.setX(x2+s*(y1-y2));
		point.setY(y2+s*(x2-x1));
		return point;
	}
}
