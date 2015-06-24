package views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ResizableCanvas extends Canvas {
	
	private double scaledWidth;
	private double scaledHeight;
	private boolean once = true;
	 
    public ResizableCanvas() {
        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    private void draw() {
    	double width = getWidth();
        double height = getHeight();
        if(once) {
        	scaledWidth = width;
        	scaledHeight = height;
        }
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width, height);
        gc.setStroke(Color.RED);
        gc.strokeLine(0, 0, width, height);
        gc.strokeLine(0, height, width, 0);
        System.out.println("ScaledCanvas: "+scaledWidth+","+scaledHeight);
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
    
    public void setScaledDimension(boolean is) {
    	this.once = is;
    }
    
    public void setScaledWidth(double sWidth) {
    	this.scaledWidth = sWidth;
    }
    
    public void setScaledHeight(double sHeight) {
    	this.scaledHeight = sHeight;
    }
    
    public double getScaledWidth() {
    	return this.scaledWidth;
    }
    
    public double getScaledHeight() {
    	return this.scaledHeight;
    }
}