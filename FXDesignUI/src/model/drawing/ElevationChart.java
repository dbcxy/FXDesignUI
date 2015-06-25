package model.drawing;

import utils.Constance;
import utils.ModelDrawing;
import views.ResizableCanvas;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ElevationChart implements ILayoutParam{
	
	double actualWidth;
	double actualHeight;
	double HEIGHT_OFF;
	double WIDTH_OFF;
	double elevationAngle;
	double distAngle;
	int dist;
	int deltaDist;
	
	private Canvas mCanvas;
	private GraphicsContext gc;
	
	private static ElevationChart instance;

    static {
        instance = new ElevationChart();
    }
    
    public static ElevationChart getInstance() {
        return instance;
    }
	
	public void init(Canvas canvas) {
		mCanvas = canvas;
    	gc = canvas.getGraphicsContext2D(); 
    	initBounds();
	}
	
	private void initBounds() {
		actualWidth = mCanvas.getWidth();
    	actualHeight = mCanvas.getHeight();
    	System.out.println("Draw: "+actualWidth+","+actualHeight);
    	HEIGHT_OFF = actualHeight-TEXT_OFFSET;
    	WIDTH_OFF = actualWidth-OFFSET;
	}
	
	public void drawBackground() {
    	gc.clearRect(0, 0, actualWidth, actualHeight);
    	gc.setFill(Color.BLACK);
        gc.fillRect(0,0,actualWidth, actualHeight);
	}
	
	public void drawElevationLine(double elAngle) {
		this.elevationAngle = elAngle;
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);
        gc.strokeLine(OFFSET,HEIGHT_OFF,WIDTH_OFF,HEIGHT_OFF);//flat line
        ModelDrawing.drawLineAtAngle(gc, OFFSET, HEIGHT_OFF, WIDTH_OFF+2*OFFSET, -elevationAngle);//cross line at 20 degrees
	}
	
	public void drawLandingStrip(int dist, double dAngle) {
		this.distAngle = dAngle;
        gc.setStroke(Color.AQUAMARINE);
        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle-1.5));//below center line
        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle+1.5));//above center line
        gc.setStroke(Color.RED);
        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF, OFFSET+OFFSET*dist, -distAngle);//center red line
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
//        gc.setLineDashes(OFFSET/2);
        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle-3.5));//imaginary below line
        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle-1.5));//imaginary above line
//        gc.setLineDashes(0);
	}
	
	public void drawDistanceGrid(int distance, int deltaDistance) {
		this.dist = distance;
		this.deltaDist = deltaDistance;
        gc.setFont(new Font("Sans Serif", 16));
        gc.setLineWidth(1.5);       
        Point p = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF, deltaDist, -elevationAngle);
        for(int i=0;i<(WIDTH_OFF/dist)-1;i++){
        	if(i==0) {
        		//write text
        		gc.setStroke(Color.YELLOW);
        		gc.strokeText(" TD ", OFFSET+(i+1)*dist, HEIGHT_OFF+HGAP);
        		
        		//dotted red line
        		gc.setStroke(Color.RED);
//        		gc.setLineDashes(OFFSET/2);
        		gc.strokeLine(OFFSET+dist+2*OFFSET,HEIGHT_OFF,p.getX()+2*OFFSET,p.getY()-OFFSET/2);
//        		gc.setLineDashes(0);
        		
        		//reset color
        		gc.setStroke(Color.CHARTREUSE);
        	} else if((i%5)==0) {
        		//write text NM
        		gc.setStroke(Color.YELLOW);
        		gc.strokeText(i+"NM", OFFSET+(i+1)*dist, HEIGHT_OFF+HGAP);
        	} else
        		gc.setStroke(Color.GREEN);

        	//draw green lines
            gc.strokeLine(OFFSET+(i+1)*dist,HEIGHT_OFF,p.getX(),p.getY());
            p = ModelDrawing.getNextPointAtAngle(p.getX(), p.getY(), deltaDist, -elevationAngle);
        }
	}
	
	public void drawText() {
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

	public void invalidate() {
		initBounds();
		drawBackground();
		drawElevationLine(elevationAngle);
		drawDistanceGrid(dist, deltaDist);
		drawLandingStrip(dist, distAngle);
		drawText();
		System.out.println("Invalidate");
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
	}
	
}
