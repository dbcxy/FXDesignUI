package model.graph;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import model.GraphChart;
import utils.Constance;
import utils.ModelDrawing;
import views.ResizableCanvas;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AzimuthChart extends GraphChart {
	
	double azimuthAngle;
	double dist;
	double deltaDist;
	
	public AzimuthChart(Canvas canvas) {
		super(canvas);
	}
	
	public void drawAzimuthLineOnImage(double azAngle) {
		azimuthAngle = azAngle;
        g2d.setColor(java.awt.Color.CYAN);
        g2d.setStroke(new BasicStroke(2));
        ModelDrawing.drawLineAtAngle(g2d, OFFSET, (int)HEIGHT_OFF/2,(int) WIDTH_OFF, -azimuthAngle);//cross line top az degrees
        ModelDrawing.drawLineAtAngle(g2d, OFFSET, (int)HEIGHT_OFF/2,(int) WIDTH_OFF, azimuthAngle);//cross line bottom az degrees
	}
	
	public void drawAzimuthLine(double azAngle) {
		azimuthAngle = azAngle;
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);
        ModelDrawing.drawLineAtAngle(gc, OFFSET, HEIGHT_OFF/2, WIDTH_OFF, -azAngle);//cross line at top az degrees
        ModelDrawing.drawLineAtAngle(gc, OFFSET, HEIGHT_OFF/2, WIDTH_OFF, azAngle);//cross line at bottom az degrees
	}
	
	public void drawLandingStripOnImage(double dist) {
        g2d.setColor(java.awt.Color.LIGHT_GRAY);
        g2d.drawLine((int)(OFFSET+dist),(int)(HEIGHT_OFF/2-OFFSET/2),(int)(OFFSET+OFFSET*dist),(int)(HEIGHT_OFF/2-OFFSET/2));//above center line
        g2d.drawLine((int)(OFFSET+dist),(int)(HEIGHT_OFF/2+OFFSET/2),(int)(OFFSET+OFFSET*dist),(int)(HEIGHT_OFF/2+OFFSET/2));//below center line
        g2d.setColor(java.awt.Color.RED);
        g2d.drawLine((int)(OFFSET+dist),(int)(HEIGHT_OFF/2),(int)(OFFSET+(1+OFFSET)*dist),(int)(HEIGHT_OFF/2));//center line
        g2d.setColor(java.awt.Color.CYAN);
        g2d.setStroke(new BasicStroke((float) 0.5));
        Point p1 = ModelDrawing.getNextPointAtAngle(OFFSET+dist, HEIGHT_OFF/2, OFFSET+(OFFSET-1)*dist, (azimuthAngle-0.5)/2);//Point below
        ModelDrawing.drawDashedLine(g2d, OFFSET+dist, HEIGHT_OFF/2, p1.getX(), p1.getY());
        Point p2 = ModelDrawing.getNextPointAtAngle(OFFSET+dist, HEIGHT_OFF/2, OFFSET+(OFFSET-1)*dist, -(azimuthAngle-0.5)/2);//Point above
        ModelDrawing.drawDashedLine(g2d, OFFSET+dist, HEIGHT_OFF/2, p2.getX(), p2.getY());
	}
	
	public void drawLandingStrip(double dist) {
        gc.setStroke(Color.AQUAMARINE);
        gc.strokeLine(OFFSET+dist,HEIGHT_OFF/2-OFFSET/2,OFFSET+OFFSET*dist,HEIGHT_OFF/2-OFFSET/2);//above center line
        gc.strokeLine(OFFSET+dist,HEIGHT_OFF/2+OFFSET/2,OFFSET+OFFSET*dist,HEIGHT_OFF/2+OFFSET/2);//below center line
        gc.setStroke(Color.RED);
        gc.strokeLine(OFFSET+dist,HEIGHT_OFF/2,OFFSET+(1+OFFSET)*dist,HEIGHT_OFF/2);//center line
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
//        gc.setLineDashes(OFFSET/2);
        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF/2, OFFSET+(OFFSET-1)*dist, (azimuthAngle-0.5)/2);//imaginary below line
        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF/2, OFFSET+(OFFSET-1)*dist, -(azimuthAngle-0.5)/2);//imaginary above line
//        gc.setLineDashes(0);
	}
	
	public void drawDistanceGridOnImage(double distance, double deltaDistance) {
		this.dist = distance;
		this.deltaDist = deltaDistance;
		g2d.setStroke(new BasicStroke((float) 1.5));
        Point pTop = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF/2, deltaDist, -Math.floor(azimuthAngle));
        Point pBtm = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF/2, deltaDist, Math.floor(azimuthAngle));
        for(int i=0;i<(WIDTH_OFF/dist)-1;i++){
        	if(i==0) {
        		
        		//dotted red line
        		g2d.setColor(java.awt.Color.RED);
//        		gc.setLineDashes(OFFSET/2);
        		g2d.drawLine((int)(OFFSET+dist+2*OFFSET),(int)pTop.getY(),(int)(OFFSET+dist+2*OFFSET),(int)pBtm.getY());
//        		gc.setLineDashes(0);
        		
        		//reset color
        		g2d.setColor(java.awt.Color.GRAY);
        	} else if((i%5)==0) {
        		g2d.setColor(java.awt.Color.YELLOW);
        	} else
        		g2d.setColor(java.awt.Color.GREEN);
            g2d.drawLine((int)(OFFSET+(i+1)*dist),(int)pTop.getY(),(int)(OFFSET+(i+1)*dist),(int)pBtm.getY());
            pBtm = ModelDrawing.getNextPointAtAngle(pBtm.getX(), pBtm.getY(), deltaDist, Math.floor(azimuthAngle));
            pTop = ModelDrawing.getNextPointAtAngle(pTop.getX(), pTop.getY(), deltaDist, -Math.floor(azimuthAngle));
        }
	}
	
	public void drawDistanceGrid(double distance, double deltaDistance) {
		dist = distance;
		deltaDist = deltaDistance;
		gc.setLineWidth(1.5);    
        Point pTop = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF/2, deltaDist, -Math.floor(azimuthAngle));
        Point pBtm = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF/2, deltaDist, Math.floor(azimuthAngle));
        for(int i=0;i<(WIDTH_OFF/dist)-1;i++){
        	if(i==0) {
        		
        		//dotted red line
        		gc.setStroke(Color.RED);
//        		gc.setLineDashes(OFFSET/2);
        		gc.strokeLine(OFFSET+dist+2*OFFSET,pTop.getY(),OFFSET+dist+2*OFFSET,pBtm.getY());
//        		gc.setLineDashes(0);
        		
        		//reset color
        		gc.setStroke(Color.CHARTREUSE);
        	} else if((i%5)==0) {
        		gc.setStroke(Color.YELLOW);
        	} else
        		gc.setStroke(Color.GREEN);
            gc.strokeLine(OFFSET+(i+1)*dist,pTop.getY(),OFFSET+(i+1)*dist,pBtm.getY());
            pBtm = ModelDrawing.getNextPointAtAngle(pBtm.getX(), pBtm.getY(), deltaDist, Math.floor(azimuthAngle));
            pTop = ModelDrawing.getNextPointAtAngle(pTop.getX(), pTop.getY(), deltaDist, -Math.floor(azimuthAngle));
        }
	}
	
	public static Image getImage(BufferedImage bufferedImage) {
		WritableImage wr = null;
		Image img = SwingFXUtils.toFXImage(bufferedImage, wr);  
		return img;
	}
	
	public static BufferedImage drawTextOnImage(Canvas canvas) {
        int count = 0;
        BufferedImage bufferedImage = new BufferedImage((int) (canvas.getWidth()), 
				 (int) canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 16));
        g2d.setColor(java.awt.Color.RED);
        g2d.drawString("AZ Ang    : "+Constance.AZ_ANGLE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.setColor(java.awt.Color.YELLOW);
        g2d.drawString("EL Tilt      : "+Constance.EL_TILT, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;count++;
        
        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 12));
        g2d.setColor(java.awt.Color.CYAN);
        g2d.drawString("Approach Angle   : "+Constance.APPROACH_ANGLE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        g2d.drawString("Offset                   : "+Constance.OFFSET, OFFSET, TEXT_OFFSET+HGAP*count);
        count = 0;
        
        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 18));
        g2d.setColor(java.awt.Color.YELLOW);
        g2d.drawString("-", OFFSET, (int) (HEIGHT_OFF/2+HGAP+OFFSET));
        g2d.drawString("+", OFFSET, (int) (HEIGHT_OFF/2-HGAP));
        
        return bufferedImage;
	}
	
	public void drawText() {
        int count = 0;
        gc.setFont(new Font("Sans Serif", 16));
        gc.setStroke(Color.RED);
        gc.strokeText("AZ Ang    : "+Constance.AZ_ANGLE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        gc.setStroke(Color.YELLOW);
        gc.strokeText("EL Tilt      : "+Constance.EL_TILT, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;count++;
        
        gc.setFont(new Font("Sans Serif", 12));
        gc.setStroke(Color.ALICEBLUE);
        gc.strokeText("Approach Angle   : "+Constance.APPROACH_ANGLE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        gc.setStroke(Color.AQUA);
        gc.strokeText("Offset                   : "+Constance.OFFSET, OFFSET, TEXT_OFFSET+HGAP*count);
        count = 0;
        
        gc.setFont(new Font("Sans Serif", 18));
        gc.setStroke(Color.YELLOW);
        gc.strokeText("-", OFFSET, HEIGHT_OFF/2+HGAP+OFFSET);
        gc.strokeText("+", OFFSET, HEIGHT_OFF/2-HGAP);
	}

}
