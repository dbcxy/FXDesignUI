package utils;

import java.awt.Graphics2D;
import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;

public class ModelDrawing {

	public static void drawLineAtAngle(GraphicsContext gc, double x1,double y1,double length,double angle) {
		angle = angle * Math.PI / 180; 
	    gc.strokeLine(x1,y1,x1 + length * Math.cos(angle),y1 + length * Math.sin(angle));
	}
	
	public static void drawLineAtAngle(Graphics2D g2d, int x1,int y1,int length,double angle) {
		angle = angle * Math.PI / 180; 
	    g2d.drawLine(x1,y1,(int)(x1 + length * Math.cos(angle)),(int)(y1 + length * Math.sin(angle)));
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
