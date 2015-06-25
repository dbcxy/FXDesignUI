package model.drawing;

import utils.Constance;
import utils.ModelDrawing;
import views.ResizableCanvas;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AzimuthChart implements ILayoutParam {
	
	double actualWidth;
	double actualHeight;
	double HEIGHT_OFF;
	double WIDTH_OFF;
	double azimuthAngle;
	int dist;
	int deltaDist;
	
	private Canvas mCanvas;
	private GraphicsContext gc;
	
	private static AzimuthChart instance;

    static {
        instance = new AzimuthChart();
    }
    
    public static AzimuthChart getInstance() {
        return instance;
    }
	
	public void init(Canvas canvas) {
		mCanvas = canvas;
    	gc = canvas.getGraphicsContext2D();    	
    	actualWidth = mCanvas.getWidth();
    	actualHeight = mCanvas.getHeight();
    	HEIGHT_OFF = actualHeight-TEXT_OFFSET;
    	WIDTH_OFF = actualWidth-OFFSET;
	}
	
	public void drawBackground() {
    	gc.clearRect(0, 0, actualWidth, actualHeight);
    	gc.setFill(Color.BLACK);
        gc.fillRect(0,0,actualWidth, actualHeight);
	}
	
	public void drawAzimuthLine(double azAngle) {
		azimuthAngle = azAngle;
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);
        ModelDrawing.drawLineAtAngle(gc, OFFSET, HEIGHT_OFF/2, WIDTH_OFF, -azAngle);//cross line at top az degrees
        ModelDrawing.drawLineAtAngle(gc, OFFSET, HEIGHT_OFF/2, WIDTH_OFF, azAngle);//cross line at bottom az degrees
	}
	
	public void drawLandingStrip(int dist) {
        gc.setStroke(Color.AQUAMARINE);
        gc.strokeLine(OFFSET+dist,HEIGHT_OFF/2-OFFSET/2,OFFSET+OFFSET*dist,HEIGHT_OFF/2-OFFSET/2);//above center line
        gc.strokeLine(OFFSET+dist,HEIGHT_OFF/2+OFFSET/2,OFFSET+OFFSET*dist,HEIGHT_OFF/2+OFFSET/2);//below center line
        gc.setStroke(Color.RED);
        gc.strokeLine(OFFSET+dist,HEIGHT_OFF/2,OFFSET+(1+OFFSET)*dist,HEIGHT_OFF/2);//center line
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
//        gc.setLineDashes(OFFSET/2);
        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF/2, OFFSET+(OFFSET-1)*dist, (azimuthAngle-0.5)/2);//imaginary below line
        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF/2, OFFSET+(OFFSET-1)*dist, -(azimuthAngle-0.5)/2);//imaginary above line
//        gc.setLineDashes(0);
	}
	
	public void drawDistanceGrid(int distance, int deltaDistance) {
		dist = distance;
		deltaDist = deltaDistance;
		gc.setLineWidth(1.5);    
        Point pTop = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF/2, deltaDist, -Math.floor(azimuthAngle));
        Point pBtm = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF/2, deltaDist, Math.floor(azimuthAngle));
        for(int i=0;i<(WIDTH_OFF/dist)-1;i++){
        	if(i==0) {
        		
        		//dotted red line
        		gc.setStroke(Color.RED);
//        		gc.setLineDashes(OFFSET/2);
        		gc.strokeLine(OFFSET+dist+2*OFFSET,pTop.getY(),OFFSET+dist+2*OFFSET,pBtm.getY());
//        		gc.setLineDashes(0);
        		
        		//reset color
        		gc.setStroke(Color.CHARTREUSE);
        	} else if((i%5)==0) {
        		gc.setStroke(Color.YELLOW);
        	} else
        		gc.setStroke(Color.GREEN);
            gc.strokeLine(OFFSET+(i+1)*dist,pTop.getY(),OFFSET+(i+1)*dist,pBtm.getY());
            pBtm = ModelDrawing.getNextPointAtAngle(pBtm.getX(), pBtm.getY(), deltaDist, Math.floor(azimuthAngle));
            pTop = ModelDrawing.getNextPointAtAngle(pTop.getX(), pTop.getY(), deltaDist, -Math.floor(azimuthAngle));
        }
	}
	
	public void drawText() {
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
	
	public void invalidate() {
		drawBackground();
		drawAzimuthLine(azimuthAngle);
		drawDistanceGrid(dist, deltaDist);
		drawLandingStrip(dist);
		drawText();
	}

	@Override
	public void draw(GraphicsContext gc) {
		
	}

}
