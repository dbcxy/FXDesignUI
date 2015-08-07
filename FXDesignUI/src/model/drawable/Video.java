package model.drawable;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import utils.Constance;
import javafx.scene.canvas.GraphicsContext;
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
		java.awt.Color.decode("#1F7A1F"),//DARK BLACK
		java.awt.Color.decode("#248F24"),
		java.awt.Color.decode("#29A329"),
		java.awt.Color.decode("#2EB82E"),
		java.awt.Color.decode("#33CC33"),
		java.awt.Color.decode("#47D147"),
		java.awt.Color.decode("#5CD65C"),
		java.awt.Color.decode("#70DB70"),
		java.awt.Color.decode("#85E085"),
		java.awt.Color.decode("#99E699"),//LIGHT GREEN
		java.awt.Color.decode("#ADEBAD")
	};
	
	private int val;
	private double range;
	private double az;
	private double el;
	
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

	public void setAz(double az) {
		this.az = az;
	}

	public double getEl() {
		return el;
	}

	public void setEl(double el) {
		this.el = el;
	}

	@Override
	public void draw(GraphicsContext gc) {
		if((range/1000) <= MatrixRef.getInstance().getVisibleRange() && Constance.SHOW_RAW) {
			gc.setFill(COLOR[val/25]);
			gc.fillRect(getX(), getY(), 1, 1);
		}
	}

	public void drawOnImage(BufferedImage bufferedImage) {
		if((range/1000) <= MatrixRef.getInstance().getVisibleRange() && Constance.SHOW_RAW) {
			Graphics2D g2d = bufferedImage.createGraphics();
			g2d.setColor(COLOR_IMG[val/25]);
	        g2d.drawRect((int) getX(), (int) getY(), 1, 1);
		}
	}

}
