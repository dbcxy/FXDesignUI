package model.drawable;

import org.apache.log4j.Logger;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import model.ItemizedOverlay;
import model.OverlayItem;
import model.graph.ILayoutParam;

public class SketchItemizedOverlay extends ItemizedOverlay<OverlayItem> implements ILayoutParam{

	static final Logger logger = Logger.getLogger(SketchItemizedOverlay.class);
	
//	public static final int TRACK 	= 1;
//	public static final int PLOT 	= 2;
//	public static final int VIDEO 	= 3;
		
	public SketchItemizedOverlay() {
		
	}
	
	public void drawTracks(GraphicsContext gc) {
		for(int i=0;i<size();i++)
			((Track) getItem(i)).draw(gc);
	}
	
	public void drawPlots(GraphicsContext gc) {
		for(int i=0;i<size();i++)
			((Plot) getItem(i)).draw(gc);
	}
	
	public void drawVideos(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.save();
		//draw points on Image
		for(int i=0;i<size();i++)
			((Video) getItem(i)).draw(gc);
		
		//show image
//		gc.drawImage(img, 0, 0);
		gc.restore();
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
	}
	
}
