package model.graph;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;

import utils.Constance;
import utils.ModelDrawing;
import views.ResizableCanvas;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
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
	
	private BufferedImage bufferedImage;
	private Graphics2D g2d;
	
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
    	HEIGHT_OFF = actualHeight-TEXT_OFFSET;
    	WIDTH_OFF = actualWidth-OFFSET;
    	
    	bufferedImage = new BufferedImage((int) (actualWidth), 
				 (int) actualHeight, BufferedImage.TYPE_INT_RGB);
		g2d = bufferedImage.createGraphics();
	}
	
	public void setScaleCanvas(double d) {
		mCanvas.setScaleX(d);
		mCanvas.setScaleY(d);
	}
	
	public void drawBackgroundOnImage() {
		g2d.clearRect(0, 0, (int)actualWidth, (int)actualHeight);
    	g2d.setColor(java.awt.Color.BLACK);
        g2d.fillRect(0,0,(int)actualWidth, (int)actualHeight);
	}
	
	public void drawBackground() {
    	gc.clearRect(0, 0, actualWidth, actualHeight);
    	gc.setFill(Color.BLACK);
        gc.fillRect(0,0,actualWidth, actualHeight);
	}
	
	public void drawElevationLineOnImage(double elAngle) {
		this.elevationAngle = elAngle;
        g2d.setColor(java.awt.Color.CYAN);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(OFFSET,(int)HEIGHT_OFF,(int)WIDTH_OFF,(int)HEIGHT_OFF);//flat line
        ModelDrawing.drawLineAtAngle(g2d, OFFSET, (int)HEIGHT_OFF,(int) WIDTH_OFF+2*OFFSET, -elevationAngle);//cross line at 20 degrees
	}
	
	public void drawElevationLine(double elAngle) {
		this.elevationAngle = elAngle;
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);
        gc.strokeLine(OFFSET,HEIGHT_OFF,WIDTH_OFF,HEIGHT_OFF);//flat line
        ModelDrawing.drawLineAtAngle(gc, OFFSET, HEIGHT_OFF,WIDTH_OFF+2*OFFSET, -elevationAngle);//cross line at 20 degrees
	}
	
	public void drawLandingStripOnImage(int dist, double dAngle) {
		this.distAngle = dAngle;
        g2d.setColor(java.awt.Color.LIGHT_GRAY);
        ModelDrawing.drawLineAtAngle(g2d, OFFSET+dist, (int)HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle-1.5));//below center line
        ModelDrawing.drawLineAtAngle(g2d, OFFSET+dist, (int)HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle+1.5));//above center line
        g2d.setColor(java.awt.Color.RED);
        ModelDrawing.drawLineAtAngle(g2d, OFFSET+dist, (int)HEIGHT_OFF, OFFSET+OFFSET*dist, -distAngle);//center red line
        g2d.setColor(java.awt.Color.CYAN);
        g2d.setStroke(new BasicStroke(1));
//        gc.setLineDashes(OFFSET/2);
        ModelDrawing.drawLineAtAngle(g2d, OFFSET+dist, (int)HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle-3.5));//imaginary below line
        ModelDrawing.drawLineAtAngle(g2d, OFFSET+dist, (int)HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle+3.5));//imaginary above line
//        gc.setLineDashes(0);
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
        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle+3.5));//imaginary above line
//        gc.setLineDashes(0);
	}
	
	public void drawDistanceGridOnImage(int distance, int deltaDistance) {
		this.dist = distance;
		this.deltaDist = deltaDistance;
        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 16));
        g2d.setStroke(new BasicStroke((float) 1.5));       
        Point p = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF, deltaDist, -elevationAngle);
        for(int i=0;i<(WIDTH_OFF/dist)-1;i++){
        	if(i==0) {
        		//write text
        		g2d.setColor(java.awt.Color.YELLOW);
        		g2d.drawString(" TD ", OFFSET+(i+1)*dist, (int) (HEIGHT_OFF+HGAP));
        		
        		//dotted red line
        		g2d.setColor(java.awt.Color.RED);
//        		gc.setLineDashes(OFFSET/2);
        		g2d.drawLine(OFFSET+dist+2*OFFSET,(int)HEIGHT_OFF,(int)p.getX()+2*OFFSET,(int)p.getY()-OFFSET/2);
//        		gc.setLineDashes(0);
        		
        		//reset color
        		g2d.setColor(java.awt.Color.GRAY);
        	} else if((i%5)==0) {
        		//write text NM
        		g2d.setColor(java.awt.Color.YELLOW);
        		g2d.drawString(i+"NM", OFFSET+(i+1)*dist, (int) (HEIGHT_OFF+HGAP));
        	} else
        		g2d.setColor(java.awt.Color.GREEN);

        	//draw green lines
            g2d.drawLine(OFFSET+(i+1)*dist,(int)HEIGHT_OFF,(int)p.getX(),(int)p.getY());
            p = ModelDrawing.getNextPointAtAngle(p.getX(), p.getY(), deltaDist, -elevationAngle);
        }
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
	
	public void drawTextOnImage() {
		int count = 0;
        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 16));
        g2d.setColor(java.awt.Color.RED);
        g2d.drawString("EL Ang     : "+Constance.EL_ANGLE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.setColor(java.awt.Color.YELLOW);
        g2d.drawString("AZ Tilt      : "+Constance.AZ_TILT, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;count++;
        
        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 12));
        g2d.setColor(java.awt.Color.CYAN);
        g2d.drawString("Glide Slope          : "+Constance.GLIDE_SLOPE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.drawString("Safety Slope        : "+Constance.SAFETY_SLOPE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.drawString("Safety Height      : "+Constance.SAFETY_HEIGHT, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;count++;
        g2d.setColor(java.awt.Color.CYAN);
        g2d.drawString("Distance          : "+Constance.DISTANCE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.drawString("Height            : "+Constance.HEIGHT, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        
        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 14));
        g2d.setColor(java.awt.Color.BLUE);
        count = 0;
        g2d.drawString("Channel   : "+Constance.CHANNEL, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.drawString("Control    : "+Constance.CONTROL, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.drawString("Route      : "+Constance.ROUTE, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.drawString("RWY        : "+Constance.RWY, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.drawString("Scale       : "+Constance.SCALE, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        
        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 14));
        g2d.setColor(java.awt.Color.GREEN);
        count = 0;
        g2d.drawString("System Perform     : ", 2*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.drawString("System Setting      : ", 2*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.drawString("System Logbook    : ", 2*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.setColor(java.awt.Color.YELLOW);
        count = 0;
        g2d.drawString(Constance.NULL, (int) (2.75*OFFSET*HGAP), TEXT_OFFSET+HGAP*count);
        count++;
        g2d.drawString(Constance.NULL, (int) (2.75*OFFSET*HGAP), TEXT_OFFSET+HGAP*count);
        count++;
        g2d.drawString(Constance.NULL, (int) (2.75*OFFSET*HGAP), TEXT_OFFSET+HGAP*count);
        count=0;		
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
	
	public void invalidateImage() {
		initBounds();
		drawBackgroundOnImage();
		drawElevationLineOnImage(elevationAngle);
		drawDistanceGridOnImage(dist, deltaDist);
		drawLandingStripOnImage(dist, distAngle);
		drawTextOnImage();
		System.out.println("Invalidate Image");
	}
	
	public Image getImage() {
		WritableImage wr = null;
		Image img = SwingFXUtils.toFXImage(bufferedImage, wr);  
		return img;
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
	}
	
}
