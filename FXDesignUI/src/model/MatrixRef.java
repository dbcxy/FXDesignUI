package model;

import org.apache.log4j.Logger;

import utils.Constance;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import model.graph.ILayoutParam;

public class MatrixRef implements ILayoutParam{
	
	private static final Logger logger = Logger.getLogger(MatrixRef.class);
	private double actualXdimen;
	private double actualYdimen;
	
	private double drawableXArea;
	private double drawableYArea;
	
	private double maxElevation;
	private double minElevation;
	
	private double maxRange;
	private double minRange;
	private double visibleRange;
	
	private double maxAzimuth;
	private double minAzimuth;
	
	private double touchDown;
//	private int zoomLevel;
	
	private static MatrixRef instance = null;
	
	protected MatrixRef() {
		// Exists only to defeat instantiation.
	}
	
	public static MatrixRef getInstance() {
	      if(instance == null) {
	         instance = new MatrixRef();
	         logger.info("Matrix Ref Instantiated");
	      }
	      return instance;
	}
	
	public void setActualXY(double acutalX, double actualY) {
		actualXdimen = acutalX;
		actualYdimen = actualY;
		
		drawableXArea = actualXdimen - OFFSET;
		drawableYArea = actualYdimen - TEXT_OFFSET;
	}
	
	public void setElevationVal(double max, double min) {
		maxElevation = max;
		minElevation = min;
	}
	
	public void setAzimuthVal(double max, double min) {
		maxAzimuth = max;
		minAzimuth = min;
	}
	
	public void setRangeVal(double max, double min) {
		maxRange = max;
		minRange = min;
	}
	
	public void setTouchDown(double td) {
		touchDown = td;
	}
	
	public double toRangePixels(double r) {
		return (((r*drawableXArea)/visibleRange));
	}
	
	public double toMaxRangePixels(double r) {
		return (((r*drawableXArea)/maxRange));
	}
	
	public Point toElevationRangePixels(double el, double r) {
		Point p = new Point();
		double x = toRangePixels(r);
		double y = toElevationPixels(el);			
		p.setX(x);
		p.setY(y);
		return p;
	}
	
	public double toElevationPixels(double el) {
		return  drawableYArea - ((el*drawableYArea)/maxElevation);
	}
	
	public Point toAzimuthRangePixels(double az, double r) {
		Point p = new Point();
		double x = toRangePixels(r);
		double y = toAzimuthPixels(az);
		p.setX(x);
		p.setY(y);
		return p;
	}
	
	public double toAzimuthPixels(double az) {
		double y;
		double addtionalYarea = drawableYArea ;
		if((0 < az) && (az < maxAzimuth))
			y = addtionalYarea/2 - ((az*(addtionalYarea/2))/maxAzimuth);
		else if((minAzimuth <= az) && (az < 0))
			y = addtionalYarea/2 + ((az*(addtionalYarea/2))/minAzimuth);
		else if(az==maxAzimuth)
			y = 0;
		else if(az==minAzimuth)
			y = addtionalYarea;
		else
			y = addtionalYarea/2;
		return y;
	}
	
	
	@Override
	public void draw(GraphicsContext gc) {
		
	}

	public double getActualXdimen() {
		return actualXdimen;
	}

	public void setActualXdimen(double actualXdimen) {
		this.actualXdimen = actualXdimen;
	}

	public double getActualYdimen() {
		return actualYdimen;
	}

	public void setActualYdimen(double actualYdimen) {
		this.actualYdimen = actualYdimen;
	}

	public double getDrawableXArea() {
		return drawableXArea;
	}

	public void setDrawableXArea(double drawableXArea) {
		this.drawableXArea = drawableXArea;
	}

	public double getDrawableYArea() {
		return drawableYArea;
	}

	public void setDrawableYArea(double drawableYArea) {
		this.drawableYArea = drawableYArea;
	}

	public double getMaxElevation() {
		return maxElevation;
	}

	public void setMaxElevation(double maxElevation) {
		this.maxElevation = maxElevation;
	}

	public double getMinElevation() {
		return minElevation;
	}

	public void setMinElevation(double minElevation) {
		this.minElevation = minElevation;
	}

	public double getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(double maxRange) {
		this.maxRange = maxRange;
	}

	public double getMinRange() {
		return minRange;
	}

	public void setMinRange(double minRange) {
		this.minRange = minRange;
	}
	
	public double getVisibleRange() {
		return visibleRange;
	}

	public void setVisibleRange(double visibleRange) {
		this.visibleRange = visibleRange;
	}

	public double getMaxAzimuth() {
		return maxAzimuth;
	}

	public void setMaxAzimuth(double maxAzimuth) {
		this.maxAzimuth = maxAzimuth;
	}

	public double getMinAzimuth() {
		return minAzimuth;
	}

	public void setMinAzimuth(double minAzimuth) {
		this.minAzimuth = minAzimuth;
	}

	public double getTouchDown() {
		return touchDown;
	}

}
