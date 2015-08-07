package model.drawable;

import java.awt.image.BufferedImage;
import java.util.Iterator;

import org.apache.log4j.Logger;

import utils.Constance;
import utils.ModelDrawing;
import javafx.animation.FadeTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;
import model.ItemizedOverlay;
import model.OverlayItem;
import model.graph.ILayoutParam;

public class SketchItemizedOverlay extends ItemizedOverlay<OverlayItem> implements ILayoutParam{

	static final Logger logger = Logger.getLogger(SketchItemizedOverlay.class);
//	BufferedImage bufferedImage;
	
	private FadeTransition fadeOut = new FadeTransition(
		    Duration.millis(250)
	);
		
	public SketchItemizedOverlay() {
		
	}
	
	public void drawVideosImage(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//		if(bufferedImage == null) {
//	    	bufferedImage = new BufferedImage((int) (canvas.getWidth()), 
//					 (int) (canvas.getHeight()), BufferedImage.TYPE_INT_ARGB);
//		}
		
		BufferedImage bImage = new BufferedImage((int) (canvas.getWidth()), 
				 (int) (canvas.getHeight()), BufferedImage.TYPE_INT_ARGB);
    	for(int i=0;i<size();i++)
			((Video) removeOverlayItem()).drawOnImage(bImage);
//    	bufferedImage = ModelDrawing.overlapBufferedImage(bufferedImage, bImage);
    	
		WritableImage wr = null;
		Image img = SwingFXUtils.toFXImage(bImage, wr);  	    
	    
		gc.drawImage(img, 0, 0);
	    
//		fadeOut.setNode(canvas);
//	    fadeOut.setFromValue(1);
//	    fadeOut.setToValue(0);
//	    fadeOut.setAutoReverse(true);
//	    fadeOut.play();
	}
	
	public void drawTracksImage(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
    	gc.save();
    	BufferedImage bufferedImage = new BufferedImage((int) (canvas.getWidth()), 
				 (int) (canvas.getHeight()), BufferedImage.TYPE_INT_ARGB);
		
    	for(int i=0;i<size();i++)
			((Track) removeOverlayItem()).drawOnImage(bufferedImage);
    	
		WritableImage wr = null;
		Image img = SwingFXUtils.toFXImage(bufferedImage, wr);  
	    	
	    gc.drawImage(img, 0, 0);
	    gc.restore();
	}
	
	public void drawPlotsImage(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
    	gc.save();
    	BufferedImage bufferedImage = new BufferedImage((int) (canvas.getWidth()), 
				 (int) (canvas.getHeight()), BufferedImage.TYPE_INT_ARGB);
		
    	for(int i=0;i<size();i++)
			((Plot) removeOverlayItem()).drawOnImage(bufferedImage);
    	
		WritableImage wr = null;
		Image img = SwingFXUtils.toFXImage(bufferedImage, wr);  
	    	
	    gc.drawImage(img, 0, 0);
	    gc.restore();
	}
	
	public void drawTracks(GraphicsContext gc) {
		for(int i=0;i<size();i++)
			((Track) removeOverlayItem()).draw(gc);
	}
	
	public void drawPlots(GraphicsContext gc) {
		for(int i=0;i<size();i++)
			((Plot) removeOverlayItem()).draw(gc);
	}
	
	public void drawVideos(GraphicsContext gc) {
		for(int i=0;i<size();i++)
			((Video) removeOverlayItem()).draw(gc);
	}
	
	public void putBlankVideos() {
		Iterator it = getOverlayIterator();
		while(it.hasNext())
			((Video)it.next()).setVal(0);			
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
	}
	
}
