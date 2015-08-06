package utils;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;

public class ModelDrawing {
	
	public enum FLIP { L2R, R2L };

	public static void drawLineAtAngle(GraphicsContext gc, double x1,double y1,double length,double angle) {
	    gc.strokeLine(x1,y1,x1 + length * Math.cos(Math.toRadians(angle)),y1 + length * Math.sin(Math.toRadians(angle)));
	}
	
	public static void drawLineAtAngle(Graphics2D g2d, double x1,double y1,double length,double angle) {
	    g2d.drawLine((int)x1,(int)y1,(int)(x1 + length * Math.cos(Math.toRadians(angle))),(int)(y1 + length * Math.sin(Math.toRadians(angle))));
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
	
	public static Point getNextPointAtAngle(double x1, double y1, double lenPx, double angle){
		Point point = new Point();
		point.setX(x1+lenPx*Math.cos(Math.toRadians(angle)));
		point.setY(y1+lenPx*Math.sin(Math.toRadians(angle)));
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
	
	public static void flipCanvasDrawing(Canvas canvas, FLIP flip) {
		if(flip.equals(FLIP.R2L)) {
			canvas.getGraphicsContext2D().save();
			canvas.setTranslateY(0);
			canvas.setScaleX(-1);
			canvas.setScaleY(1);
			canvas.getGraphicsContext2D().restore();
		} else {
			canvas.getGraphicsContext2D().save();
			canvas.setTranslateY(1);
			canvas.setScaleX(1);
			canvas.setScaleY(1);
			canvas.getGraphicsContext2D().restore();
		}
	}
}
