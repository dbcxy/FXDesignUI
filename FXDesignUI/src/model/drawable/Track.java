package model.drawable;

import model.MatrixRef;
import model.OverlayItem;
import model.graph.ILayoutParam;
import utils.Constance;
import utils.ModelDrawing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Track extends OverlayItem implements ILayoutParam{

	private int trackNumber;
	private boolean isTextShown = true;
	
	private double elevation;
	private double azimuth;
	private double range;
	
	private boolean isEl = false;
	private boolean isAz = false;
	
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
	    	gc.fillOval(x-OFFSET, y-OFFSET, HGAP, HGAP);
	    	gc.setStroke(Color.WHITE);
	    	gc.setLineWidth(2);
	    	gc.strokeOval(x-OFFSET, y-OFFSET, HGAP, HGAP);
	    	gc.strokeLine(x,y+HGAP,getX(),y-HGAP);
	    	gc.strokeLine(x+HGAP, y, x-HGAP, y);
	    	
	    	if(isTextShown) {
	        	gc.setStroke(Color.CHOCOLATE);
	        	ModelDrawing.drawLineAtAngle(gc, x, y, HGAP, -45);
	        	Point p = ModelDrawing.getNextPointAtAngle(x, y, HGAP, -45);
	        	gc.strokeLine(p.getX(), p.getY(), p.getX()+2*TEXT_OFFSET, p.getY());
	        	gc.setFont(new Font("Arial", 14));
	        	gc.setStroke(Color.WHITE);
	        	gc.strokeText(getTitle(), p.getX()+OFFSET, p.getY()-OFFSET);
	        	gc.setStroke(Color.YELLOW);
	        	gc.strokeText(getTrackNumber(), p.getX()+OFFSET, p.getY()+HGAP);
	    	}
		}
	}

//	public void drawOnImage(Canvas canvas) {
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//    	gc.save();
//    	
//    	
//    	BufferedImage bufferedImage = new BufferedImage((int) (canvas.getWidth()), 
//				 (int) (canvas.getHeight()), BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g2d = bufferedImage.createGraphics();
//		g2d.setColor(java.awt.Color.RED);
//		g2d.fillOval((int)getX()-OFFSET, (int)getY()-OFFSET, HGAP, HGAP);
//		g2d.setColor(java.awt.Color.WHITE);
//		g2d.setStroke(new BasicStroke(2));
//		g2d.drawOval((int)getX()-OFFSET, (int)getY()-OFFSET, HGAP, HGAP);
//		g2d.drawLine((int)getX(),(int)getY()+HGAP,(int)getX(),(int)getY()-HGAP);
//		g2d.drawLine((int)getX()+HGAP, (int)getY(), (int)getX()-HGAP, (int)getY());
//    	
//    	if(isTextShown)
//			displayText(g2d);
//		
//    	WritableImage wr = null;
//		Image img = SwingFXUtils.toFXImage(bufferedImage, wr);  
//    	
//    	gc.drawImage(img, 0, 0);
//    	gc.restore();
//		
//	}
//
//	private void displayText(Graphics2D g2d) {
//		g2d.setColor(java.awt.Color.BLUE);
//    	ModelDrawing.drawLineAtAngle(g2d, getX(), getY(), HGAP, -45);
//    	Point p = ModelDrawing.getNextPointAtAngle(getX(), getY(), HGAP, -45);
//    	g2d.drawLine((int)p.getX(), (int)p.getY(), (int)p.getX()+2*TEXT_OFFSET, (int)p.getY());
//    	g2d.setFont(new java.awt.Font("Arial",java.awt.Font.PLAIN, 14));
//    	g2d.setColor(java.awt.Color.WHITE);
//    	g2d.drawString(getTitle(), (int)p.getX()+OFFSET, (int)p.getY()-OFFSET);
//    	g2d.setColor(java.awt.Color.YELLOW);
//    	g2d.drawString(getTrackNumber(), (int)p.getX()+OFFSET, (int)p.getY()+HGAP);		
//	}

}
