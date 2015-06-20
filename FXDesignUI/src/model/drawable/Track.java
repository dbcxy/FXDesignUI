package model.drawable;

import model.OverlayItem;
import model.drawing.ILayoutParam;
import utils.ModelDrawing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Track extends OverlayItem implements ILayoutParam{

	private String trackNumber;
	
	private Canvas mCanvas;
	private GraphicsContext gc;
	private boolean isTextShown = true;
	
	public Track(Canvas canvas) {
		this(canvas,null,null);
	}
	
	public Track(Canvas canvas, final Double x, final Double y) {
		super(x, y);
		this.mCanvas = canvas;
		this.gc = mCanvas.getGraphicsContext2D();
	}

	public boolean isTextShown() {
		return isTextShown;
	}
	
	public void setText(String name, String number) {
		super.setTitle(name);
		trackNumber = number;
	}
	
	public void setXY(double x, double y) {
		super.setX(x);
		super.setY(y);
	}

	public void drawTrack() {
    	gc.setFill(Color.BISQUE);
    	gc.fillOval(getX()-OFFSET, getY()-OFFSET, HGAP, HGAP);
    	gc.setStroke(Color.WHITE);
    	gc.setLineWidth(2);
    	gc.strokeOval(getX()-OFFSET, getY()-OFFSET, HGAP, HGAP);
    	gc.strokeLine(getX(),getY()+HGAP,getX(),getY()-HGAP);
    	gc.strokeLine(getX()+HGAP, getY(), getX()-HGAP, getY());
    	
    	if(isTextShown)
			displayText();
	}
	
	public void showText(boolean show) {		
		isTextShown = show;
	}
	
	private void displayText() {
    	gc.setStroke(Color.CHOCOLATE);
    	ModelDrawing.drawLineAtAngle(gc, getX(), getY(), HGAP, -45);
    	Point p = ModelDrawing.getPointOfLineAtAngle(gc, getX(), getY(), HGAP, -45);
    	gc.strokeLine(p.getX(), p.getY(), p.getX()+2*TEXT_OFFSET, p.getY());
    	gc.setFont(new Font("Arial", 14));
    	gc.setStroke(Color.WHITE);
    	gc.strokeText(getTitle(), p.getX()+OFFSET, p.getY()-OFFSET);
    	gc.setStroke(Color.YELLOW);
    	gc.strokeText(trackNumber, p.getX()+OFFSET, p.getY()+HGAP);
	}

}