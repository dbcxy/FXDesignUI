package model.graph;

import model.GraphChart;
import model.MatrixRef;
import utils.Constance;
import utils.ModelDrawing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AzimuthChart extends GraphChart {
	
	double azimuthAngle;
	double dist;
	double midAzimuth;
	
	MatrixRef matrixRef = MatrixRef.getInstance();
	Point startPoint;
	Point endPoint;
	
	public AzimuthChart(Canvas canvas) {
		super(canvas);
		midAzimuth = (matrixRef.getMinAzimuth()+matrixRef.getMaxAzimuth())/2;
		
	}
	
	public void drawAzimuthLine(double azAngle) {
		this.azimuthAngle = azAngle;
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);
        startPoint = matrixRef.toAzimuthPixels(midAzimuth, matrixRef.getMinRange());
        endPoint = matrixRef.toAzimuthPixels(midAzimuth, matrixRef.getMaxRange()); 
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(),startPoint.getY(),endPoint.getX(), -azimuthAngle);//cross line at top az degrees
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(),startPoint.getY(),endPoint.getX(), azimuthAngle);//cross line at bottom az degrees
	}

	public void drawLandingStrip(double centerDist, double offsetInAzimuth) {
		this.dist = centerDist;
        Point offset = new Point(offsetInAzimuth,offsetInAzimuth,offsetInAzimuth, null);
		
        startPoint = matrixRef.toAzimuthPixels(midAzimuth, matrixRef.getTouchDown());
        endPoint = matrixRef.toAzimuthPixels(midAzimuth, dist-Constance.RANGE_DISP);
        
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
//        gc.setLineDashes(OFFSET/2);
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), (azimuthAngle-0.5)/2);//imaginary below line
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(azimuthAngle-0.5)/2);//imaginary above line
//        gc.setLineDashes(0);
        
        endPoint = matrixRef.toAzimuthPixels(midAzimuth, dist);
        gc.setStroke(Color.AQUAMARINE);
        gc.strokeLine(startPoint.getX(), startPoint.getY()-offset.getY(), endPoint.getX(),endPoint.getY()-offset.getY());//above center line
        gc.strokeLine(startPoint.getX(), startPoint.getY()+offset.getY(), endPoint.getX(),endPoint.getY()+offset.getY());//below center line
        
        endPoint = matrixRef.toAzimuthPixels(midAzimuth, dist+Constance.RANGE_DISP);
        gc.setStroke(Color.RED);
        gc.strokeLine(startPoint.getX(), startPoint.getY(), endPoint.getX(),endPoint.getY());//center line

	}
	
	public void drawRedDistanceLine(double offsetInRange) {
		double range = matrixRef.toRangePixels(offsetInRange);
		
		startPoint = matrixRef.toAzimuthPixels(midAzimuth, matrixRef.getTouchDown());
        
		//dotted red line
		gc.setStroke(Color.RED);
		gc.setLineWidth(1.5); 
//		gc.setLineDashes(OFFSET/2);

        endPoint = matrixRef.toAzimuthPixels(Constance.AZIMUTH_DISP, matrixRef.getTouchDown());
		gc.strokeLine(startPoint.getX()+range,startPoint.getY(),endPoint.getX()+range,endPoint.getY());
		
        endPoint = matrixRef.toAzimuthPixels(-Constance.AZIMUTH_DISP, matrixRef.getTouchDown());
		gc.strokeLine(startPoint.getX()+range,startPoint.getY(),endPoint.getX()+range,endPoint.getY());
//		gc.setLineDashes(0);
	}

	public void drawDistanceGrid() {
		Point endTop = null;
		Point endBttm = null;
		startPoint = matrixRef.toAzimuthPixels(midAzimuth, matrixRef.getTouchDown());
		
		//TD Line
		gc.setLineWidth(1);

		//remaining Lines		
        for(int i=(int) matrixRef.getTouchDown();i<matrixRef.getMaxRange()+Constance.RANGE_DISP;i+=Constance.RANGE_DISP){
        	
        	startPoint = matrixRef.toAzimuthPixels(midAzimuth, i);
        	if((i*Constance.AZIMUTH_DISP) < matrixRef.getMaxAzimuth()) {
        		endTop = matrixRef.toAzimuthPixels((i)*Constance.AZIMUTH_DISP, i);
        		endBttm = matrixRef.toAzimuthPixels((-i)*Constance.AZIMUTH_DISP, i);
        	} else {
        		endTop.setX(startPoint.getX());
        		endTop.setY(matrixRef.toAzimuthPixels(matrixRef.getMaxAzimuth()));
        		
        		endBttm.setX(startPoint.getX());
        		endBttm.setY(matrixRef.toAzimuthPixels(matrixRef.getMinAzimuth()));
        	}
            
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
        gc.strokeText("Offset                   : "+Constance.OFFSET, OFFSET, TEXT_OFFSET+HGAP*count);
        count = 0;
        
        gc.setFont(new Font("Sans Serif", 18));
        gc.setStroke(Color.YELLOW);
        gc.strokeText("-", OFFSET, HEIGHT_OFF/2+HGAP+OFFSET);
        gc.strokeText("+", OFFSET, HEIGHT_OFF/2-HGAP);
	}

}
