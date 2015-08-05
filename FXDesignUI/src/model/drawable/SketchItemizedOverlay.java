package model.drawable;

import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import model.ItemizedOverlay;
import model.OverlayItem;
import model.graph.ILayoutParam;

public class SketchItemizedOverlay extends ItemizedOverlay<OverlayItem> implements ILayoutParam{

	static final Logger logger = Logger.getLogger(SketchItemizedOverlay.class);
		
	public SketchItemizedOverlay() {
		
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
	
	public void drawVideos(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.save();
		//draw points on Image

		
		//show image
//		gc.drawImage(img, 0, 0);
		gc.restore();
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
	}
	
}
