package model.drawable;

import org.apache.log4j.Logger;

import javafx.scene.canvas.GraphicsContext;
import model.ItemizedOverlay;
import model.OverlayItem;
import model.graph.ILayoutParam;

public class SketchItemizedOverlay extends ItemizedOverlay<OverlayItem> implements ILayoutParam{

	static final Logger logger = Logger.getLogger(SketchItemizedOverlay.class);
	
	public static final int TRACK 	= 1;
	public static final int PLOT 	= 2;
	public static final int VIDEO 	= 3;
	
	private int mType;
	
	public SketchItemizedOverlay(int type) {
		mType = type;
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		
		for(int i=0;i<size();i++) {
			switch (mType) {
			case TRACK:
				Track track = (Track) getItem(i);
				track.draw(gc);
				break;
				
			case PLOT:
				Plot plot = (Plot) getItem(i);
				plot.draw(gc);
				break;
				
			case VIDEO:
				
				break;
	
			default:
				break;
			}
		}
	}
	
}
