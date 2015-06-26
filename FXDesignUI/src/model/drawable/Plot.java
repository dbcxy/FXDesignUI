package model.drawable;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.OverlayItem;
import model.graph.ILayoutParam;
import utils.ModelDrawing;

public class Plot extends OverlayItem implements ILayoutParam{
	
	private String plotNumber;
	private boolean isTextShown = false;
	
	public Plot() {
		this(null,null);
	}
	
	public Plot(final Double x, final Double y) {
		super(x, y);
	}
	
	public boolean isTextShown() {
		return isTextShown;
	}
	
	public void setText(String name, String number) {
		super.setTitle(name);
		plotNumber = number;
	}
	
	public void setXY(double x, double y) {
		super.setX(x);
		super.setY(y);
	}
	
	public void showText(boolean show) {		
		isTextShown = show;
	}
	
	private void displayText(GraphicsContext gc) {
    	gc.setStroke(Color.CHOCOLATE);
    	ModelDrawing.drawLineAtAngle(gc, getX(), getY(), HGAP, -45);
    	Point p = ModelDrawing.getPointOfLineAtAngle(gc, getX(), getY(), HGAP, -45);
    	gc.strokeLine(p.getX(), p.getY(), p.getX()+2*TEXT_OFFSET, p.getY());
    	gc.setFont(new Font("Arial", 14));
    	gc.setStroke(Color.WHITE);
    	gc.strokeText(getTitle(), p.getX()+OFFSET, p.getY()-OFFSET);
    	gc.setStroke(Color.YELLOW);
    	gc.strokeText(plotNumber, p.getX()+OFFSET, p.getY()+HGAP);
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.RED);
    	gc.fillOval(getX()-OFFSET, getY()-OFFSET, OFFSET, OFFSET);
    	gc.setStroke(Color.WHITE);
    	gc.setLineWidth(1);
    	gc.strokeOval(getX()-OFFSET, getY()-OFFSET, OFFSET, OFFSET);
    	
    	if(isTextShown)
			displayText(gc);
		
	}
}
