package model.graph;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import model.GraphChart;
import model.MatrixRef;
import utils.Constance;
import utils.ModelDrawing;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ElevationChart extends GraphChart{
	
	static final Logger logger = Logger.getLogger(ElevationChart.class);
	
	MatrixRef matrixRef = MatrixRef.getInstance();
	Point startPoint;
	Point endPoint;
	
	class myPoint {
		int X;
		int Y;
	}	
	List<myPoint> crossPoints = new ArrayList<myPoint>();
	
	
	public ElevationChart(Canvas canvas) {
		super(canvas);
	}
	
	public void drawElevationLine() {
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);        
        startPoint = matrixRef.toElevationRangePixels(matrixRef.getMinElevation(), matrixRef.getMinRange());
        Point pointCross = ModelDrawing.getNextPointAtAngle(startPoint.getX(), startPoint.getY(),matrixRef.getDrawableXArea(), -Constance.ELEVATION.USL_ANGLE);//cross line at 20 degrees
        Point pointFlat = ModelDrawing.getNextPointAtAngle(startPoint.getX(), startPoint.getY(),matrixRef.getDrawableXArea(), -Constance.ELEVATION.LSL_ANGLE);//flat line
        gc.strokePolyline(new double[]{pointCross.getX(), startPoint.getX(), pointFlat.getX()},
                new double[]{pointCross.getY(), startPoint.getY(), pointFlat.getY()}, 3);
        
        //calculation done for finding the X,Y point for range markers
        for(int i=(int) matrixRef.getTouchDown();i<matrixRef.getVisibleRange()+Constance.RANGE_DISP;i+=Constance.RANGE_DISP){
        	Point point = matrixRef.toElevationRangePixels(matrixRef.getMinElevation(), i);
        	if(i==matrixRef.getTouchDown()){
        		double range = matrixRef.toRangePixels(Constance.ELEVATION.DH/Constance.RANGE_DISP);//range offset from TD (for red line)
        		myPoint p = new myPoint();
            	p.X = (int) (point.getX()+range);
            	crossPoints.add(p);
        	}
        	myPoint p = new myPoint();
        	p.X = (int) point.getX();
        	crossPoints.add(p);
        }
        //finding Y co-ordinates
        for(int i=0;i<matrixRef.getDrawableXArea();i++) {
        	pointCross = ModelDrawing.getNextPointAtAngle(startPoint.getX(), startPoint.getY(),i, -Constance.ELEVATION.USL_ANGLE);//cross line at 20 degrees
        	int raX = (int) pointCross.getX();
        	for(int j=0;j<crossPoints.size();j++) {
        		if(raX == crossPoints.get(j).X){
        			crossPoints.get(j).Y =(int) pointCross.getY();
        			break;
        		}
        	}
        }

	}
	
	public void drawRedDistanceLine() {
		double range = matrixRef.toRangePixels(Constance.ELEVATION.DH/Constance.RANGE_DISP);//range offset from TD
		
		startPoint = matrixRef.toElevationRangePixels(matrixRef.getMinElevation(), matrixRef.getTouchDown());
		endPoint.setX(crossPoints.get(1).X);
    	endPoint.setY(crossPoints.get(1).Y);
        
		//dotted red line
		gc.setStroke(Color.RED);
		gc.setLineWidth(1.5); 
		gc.setLineDashes(OFFSET/2);
		gc.strokeLine(startPoint.getX()+range,startPoint.getY(),endPoint.getX()+range,endPoint.getY());
		gc.setLineDashes(0);
	}
	
	public void drawDistanceGrid() {
		
        //draw remaining lines
        for(int i=(int) matrixRef.getTouchDown();i<matrixRef.getVisibleRange()+Constance.RANGE_DISP;i+=Constance.RANGE_DISP){
        	
        	startPoint = matrixRef.toElevationRangePixels(matrixRef.getMinElevation(), i);        	
        	endPoint.setX(crossPoints.get(i).X);
        	endPoint.setY(crossPoints.get(i).Y);
            
            if((i%5)==0) {
        		//write text Range
        		gc.setStroke(Color.YELLOW);
        		gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
        	} else if (i==1) {
                //write text TD
        		gc.setStroke(Color.YELLOW);
        		gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
        	} else {
            	gc.setStroke(Color.GREEN);
            	gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
        	}
        }
	}
	
	public void drawLandingStrip() {
		double centerDist = Constance.ELEVATION.GLIDE_FLAT_START_DIST-Constance.RANGE_DISP;//till 9NM drawing shows till 10NM
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
        
        startPoint = matrixRef.toElevationRangePixels(matrixRef.getMinElevation(), matrixRef.getTouchDown());
        endPoint = matrixRef.toElevationRangePixels(matrixRef.getMinElevation(), centerDist-Constance.RANGE_DISP);
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(Constance.ELEVATION.LAL_ANGLE));//below center line
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(Constance.ELEVATION.UAL_ANGLE));//above center line
        
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
        gc.setLineDashes(OFFSET/2);
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(Constance.ELEVATION.LSaL_ANGLE));//dotted below line
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(Constance.ELEVATION.USaL_ANGLE));//dotted above line
        gc.setLineDashes(0);
        
        endPoint = matrixRef.toElevationRangePixels(matrixRef.getMinElevation(), centerDist);
        gc.setStroke(Color.RED);
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -Constance.ELEVATION.GLIDE_ANGLE);//center red line

        Point point = ModelDrawing.getNextPointAtAngle(startPoint.getX(), startPoint.getY(), endPoint.getX(), -Constance.ELEVATION.GLIDE_ANGLE);
        gc.strokeLine(point.getX(),point.getY(),matrixRef.toRangePixels(Constance.ELEVATION.GLIDE_MAX_DIST),point.getY());
	}
	
	public void drawUnitsText() {

		gc.setStroke(Color.YELLOW);
		gc.setFont(new Font("Sans Serif", 14));
        gc.setLineWidth(1);
		
        //draw remaining lines
        for(int i=(int) matrixRef.getTouchDown();i<matrixRef.getVisibleRange()+Constance.RANGE_DISP;i+=Constance.RANGE_DISP){
        	
        	startPoint = matrixRef.toElevationRangePixels(matrixRef.getMinElevation(), i);        	
        	endPoint.setX(crossPoints.get(i).X);
        	endPoint.setY(crossPoints.get(i).Y);
            
            if((i%5)==0) {
        		//write text Range
        		if(Constance.PREF.SEL_RUNWAY.contains("3") || Constance.PREF.SEL_RUNWAY.contains("4"))
	        		gc.drawImage(printMirrorImage(i+Constance.UNITS.getLENGTH()), startPoint.getX()-HGAP, startPoint.getY()+OFFSET);
        		else
            		gc.strokeText(i+Constance.UNITS.getLENGTH(), startPoint.getX()-OFFSET, startPoint.getY()+HGAP);
        	} else if (i==1) {
                //write text TD
        		if(Constance.PREF.SEL_RUNWAY.contains("3") || Constance.PREF.SEL_RUNWAY.contains("4"))
	        		gc.drawImage(printMirrorImage(" TD "), startPoint.getX()-HGAP, startPoint.getY()+OFFSET);
        		else
            		gc.strokeText(" TD ", startPoint.getX()-OFFSET, startPoint.getY()+HGAP);
        	}
        }
	}
		
	public void drawText(Canvas canvas, int x, int y) {
		int count = 0;
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.translate(x, y);
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
                
        gc.setFont(new Font("Sans Serif", 14));
        gc.setStroke(Color.CADETBLUE);
        count = 0;
        gc.strokeText("Channel   : "+Constance.CHANNEL, 0.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Control    : "+Constance.CONTROL, 0.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Route      : "+Constance.ROUTE, 0.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("RWY        : "+Constance.PREF.SEL_RUNWAY, 0.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Scale       : "+Constance.getSCALE(), 0.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.setFont(new Font("Sans Serif", 12));
        gc.setStroke(Color.AQUA);
        gc.strokeText("Distance          : "+Constance.DISTANCE, 0.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Height            : "+Constance.HEIGHT, 0.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        
        gc.setFont(new Font("Sans Serif", 14));
        gc.setStroke(Color.GREENYELLOW);
        count = 0;
        gc.strokeText("System Perform     : "+Constance.NULL, 1.3*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("System Setting      : "+Constance.NULL, 1.3*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("System Logbook    : "+Constance.NULL, 1.3*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count=0;
	}
	
	private Image printMirrorImage(String str) {
    	BufferedImage bufferedImage = new BufferedImage(50,50, BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g2d = bufferedImage.createGraphics();
    	g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.BOLD, 14));
    	g2d.setColor(java.awt.Color.YELLOW);
    	g2d.drawString(str,10,10);
    	AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
    	tx.translate(-bufferedImage.getWidth(null), 0);
    	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    	bufferedImage = op.filter(bufferedImage, null);
    	WritableImage wr = null;
		Image img = SwingFXUtils.toFXImage(bufferedImage, wr);
		return img;
	}
	
}
