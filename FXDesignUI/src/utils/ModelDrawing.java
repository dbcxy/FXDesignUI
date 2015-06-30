package utils;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;

public class ModelDrawing {

	public static void drawLineAtAngle(GraphicsContext gc, double x1,double y1,double length,double angle) {
		angle = angle * Math.PI / 180; 
	    gc.strokeLine(x1,y1,x1 + length * Math.cos(angle),y1 + length * Math.sin(angle));
	}
	
	public static void drawLineAtAngle(Graphics2D g2d, double x1,double y1,double length,double angle) {
		angle = angle * Math.PI / 180; 
	    g2d.drawLine((int)x1,(int)y1,(int)(x1 + length * Math.cos(angle)),(int)(y1 + length * Math.sin(angle)));
	}
	
	public static void drawDashedLine(Graphics g, double x1, double y1, double x2, double y2) {

        //creates a copy of the Graphics instance
        Graphics2D g2d = (Graphics2D) g.create();

        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine((int)x1, (int)y1, (int)x2, (int)y2);

        //gets rid of the copy
        g2d.dispose();
	}
	
	public static Point getPointOfLineAtAngle(GraphicsContext gc, double x1,double y1,double length,double angle) {
		angle = angle * Math.PI / 180; 
		double x2 = x1 + length * Math.cos(angle);
		double y2 = y1 + length * Math.sin(angle);
	    gc.strokeLine(x1,y1,x2,y2);
	    return new Point(x2,y2,0,null);
	}
	
	public static Point getNextPointAtAngle(double x1, double y1, double len, double angle){
		angle = angle * Math.PI / 180;
		Point point = new Point();
		point.setX(x1+len*Math.cos(angle));
		point.setY(y1+len*Math.sin(angle));
		return point;
	}
	
	public static void runSafe(final Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable");
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        }
        else {
            Platform.runLater(runnable);
        }
    }
}
