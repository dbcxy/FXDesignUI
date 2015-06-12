package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;

public class FXMLController implements Initializable{
	
	@FXML private Label actiontarget;
	
	@FXML private Canvas chartTop;
	@FXML private Canvas chartBottom;
	
	final int OFFSET = 10;
	//1Nautical Miles = 18.5KMs
	//Graph Scale Since WIDTH_OFF = 880px, Consider each 1NM = 110px, so 11NMs lines are visible
	final int NM = 110;
    
    @FXML 
    protected void handleSubmitButtonAction(ActionEvent event) {

        actiontarget.setText("Button Clicked");
    }
    

	@Override
	public void initialize(URL url, ResourceBundle rb) {
    	GraphicsContext gcTop = chartTop.getGraphicsContext2D();
        drawGraphTop(gcTop, chartTop);
        
        GraphicsContext gcBtm = chartBottom.getGraphicsContext2D();
        drawGraphBottom(gcBtm,chartBottom);
		
	}
	
    private void drawGraphTop(GraphicsContext gc, Canvas canvas) {
    	//offset for drawing
    	double width = canvas.getWidth();
    	double height = canvas.getHeight();
    	final double HEIGHT_OFF = height-OFFSET;
    	final double WIDTH_OFF = width-OFFSET;
    	System.out.println("WID: "+WIDTH_OFF+"HIE: "+HEIGHT_OFF);
    	
        //set BG & boundary
    	gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width,height);
        
        //custom graph
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);
        gc.scale(1, 1);
        gc.strokeLine(OFFSET,HEIGHT_OFF,WIDTH_OFF,HEIGHT_OFF);//flat line
//        gc.strokeLine(OFFSET,HEIGHT_OFF,WIDTH_OFF*0.75,OFFSET);//cross line
//        gc.strokeLine(WIDTH_OFF*0.75,OFFSET,WIDTH_OFF,OFFSET);//cross finish line
        double currX,currY,nextX,nextY;
        currX = OFFSET;
        currY = HEIGHT_OFF;
        for(int i=OFFSET;i<WIDTH_OFF;i++){
        	nextX = currX+i+1;
        	nextY = currY-i-1;
        	if(nextY <= OFFSET){
        		gc.strokeLine(currX,currY,WIDTH_OFF,OFFSET);//cross line
        		break;
        	}        		
        	gc.strokeLine(currX,currY,nextX,nextY);//cross line
        	currX = nextX;
        	currY = nextY;
        }
        
        
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(1.5);
//        gc.strokeLine(OFFSET+NM,HEIGHT_OFF,OFFSET+NM,HEIGHT_OFF+NM);
        
//        gc.fillOval(10, 60, 30, 30);
//        gc.strokeOval(60, 60, 30, 30);
//        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
//        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
//        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
//        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
//        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
//        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
//        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
//        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
//        gc.fillPolygon(new double[]{10, 40, 10, 40},
//                       new double[]{210, 210, 240, 240}, 4);
//        gc.strokePolygon(new double[]{60, 90, 60, 90},
//                         new double[]{210, 210, 240, 240}, 4);
//        gc.strokePolyline(new double[]{110, 140, 110, 140},
//                          new double[]{210, 210, 240, 240}, 4);
    }
    
	private void drawGraphBottom(GraphicsContext gc, Canvas canvas) {
    	//offset for drawing
    	double width = canvas.getWidth();
    	double height = canvas.getHeight();
    	final int OFFSET = 20;
    	final double HEIGHT_OFF = height-OFFSET;
    	final double WIDTH_OFF = width-OFFSET;
    	
        //set BG
    	gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width,height);
        
        //custom graph
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);        
//        gc.strokeLine(OFFSET,HEIGHT_OFF/2,WIDTH_OFF,HEIGHT_OFF/2);//center line
        
        gc.strokeLine(OFFSET,HEIGHT_OFF/2,WIDTH_OFF*0.75,OFFSET);//top cross line
        gc.strokeLine(WIDTH_OFF*0.75,OFFSET,WIDTH_OFF,OFFSET);//cross finish line
        
        gc.strokeLine(OFFSET,HEIGHT_OFF/2,WIDTH_OFF*0.75,HEIGHT_OFF);//bottom cross line
        gc.strokeLine(WIDTH_OFF*0.75,HEIGHT_OFF,WIDTH_OFF,HEIGHT_OFF);//cross finish line
        
	}
	
	private void lineAtAngle(GraphicsContext gc,double x1,double y1,double length,double angle) {
	    gc.moveTo(x1, y1);
	    gc.lineTo(x1 + length * Math.cos(angle), y1 + length * Math.sin(angle));
	}
}
