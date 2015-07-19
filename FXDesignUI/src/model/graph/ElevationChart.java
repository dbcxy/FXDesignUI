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

public class ElevationChart extends GraphChart{
	
	double elevationAngle;
	double distAngle;
	double centerDist;
	
	MatrixRef matrixRef = MatrixRef.getInstance();
	Point startPoint;
	Point endPoint;
	
	public ElevationChart(Canvas canvas) {
		super(canvas);
	}
	
	public void drawElevationLine(double elAngle) {
		this.elevationAngle = elAngle;
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);        
        startPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), matrixRef.getMinRange());
        endPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), matrixRef.getMaxRange());        		
        gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());//flat line     
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(),matrixRef.getDrawableXArea(), -elevationAngle);//cross line at 20 degrees
	}
	
	public void drawRedDistanceLine(double offsetInRange) {
		double range = matrixRef.toRangePixels(offsetInRange);
		
		startPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), matrixRef.getTouchDown());
        endPoint = matrixRef.toElevationPixels(Constance.ELEVATION_DISP, matrixRef.getTouchDown());
        
		//dotted red line
		gc.setStroke(Color.RED);
		gc.setLineWidth(1.5); 
//		gc.setLineDashes(OFFSET/2);
		gc.strokeLine(startPoint.getX()+range,startPoint.getY(),endPoint.getX()+range,endPoint.getY());
//		gc.setLineDashes(0);
	}
	
	public void drawDistanceGrid() {
		
        gc.setFont(new Font("Sans Serif", 16));
        gc.setLineWidth(1);

		//draw remaining lines
        for(int i=(int) matrixRef.getTouchDown();i<matrixRef.getMaxRange()+Constance.RANGE_DISP;i+=Constance.RANGE_DISP){
        	
        	startPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), i);
            endPoint = matrixRef.toElevationPixels((i)*Constance.ELEVATION_DISP, i);
//            endPoint = matrixRef.toElevationPixels(ModelDrawing.getElevationHeigth(distAngle, i), i);
            
            //completing top angle line by joining
//        	Point prevPoint = matrixRef.toElevationPixels((i-1)*Constance.ELEVATION_DISP, (i-1));
//            gc.setStroke(Color.CYAN);
//            gc.strokeLine(prevPoint.getX(),prevPoint.getY(),endPoint.getX(),endPoint.getY());
            

            if((i%5)==0) {
        		//write text Range
        		gc.setStroke(Color.YELLOW);
        		gc.strokeText(i+Constance.UNITS.LENGTH, startPoint.getX()-OFFSET, startPoint.getY()+HGAP);
        		gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
        	} else if (i==1) {
                //write text TD
        		gc.setStroke(Color.YELLOW);
        		gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
        		gc.strokeText(" TD ", startPoint.getX()-OFFSET, startPoint.getY()+HGAP);
        	} else {
            	gc.setStroke(Color.GREEN);
            	gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
        	}
        }
	}
	
	public void drawLandingStrip(double centerDist, double dAngle) {
		this.distAngle = dAngle;
		this.centerDist = centerDist;
        gc.setStroke(Color.AQUAMARINE);
        
        startPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), matrixRef.getTouchDown());
        endPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), centerDist-Constance.RANGE_DISP);
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(distAngle-1.5));//below center line
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(distAngle+1.5));//above center line
        
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
//        gc.setLineDashes(OFFSET/2);
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(distAngle-3.5));//imaginary below line
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(distAngle+3.5));//imaginary above line
//        gc.setLineDashes(0);
        
        endPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), centerDist);
        gc.setStroke(Color.RED);
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -distAngle);//center red line
	}
		
	public static void drawText(Canvas canvas) {
		int count = 0;
		GraphicsContext gc = canvas.getGraphicsContext2D();
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
        gc.strokeText("RWY        : "+Constance.PREF.SEL_RUNWAY, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
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
	
}
