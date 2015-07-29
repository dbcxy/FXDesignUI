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
	private double azimuth;
	private double range;
	
	private boolean isVisibleX = false;
	private boolean isVisibleY = false;
	
	public Plot() {
		super(null,null);
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
		this.elevation = elevation;
	}

	public double getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(double azimuth) {
		this.azimuth = azimuth;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}
	
	@Override
	public double getX() {
		double x = 0.0;
		if((range/1000) <= MatrixRef.getInstance().getVisibleRange()) {
			isVisibleX = true;
			x = MatrixRef.getInstance().toRangePixels(range/1000);
		}
		return x;
	}

	@Override
	public double getY() {
		double y = 0.0;
		if(elevation > 0) {
			isVisibleY = true;
			y = MatrixRef.getInstance().toElevationPixels(elevation);
		}
		if(azimuth > 0) {
			isVisibleY = true;
			y = MatrixRef.getInstance().toAzimuthPixels(azimuth);
		}
		return y;
	}

	@Override
	public void draw(GraphicsContext gc) {
		
		if(isVisibleX && isVisibleY) {
			gc.setFill(Color.RED);
	    	gc.fillOval(getX()-OFFSET, getY()-OFFSET, OFFSET, OFFSET);
	    	gc.setStroke(Color.WHITE);
	    	gc.setLineWidth(1);
	    	gc.strokeOval(getX()-OFFSET, getY()-OFFSET, OFFSET, OFFSET);
	    	
	    	if(isTextShown)
				displayText(gc);
		}
	}
		
	private void displayText(GraphicsContext gc) {
    	gc.setStroke(Color.CHOCOLATE);
    	ModelDrawing.drawLineAtAngle(gc, getX(), getY(), HGAP, -45);
    	Point p = ModelDrawing.getNextPointAtAngle(getX(), getY(), HGAP, -45);
    	gc.strokeLine(p.getX(), p.getY(), p.getX()+2*TEXT_OFFSET, p.getY());
    	gc.setFont(new Font("Arial", 14));
    	gc.setStroke(Color.WHITE);
    	gc.strokeText(getTitle(), p.getX()+OFFSET, p.getY()-OFFSET);
    	gc.setStroke(Color.YELLOW);
    	gc.strokeText(plotNumber, p.getX()+OFFSET, p.getY()+HGAP);
	}
}
