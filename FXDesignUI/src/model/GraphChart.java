package model;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import model.graph.ILayoutParam;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class GraphChart implements ILayoutParam{
	
	protected double actualWidth;
	protected double actualHeight;
	protected static double HEIGHT_OFF;
	protected static double WIDTH_OFF;
	
	Canvas mCanvas;
	protected GraphicsContext gc;
	
	BufferedImage bufferedImage;
	protected Graphics2D g2d;
	
	public GraphChart(Canvas canvas) {
		mCanvas = canvas;
    	gc = canvas.getGraphicsContext2D(); 
    	initBounds();
	}
	
	private void initBounds() {
		actualWidth = mCanvas.getWidth();
    	actualHeight = mCanvas.getHeight();
    	HEIGHT_OFF = actualHeight-TEXT_OFFSET;
    	WIDTH_OFF = actualWidth-OFFSET;
    	
    	bufferedImage = new BufferedImage((int) (actualWidth), 
				 (int) actualHeight, BufferedImage.TYPE_INT_ARGB);
		g2d = bufferedImage.createGraphics();
	}
	
	public void drawBackgroundOnImage() {
		g2d.clearRect(0, 0, (int)actualWidth, (int)actualHeight);
    	g2d.setColor(java.awt.Color.BLACK);
        g2d.fillRect(0,0,(int)actualWidth, (int)actualHeight);
	}
	
	public void drawBackground() {
    	gc.clearRect(0, 0, actualWidth, actualHeight);
    	gc.setFill(Color.BLACK);
        gc.fillRect(0,0,actualWidth, actualHeight);
	}
	
	public Image getScaledImage(double scale) {
		WritableImage wr = null;
		BufferedImage scaledBufImage = new BufferedImage((int) (scale*bufferedImage.getWidth()), 
				(int) (scale*bufferedImage.getHeight()), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		scaledBufImage = scaleOp.filter(bufferedImage, scaledBufImage);
		Image img = SwingFXUtils.toFXImage(scaledBufImage, wr);
		return img;
	}
	
	public void drawImage(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
    	gc.save();
    	gc.drawImage(getImage(), 0, 0);
    	gc.restore();
	}
	
	public void drawScaledImage(Canvas canvas, double scale) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
    	gc.save();
    	gc.drawImage(getScaledImage(scale), 0, 0);
    	gc.restore();
	}
	
	public Image getImage() {
		WritableImage wr = null;
		Image img = SwingFXUtils.toFXImage(bufferedImage, wr);  
		return img;
	}

	@Override
	public void draw(GraphicsContext gc) {
		
	}
	
}
