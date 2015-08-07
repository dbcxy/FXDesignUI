package model.drawable;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

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
	
	private static final int PLOT_SIZE = 3;
	
	private String Title = "";
	private int plotNumber;
	
	private double elevation;
	private double azimuth;//radians
	private double range;//KM
	
	private boolean isAz = false;
	private boolean isEl = false;
		
	public Plot() {
		super(null,null,null);
	}

	public void setTitle(String title) {
		this.Title = title;
	}
	
	public void setPlotNumber(int number) {
		plotNumber = number;
	}
	
	public String getPlotNumber() {
		return String.valueOf(plotNumber);
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
		
		if((range/1000) <= MatrixRef.getInstance().getVisibleRange() && Constance.SHOW_PLOT) {
			gc.setFill(Color.RED);
	    	gc.fillOval(x-PLOT_SIZE, y-PLOT_SIZE, PLOT_SIZE, PLOT_SIZE);
	    	gc.setStroke(Color.WHITE);
	    	gc.setLineWidth(0.5);
	    	gc.strokeOval(x-PLOT_SIZE, y-PLOT_SIZE, PLOT_SIZE, PLOT_SIZE);
	    	
	    	if(Constance.SHOW_PLOT_LABEL) {
	    		gc.setStroke(Color.CHOCOLATE);
	        	ModelDrawing.drawLineAtAngle(gc, x, y, 2*PLOT_SIZE, -45);
	        	Point p = ModelDrawing.getNextPointAtAngle(x, y, 2*PLOT_SIZE, -45);
	        	gc.strokeLine(p.getX(), p.getY(), p.getX()+6*PLOT_SIZE, p.getY());
	        	gc.setFont(new Font("Arial", 2*PLOT_SIZE));
	        	gc.setStroke(Color.WHITE);
	        	gc.strokeText(Title, p.getX()+PLOT_SIZE, p.getY()-PLOT_SIZE);
	        	gc.setStroke(Color.YELLOW);
	        	gc.strokeText(getPlotNumber(), p.getX()+PLOT_SIZE, p.getY()+2*PLOT_SIZE);
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

		if((range/1000) <= MatrixRef.getInstance().getVisibleRange() && Constance.SHOW_PLOT) {
			Graphics2D g2d = bufferedImage.createGraphics();
			g2d.setColor(java.awt.Color.RED);
			g2d.fillOval((int)x-PLOT_SIZE, (int)y-PLOT_SIZE, PLOT_SIZE, PLOT_SIZE);
			g2d.setColor(java.awt.Color.WHITE);
			g2d.setStroke(new BasicStroke((float) 0.5));
			g2d.drawOval((int)x-PLOT_SIZE, (int)y-PLOT_SIZE, PLOT_SIZE, PLOT_SIZE);
	    	
	    	if(Constance.SHOW_PLOT_LABEL) {
	    		g2d.setColor(java.awt.Color.BLUE);
	        	ModelDrawing.drawLineAtAngle(g2d, x, y, 2*PLOT_SIZE, -45);
	        	Point p = ModelDrawing.getNextPointAtAngle(x, y, 2*PLOT_SIZE, -45);
	        	g2d.drawLine((int)p.getX(), (int)p.getY(), (int)p.getX()+6*PLOT_SIZE, (int)p.getY());
	        	g2d.setFont(new java.awt.Font("Arial",java.awt.Font.PLAIN, 2*PLOT_SIZE));
	        	g2d.setColor(java.awt.Color.WHITE);
	        	g2d.drawString(Title, (int)p.getX()+PLOT_SIZE, (int)p.getY()-PLOT_SIZE);
	        	g2d.setColor(java.awt.Color.YELLOW);
	        	g2d.drawString(getPlotNumber(), (int)p.getX()+PLOT_SIZE, (int)p.getY()+2*PLOT_SIZE);	
	    	}
		}
	}

}
