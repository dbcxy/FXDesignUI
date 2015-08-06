package model.drawable;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import model.MatrixRef;
import model.OverlayItem;
import model.graph.ILayoutParam;
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

public class Track extends OverlayItem implements ILayoutParam{

	private static final int TRACK_SIZE = 5;
	
	private String Title = "";
	private int trackNumber;
	private boolean isTextShown = true;
	
	private double elevation;
	private double azimuth;
	private double range;
	
	private boolean isEl = false;
	private boolean isAz = false;
	
//	https://en.wikipedia.org/wiki/Spherical_coordinate_system
//	http://in.mathworks.com/help/matlab/ref/cart2sph.html
//	x = r .* cos(elevation) .* cos(azimuth)
//	y = r .* cos(elevation) .* sin(azimuth)
//	z = r .* sin(elevation)
	
//	azimuth = atan2(y,x)
//	elevation = atan2(z,sqrt(x.^2 + y.^2))
//	r = sqrt(x.^2 + y.^2 + z.^2)
		
	public Track() {
		super(null,null,null);
	}

	public boolean isTextShown() {
		return isTextShown;
	}
	
	public void setTitle(String title) {
		this.Title = title;
	}
	
	public void setTrackNumber(int number) {
		trackNumber = number;
	}
	
	public String getTrackNumber() {
		return String.valueOf(trackNumber);
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
		this.azimuth = Math.toRadians(Constance.AZIMUTH_MAX) - azimuth;
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
		double x = getX();
		double y = getY();
		double z = getZ();
		
		//Range
		range = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2));
		setX(MatrixRef.getInstance().toRangePixels(range/1000));
		
		//Azimuth
		if(isAz) {
			azimuth = Math.toRadians(Constance.AZIMUTH_MAX) - Math.atan2(y,x);//Changing from 10->-10degrees to 0->20degrees
			MatrixRef matrixRef = MatrixRef.getInstance();
			double midAzimuth = (matrixRef.getMinAzimuth()+matrixRef.getMaxAzimuth())/2;
			double midAzimuthOffset = matrixRef.toRangePixels(Constance.AZIMUTH.RCLO/Constance.RANGE_DISP);
	        Point start = matrixRef.toAzimuthRangePixels(midAzimuth, matrixRef.getMinRange());
			Point p = ModelDrawing.getNextPointAtAngle(start.getX(), start.getY()+midAzimuthOffset, getX(), Math.toDegrees(-azimuth));
			setY(p.getY());
		}
		
		//Elevation
		if(isEl) {
			elevation = Math.atan2(z, Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)));
			MatrixRef matrixRef = MatrixRef.getInstance();
	        Point start = matrixRef.toElevationRangePixels(matrixRef.getMinElevation(), matrixRef.getMinRange());
			Point p = ModelDrawing.getNextPointAtAngle(start.getX(), start.getY(), getX(), Math.toDegrees(-elevation));
			setZ(p.getY());
		}
	}
	
	public void showText(boolean show) {		
		isTextShown = show;
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
			gc.setFill(Color.BISQUE);
	    	gc.fillOval(x-TRACK_SIZE, y-TRACK_SIZE, 2*TRACK_SIZE, 2*TRACK_SIZE);
	    	gc.setStroke(Color.WHITE);
	    	gc.setLineWidth(2);
	    	gc.strokeOval(x-TRACK_SIZE, y-TRACK_SIZE, 2*TRACK_SIZE, 2*TRACK_SIZE);
	    	gc.strokeLine(x,y+2*TRACK_SIZE,getX(),y-2*TRACK_SIZE);
	    	gc.strokeLine(x+2*TRACK_SIZE, y, x-2*TRACK_SIZE, y);
	    	
	    	if(isTextShown) {
	        	gc.setStroke(Color.CHOCOLATE);
	        	ModelDrawing.drawLineAtAngle(gc, x, y, 2*TRACK_SIZE, -45);
	        	Point p = ModelDrawing.getNextPointAtAngle(x, y, 2*TRACK_SIZE, -45);
	        	gc.strokeLine(p.getX(), p.getY(), p.getX()+6*TRACK_SIZE, p.getY());
	        	gc.setFont(new Font("Arial", 2*TRACK_SIZE));
	        	gc.setStroke(Color.WHITE);
	        	gc.strokeText(Title, p.getX()+TRACK_SIZE, p.getY()-TRACK_SIZE);
	        	gc.setStroke(Color.YELLOW);
	        	gc.strokeText(getTrackNumber(), p.getX()+2*TRACK_SIZE, p.getY()+2*TRACK_SIZE);
	    	}
		}
	}

	public void drawOnImage(BufferedImage bufferedImage) {

		double x = getX();
		double y = 0;
		if(isAz) {
			y = getY();
		} else if(isEl) {
			y = getZ();
		}

		if((range/1000) <= MatrixRef.getInstance().getVisibleRange()) {
			Graphics2D g2d = bufferedImage.createGraphics();
			g2d.setColor(java.awt.Color.RED);
			g2d.fillOval((int)x-TRACK_SIZE, (int)y-TRACK_SIZE, 2*TRACK_SIZE, 2*TRACK_SIZE);
			g2d.setColor(java.awt.Color.WHITE);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawOval((int)x-TRACK_SIZE, (int)y-TRACK_SIZE, 2*TRACK_SIZE, 2*TRACK_SIZE);
			g2d.drawLine((int)x,(int)y+2*TRACK_SIZE,(int)x,(int)y-2*TRACK_SIZE);
			g2d.drawLine((int)x+2*TRACK_SIZE, (int)y, (int)x-2*TRACK_SIZE, (int)y);
	    	
	    	if(isTextShown) {
	    		g2d.setColor(java.awt.Color.BLUE);
	        	ModelDrawing.drawLineAtAngle(g2d, x, y, 2*TRACK_SIZE, -45);
	        	Point p = ModelDrawing.getNextPointAtAngle(x, y, 2*TRACK_SIZE, -45);
	        	g2d.drawLine((int)p.getX(), (int)p.getY(), (int)p.getX()+6*TRACK_SIZE, (int)p.getY());
	        	g2d.setFont(new java.awt.Font("Arial",java.awt.Font.PLAIN, 2*TRACK_SIZE));
	        	g2d.setColor(java.awt.Color.WHITE);
	        	g2d.drawString(Title, (int)p.getX()+TRACK_SIZE, (int)p.getY()-TRACK_SIZE);
	        	g2d.setColor(java.awt.Color.YELLOW);
	        	g2d.drawString(getTrackNumber(), (int)p.getX()+TRACK_SIZE, (int)p.getY()+2*TRACK_SIZE);	
	    	}
		}
	}

}
