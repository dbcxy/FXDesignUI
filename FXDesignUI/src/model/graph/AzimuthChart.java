package model.graph;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import model.GraphChart;
import model.MatrixRef;
import model.graph.ElevationChart.myPoint;
import utils.Constance;
import utils.ModelDrawing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AzimuthChart extends GraphChart {
	
	static final Logger logger = Logger.getLogger(AzimuthChart.class);
	
	double midAzimuth;	
	MatrixRef matrixRef = MatrixRef.getInstance();
	Point startPoint;
	Point endPoint;
	
	class myPoint {
		int X;
		int leftY;
		int rightY;
	}	
	List<myPoint> crossPoints = new ArrayList<myPoint>();
	
	public AzimuthChart(Canvas canvas) {
		super(canvas);
		midAzimuth = (matrixRef.getMinAzimuth()+matrixRef.getMaxAzimuth())/2;
	}
	
	public void drawAzimuthLine() {
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);

		double midAzimuthOffset = matrixRef.toRangePixels(Constance.AZIMUTH.RCLO/Constance.RANGE_DISP);
        startPoint = matrixRef.toAzimuthRangePixels(midAzimuth, matrixRef.getMinRange());
        endPoint = matrixRef.toAzimuthRangePixels(midAzimuth, matrixRef.getVisibleRange()); 
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(),startPoint.getY()+midAzimuthOffset,endPoint.getX(), -Constance.AZIMUTH.LSL_ANGLE);//cross line at top az degrees
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(),startPoint.getY()+midAzimuthOffset,endPoint.getX(), -Constance.AZIMUTH.RSL_ANGLE);//cross line at bottom az degrees
        
        Point pointLeft = ModelDrawing.getNextPointAtAngle(startPoint.getX(), startPoint.getY()+midAzimuthOffset,endPoint.getX(), -Constance.AZIMUTH.LSL_ANGLE);//cross line at 20 degrees
        Point pointRight = ModelDrawing.getNextPointAtAngle(startPoint.getX(), startPoint.getY()+midAzimuthOffset,endPoint.getX(), -Constance.AZIMUTH.RSL_ANGLE);//flat line
        //calculation done for finding the X,Y point for range markers
        for(int i=(int) matrixRef.getTouchDown();i<matrixRef.getVisibleRange()+Constance.RANGE_DISP;i+=Constance.RANGE_DISP){
        	Point point = matrixRef.toAzimuthRangePixels(matrixRef.getMinElevation(), i);
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
        //finding Y (L & R) co-ordinates
        for(int i=0;i<matrixRef.getDrawableXArea();i++) {
        	pointLeft = ModelDrawing.getNextPointAtAngle(startPoint.getX(), startPoint.getY()+midAzimuthOffset,i, -Constance.AZIMUTH.LSL_ANGLE);//cross line at 20 degrees
        	pointRight = ModelDrawing.getNextPointAtAngle(startPoint.getX(), startPoint.getY()+midAzimuthOffset,i, -Constance.AZIMUTH.RSL_ANGLE);//cross line at 20 degrees
        	int raX = (int) pointLeft.getX();
        	for(int j=0;j<crossPoints.size();j++) {
        		if(raX == crossPoints.get(j).X){
        			crossPoints.get(j).leftY =(int) pointLeft.getY();
        			crossPoints.get(j).rightY =(int) pointRight.getY();
        			break;
        		}
        	}
        }
	}

	public void drawLandingStrip() {
		double centerDist = Constance.ELEVATION.GLIDE_MAX_DIST-Constance.RANGE_DISP;
		
        startPoint = matrixRef.toAzimuthRangePixels(midAzimuth, matrixRef.getTouchDown());
        endPoint = matrixRef.toAzimuthRangePixels(midAzimuth, centerDist-Constance.RANGE_DISP);
        
        //dotted line
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
        gc.setLineDashes(OFFSET/2);
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(Constance.AZIMUTH.RAL_ANGLE));//dotted below line
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(Constance.AZIMUTH.LAL_ANGLE));//dotted above line
        gc.setLineDashes(0);
        
        //solid line
        endPoint = matrixRef.toAzimuthRangePixels(midAzimuth, centerDist);
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
		double offsetLeftAzimuth = matrixRef.toRangePixels(Constance.AZIMUTH.LSaL/Constance.RANGE_DISP);
        Point offsetLeft = new Point(offsetLeftAzimuth,offsetLeftAzimuth,offsetLeftAzimuth, null);
        gc.strokeLine(startPoint.getX(), startPoint.getY()-offsetLeft.getY(), endPoint.getX(),endPoint.getY()-offsetLeft.getY());//above center line
		double offsetRightAzimuth = matrixRef.toRangePixels(Constance.AZIMUTH.RSaL/Constance.RANGE_DISP);
        Point offsetRight = new Point(offsetRightAzimuth,offsetRightAzimuth,offsetRightAzimuth, null);
        gc.strokeLine(startPoint.getX(), startPoint.getY()+offsetRight.getY(), endPoint.getX(),endPoint.getY()+offsetRight.getY());//below center line
        
        endPoint = matrixRef.toAzimuthRangePixels(midAzimuth, centerDist+Constance.RANGE_DISP);
        gc.setStroke(Color.RED);
        gc.strokeLine(startPoint.getX(), startPoint.getY(), endPoint.getX(),endPoint.getY());//center line

	}
	
	public void drawRedDistanceLine() {
		double range = matrixRef.toRangePixels(Constance.ELEVATION.DH/Constance.RANGE_DISP);
		
		startPoint = matrixRef.toAzimuthRangePixels(midAzimuth, matrixRef.getTouchDown());
		myPoint ePoint = crossPoints.get(1);
        
		//dotted red line
		gc.setStroke(Color.RED);
		gc.setLineWidth(1.5); 
		gc.setLineDashes(OFFSET/2);
		
		gc.strokeLine(startPoint.getX()+range,startPoint.getY(),ePoint.X+range,ePoint.leftY);
		gc.strokeLine(startPoint.getX()+range,startPoint.getY(),ePoint.X+range,ePoint.rightY);
		
		gc.setLineDashes(0);
	}

	public void drawDistanceGrid() {
		
		startPoint = matrixRef.toAzimuthRangePixels(midAzimuth, matrixRef.getTouchDown());
		gc.setLineWidth(1);

		//remaining Lines		
        for(int i=(int) matrixRef.getTouchDown();i<matrixRef.getVisibleRange()+Constance.RANGE_DISP;i+=Constance.RANGE_DISP){
        	
        	startPoint = matrixRef.toAzimuthRangePixels(midAzimuth, i);
        	myPoint ePoint = crossPoints.get(i);
        	if(ePoint.leftY==ePoint.rightY)
        		ePoint.rightY=(int) matrixRef.getActualYdimen();
    		Point endTop = new Point();
        	endTop.setX(ePoint.X);endTop.setY(ePoint.leftY);
        	Point endBttm = new Point();
        	endBttm.setX(ePoint.X);endBttm.setY(ePoint.rightY);
            
        	if((i%5)==0) {
        		gc.setStroke(Color.YELLOW);
        		gc.strokeLine(startPoint.getX(),startPoint.getY(),endTop.getX(),endTop.getY());
        		gc.strokeLine(startPoint.getX(),startPoint.getY(),endBttm.getX(),endBttm.getY());
        	} else if (i==1) {
                //draw TD
        		gc.setStroke(Color.YELLOW);
        		gc.strokeLine(startPoint.getX(),startPoint.getY(),endTop.getX(),endTop.getY());
        		gc.strokeLine(startPoint.getX(),startPoint.getY(),endBttm.getX(),endBttm.getY());
//        		gc.strokeText(" TD ", startPoint.getX()-OFFSET, startPoint.getY()+HGAP);
        	} else {
        		gc.setStroke(Color.GREEN);
        		gc.strokeLine(startPoint.getX(),startPoint.getY(),endTop.getX(),endTop.getY());
        		gc.strokeLine(startPoint.getX(),startPoint.getY(),endBttm.getX(),endBttm.getY());
        	}
        }
	}

	public static void drawText(Canvas canvas) {
		int count = 0;
		GraphicsContext gc = canvas.getGraphicsContext2D();
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
        gc.strokeText("Offset                   : "+Constance.AZIMUTH.RCLO+Constance.UNITS.getLENGTH(), OFFSET, TEXT_OFFSET+HGAP*count);
        count = 0;
        
        gc.setFont(new Font("Sans Serif", 18));
        gc.setStroke(Color.YELLOW);
        gc.strokeText("-", OFFSET, HEIGHT_OFF/2+HGAP+TEXT_OFFSET);
        gc.strokeText("+", OFFSET, HEIGHT_OFF/2-OFFSET);
	}

}
