package model.drawable;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import utils.ModelDrawing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
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
		java.awt.Color.decode("#296614"),//DARK GREEN
		java.awt.Color.decode("#33801A"),
		java.awt.Color.decode("#3D991F"),
		java.awt.Color.decode("#47B224"),
		java.awt.Color.decode("#52CC29"),
		java.awt.Color.decode("#5CE62E"),
		java.awt.Color.decode("#66FF33"),
		java.awt.Color.decode("#75FF47"),
		java.awt.Color.decode("#85FF5C"),
		java.awt.Color.decode("#94FF70"),//LIGHT GREEN
		java.awt.Color.decode("#A3FF85")
	};
	
	private int val;
	
	public Video() {
		super(null,null,null);
	}
	
	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = (val & 0xFF);
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(COLOR[val/25]);
		gc.fillRect(getX(), getY(), 1, 1);
	}

	public void drawOnImage(BufferedImage bufferedImage) {
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setColor(COLOR_IMG[val/25]);
        g2d.drawRect((int) getX(), (int) getY(), 1, 1);	
	}

}
