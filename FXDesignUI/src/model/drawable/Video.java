package model.drawable;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import utils.Constance;
import utils.ModelDrawing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import model.MatrixRef;
import model.OverlayItem;
import model.graph.ILayoutParam;

public class Video extends OverlayItem implements ILayoutParam{

	private static final Color[] COLOR = new Color[] {
		Color.valueOf("#296614"),//DARK GREEN
		Color.valueOf("#33801A"),
		Color.valueOf("#3D991F"),
		Color.valueOf("#47B224"),
		Color.valueOf("#52CC29"),
		Color.valueOf("#5CE62E"),
		Color.valueOf("#66FF33"),
		Color.valueOf("#75FF47"),
		Color.valueOf("#85FF5C"),
		Color.valueOf("#94FF70"),//LIGHT GREEN
		Color.valueOf("#A3FF85")
	};
	
	private static final java.awt.Color[] COLOR_IMG = new java.awt.Color[] {
		java.awt.Color.decode("#0F3D0F"),//DARK BLACK or LOW value
		java.awt.Color.decode("#248F24"),
		java.awt.Color.decode("#29A329"),
		java.awt.Color.decode("#2EB82E"),
		java.awt.Color.decode("#33CC33"),
		java.awt.Color.decode("#47D147"),
		java.awt.Color.decode("#5CD65C"),
		java.awt.Color.decode("#70DB70"),
		java.awt.Color.decode("#85E085"),
		java.awt.Color.decode("#99E699"),//LIGHT GREEN or HIGH value
		java.awt.Color.decode("#ADEBAD")
	};
	
	private int val;
	private double range;
	private double az;
	private double el;
	
	private boolean isAz = false;
	private boolean isEl = false;
	
	public Video() {
		super(null,null,null);
	}
	
	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = (val & 0xFF);
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getAz() {
		return az;
	}

	public void setAzimuth(double az) {
		this.az = az;
	}

	public double getEl() {
		return el;
	}

	public void setElevation(double el) {
		this.el = el;
	}
	
	public void setAz(boolean isAz) {
		this.isAz = isAz;
	}

	public void setEl(boolean isEl) {
		this.isEl = isEl;
	}

	private void evaluateXYZ() {
		MatrixRef matrixRef = MatrixRef.getInstance();
		setX(MatrixRef.getInstance().toRangePixels(range/1000));
		if(isAz) {
	    	double midAzimuth = (matrixRef.getMinAzimuth()+matrixRef.getMaxAzimuth())/2;
			double midAzimuthOffset = matrixRef.toRangePixels(Constance.AZIMUTH.RCLO/Constance.RANGE_DISP);
			Point start = matrixRef.toAzimuthRangePixels(midAzimuth, matrixRef.getMinRange());
	    	Point point = ModelDrawing.getNextPointAtAngle(start.getX(), start.getY()+midAzimuthOffset, getX(), -az);
	    	setY(point.getY());
		}
		if(isEl) {
	        Point start = matrixRef.toElevationRangePixels(matrixRef.getMinElevation(), matrixRef.getMinRange());
	    	Point point = ModelDrawing.getNextPointAtAngle(start.getX(), start.getY(), getX(), -el);
	    	setZ(point.getY());
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		if((range/1000) <= MatrixRef.getInstance().getVisibleRange() && Constance.SHOW_RAW) {
	    	evaluateXYZ();
	    	double x = getX();
			double y = 0;
			if(isAz) {
				y = getY();
			} else if(isEl) {
				y = getZ();
			}
			
			gc.setFill(COLOR[val/25]);
			gc.fillRect(x, y, 1, 1);
		}
	}

	public void drawOnImage(BufferedImage bufferedImage) {
		if((range/1000) <= MatrixRef.getInstance().getVisibleRange() && Constance.SHOW_RAW) {
			evaluateXYZ();
			double x = getX();
			double y = 0;
			if(isAz) {
				y = getY();
			} else if(isEl) {
				y = getZ();
			}
			
			Graphics2D g2d = bufferedImage.createGraphics();
			g2d.setColor(COLOR_IMG[val/25]);
	        g2d.drawRect((int) x, (int) y, 1, 1);
		}
	}

}
