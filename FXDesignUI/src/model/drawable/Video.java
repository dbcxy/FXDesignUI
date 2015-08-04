package model.drawable;

import javafx.scene.canvas.GraphicsContext;
import model.OverlayItem;
import model.graph.ILayoutParam;

public class Video extends OverlayItem implements ILayoutParam{

	public Video() {
		this(null,null);
	}
	
	public Video(Double x, Double y) {
		super(x, y);
	}

	@Override
	public void draw(GraphicsContext gc) {
		
	}

}
