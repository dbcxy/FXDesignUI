package model.drawable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.MatrixRef;
import model.OverlayItem;
import model.graph.ILayoutParam;
import utils.Constance;
import utils.ModelDrawing;

public class Plot extends OverlayItem implements ILayoutParam{
	
	private String plotNumber;
	private boolean isTextShown = false;
	
	private double elevation;
	private double azimuth;//radians
	private double range;//KM
	
	private boolean isAz = false;
	private boolean isEl = false;
		
	public Plot() {
		super(null,null,null);
	}

	public boolean isTextShown() {
		return isTextShown;
	}
	
	public void setText(String name, String number) {
		super.setTitle(name);
		plotNumber = number;
	}
	
	public void showText(boolean show) {		
		isTextShown = show;
	}
	
	public double getElevation() {
		return elevation;
	}

	public void setElevation(double elevation) {
		isEl = true;
		this.elevation = elevation;
	}

	public double getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(double azimuth) {
		isAz = true;
		this.azimuth = Math.toRadians(Constance.AZIMUTH_MAX) - azimuth;//Changing from 10->-10degrees to 0->20degrees
//		this.azimuth = azimuth;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}
	
	public void setAz(boolean b) {
		isAz = b;
	}
	
	public void setEl(boolean b) {
		isEl = b;
	}
	
	public void extractGraphAER() {
		
		//Range
		setX(MatrixRef.getInstance().toRangePixels(range/1000));
		
		//Azimuth
		if(isAz) {
			MatrixRef matrixRef = MatrixRef.getInstance();
			double midAzimuth = (matrixRef.getMinAzimuth()+matrixRef.getMaxAzimuth())/2;
			double midAzimuthOffset = matrixRef.toRangePixels(Constance.AZIMUTH.RCLO/Constance.RANGE_DISP);
	        Point start = matrixRef.toAzimuthRangePixels(midAzimuth, matrixRef.getMinRange());
			Point p = ModelDrawing.getNextPointAtAngle(start.getX(), start.getY()+midAzimuthOffset, getX(), Math.toDegrees(-azimuth));
			setY(p.getY());
		}
		
		//Elevation
		if(isEl) {
			MatrixRef matrixRef = MatrixRef.getInstance();
	        Point start = matrixRef.toElevationRangePixels(matrixRef.getMinElevation(), matrixRef.getMinRange());
			Point p = ModelDrawing.getNextPointAtAngle(start.getX(), start.getY(), getX(), Math.toDegrees(-elevation));
			setZ(p.getY());
		}
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		double x = getX();
		double y = 0;
		if(isAz) {
			y = getY();
		} else if(isEl) {
			y = getZ();
		}
		
		if((range/1000) <= MatrixRef.getInstance().getVisibleRange()) {
			gc.setFill(Color.RED);
	    	gc.fillOval(x-OFFSET, y-OFFSET, OFFSET, OFFSET);
	    	gc.setStroke(Color.WHITE);
	    	gc.setLineWidth(1);
	    	gc.strokeOval(x-OFFSET, y-OFFSET, OFFSET, OFFSET);
	    	
	    	if(isTextShown) {
	    		gc.setStroke(Color.CHOCOLATE);
	        	ModelDrawing.drawLineAtAngle(gc, x, y, HGAP, -45);
	        	Point p = ModelDrawing.getNextPointAtAngle(x, y, HGAP, -45);
	        	gc.strokeLine(p.getX(), p.getY(), p.getX()+2*TEXT_OFFSET, p.getY());
	        	gc.setFont(new Font("Arial", 14));
	        	gc.setStroke(Color.WHITE);
	        	gc.strokeText(getTitle(), p.getX()+OFFSET, p.getY()-OFFSET);
	        	gc.setStroke(Color.YELLOW);
	        	gc.strokeText(plotNumber, p.getX()+OFFSET, p.getY()+HGAP);
	    	}
		}
	}

}
