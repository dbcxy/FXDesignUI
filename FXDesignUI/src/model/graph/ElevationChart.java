package model.graph;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import model.GraphChart;
import model.MatrixRef;
import utils.Constance;
import utils.ModelDrawing;
import views.ResizableCanvas;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ElevationChart extends GraphChart{
	
	double elevationAngle;
	double distAngle;
	double centerDist;
//	double deltaDist;
	
	MatrixRef matrixRef = MatrixRef.getInstance();
	Point startPoint;
	Point endPoint;
	
	public ElevationChart(Canvas canvas) {
		super(canvas);
	}
	
	public void drawElevationLine(double elAngle) {
		this.elevationAngle = elAngle;
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);        
        startPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), matrixRef.getMinRange());
        endPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), matrixRef.getMaxRange());        		
        gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());//flat line     
//        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(),matrixRef.getDrawableXArea(), -elevationAngle);//cross line at 20 degrees
	}
	
	public void drawDistanceGrid() {
		
        gc.setFont(new Font("Sans Serif", 16));
        gc.setLineWidth(1.5);  
        
        //write text
        startPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), matrixRef.getTouchDown());
        endPoint = matrixRef.toElevationPixels(Constance.ELEVATION_DISP, matrixRef.getTouchDown());
		gc.setStroke(Color.YELLOW);
		gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
		gc.strokeText(" TD ", startPoint.getX()-OFFSET, startPoint.getY()+HGAP);
  		
		//dotted red line
		gc.setStroke(Color.RED);
//		gc.setLineDashes(OFFSET/2);
		gc.strokeLine(startPoint.getX()+2*OFFSET,startPoint.getY(),endPoint.getX()+2*OFFSET,endPoint.getY()-OFFSET/2);
//		gc.setLineDashes(0);

		//draw remaining lines
        for(int i=(int) matrixRef.getTouchDown();i<matrixRef.getMaxRange()+Constance.RANGE_DISP;i+=Constance.RANGE_DISP){
        	
        	startPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), i);
            endPoint = matrixRef.toElevationPixels(i*Constance.ELEVATION_DISP, i);
            
            //completing cross line by joining
        	Point prevPoint = matrixRef.toElevationPixels((i-1)*Constance.ELEVATION_DISP, (i-1));
            gc.setStroke(Color.CYAN);
            gc.strokeLine(prevPoint.getX(),prevPoint.getY(),endPoint.getX(),endPoint.getY());
            

            if((i%5)==0) {
        		//write text NM
        		gc.setStroke(Color.YELLOW);
        		gc.strokeText(i+Constance.UNITS, startPoint.getX()-OFFSET, startPoint.getY()+HGAP);
        		gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
        	} else
            	gc.setStroke(Color.GREEN);

        	//draw green lines
            gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
        }
	}
	
	public void drawLandingStrip(double centerDist, double dAngle) {
		this.distAngle = dAngle;
		this.centerDist = centerDist;
        gc.setStroke(Color.AQUAMARINE);
        
        startPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), matrixRef.getTouchDown());
        endPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), centerDist-Constance.RANGE_DISP);
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(distAngle-1.5));//below center line
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(distAngle+1.5));//above center line
        
        gc.setStroke(Color.CYAN);
        gc.setLineWidth(1);
//        gc.setLineDashes(OFFSET/2);
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(distAngle-3.5));//imaginary below line
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -(distAngle+3.5));//imaginary above line
//        gc.setLineDashes(0);
        
        endPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), centerDist);
        gc.setStroke(Color.RED);
        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(), endPoint.getX(), -distAngle);//center red line
	}
	
//	public void drawElevationLine(double elAngle) {
//		this.elevationAngle = elAngle;
//        gc.setStroke(Color.CYAN);
//        gc.setLineWidth(2);
//        
//        startPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), matrixRef.getMinRange());
//        endPoint = matrixRef.toElevationPixels(matrixRef.getMinElevation(), matrixRef.getMaxRange());        		
//        gc.strokeLine(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());//flat line        
//        ModelDrawing.drawLineAtAngle(gc, startPoint.getX(), startPoint.getY(),matrixRef.getDrawableXArea(), -elevationAngle);//cross line at 20 degrees
//	}
//	
//	public void drawLandingStrip(double dist, double dAngle) {
//		this.distAngle = dAngle;
//        gc.setStroke(Color.AQUAMARINE);
//        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle-1.5));//below center line
//        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle+1.5));//above center line
//        gc.setStroke(Color.RED);
//        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF, OFFSET+OFFSET*dist, -distAngle);//center red line
//        gc.setStroke(Color.CYAN);
//        gc.setLineWidth(1);
////        gc.setLineDashes(OFFSET/2);
//        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle-3.5));//imaginary below line
//        ModelDrawing.drawLineAtAngle(gc, OFFSET+dist, HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle+3.5));//imaginary above line
////        gc.setLineDashes(0);
//	}	
//	
//	public void drawDistanceGrid(double distance, double deltaDistance) {
//		this.dist = distance;
//		this.deltaDist = deltaDistance;
//        gc.setFont(new Font("Sans Serif", 16));
//        gc.setLineWidth(1.5);       
//        Point p = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF, deltaDist, -elevationAngle);
//        for(int i=0;i<(WIDTH_OFF/dist)-1;i++){
//        	if(i==0) {
//        		//write text
//        		gc.setStroke(Color.YELLOW);
//        		gc.strokeText(" TD ", OFFSET+(i+1)*dist, HEIGHT_OFF+HGAP);
//        		
//        		//dotted red line
//        		gc.setStroke(Color.RED);
////        		gc.setLineDashes(OFFSET/2);
//        		gc.strokeLine(OFFSET+dist+2*OFFSET,HEIGHT_OFF,p.getX()+2*OFFSET,p.getY()-OFFSET/2);
////        		gc.setLineDashes(0);
//        		
//        		//reset color
//        		gc.setStroke(Color.CHARTREUSE);
//        	} else if((i%5)==0) {
//        		//write text NM
//        		gc.setStroke(Color.YELLOW);
//        		gc.strokeText(i+"NM", OFFSET+(i+1)*dist, HEIGHT_OFF+HGAP);
//        	} else
//        		gc.setStroke(Color.GREEN);
//
//        	//draw green lines
//            gc.strokeLine(OFFSET+(i+1)*dist,HEIGHT_OFF,p.getX(),p.getY());
//            p = ModelDrawing.getNextPointAtAngle(p.getX(), p.getY(), deltaDist, -elevationAngle);
//        }
//	}
	
	public static void drawText(Canvas canvas) {
		int count = 0;
		GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(new Font("Sans Serif", 16));
        gc.setStroke(Color.RED);
        gc.strokeText("EL Ang     : "+Constance.EL_ANGLE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        gc.setStroke(Color.YELLOW);
        gc.strokeText("AZ Tilt      : "+Constance.AZ_TILT, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;count++;
        
        gc.setFont(new Font("Sans Serif", 12));
        gc.setStroke(Color.ALICEBLUE);
        gc.strokeText("Glide Slope          : "+Constance.GLIDE_SLOPE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Safety Slope        : "+Constance.SAFETY_SLOPE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Safety Height      : "+Constance.SAFETY_HEIGHT, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;count++;
        gc.setStroke(Color.AQUA);
        gc.strokeText("Distance          : "+Constance.DISTANCE, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Height            : "+Constance.HEIGHT, OFFSET, TEXT_OFFSET+HGAP*count);
        count++;
        
        gc.setFont(new Font("Sans Serif", 14));
        gc.setStroke(Color.CADETBLUE);
        count = 0;
        gc.strokeText("Channel   : "+Constance.CHANNEL, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Control    : "+Constance.CONTROL, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Route      : "+Constance.ROUTE, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("RWY        : "+Constance.RWY, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("Scale       : "+Constance.SCALE, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        
        gc.setFont(new Font("Sans Serif", 14));
        gc.setStroke(Color.GREENYELLOW);
        count = 0;
        gc.strokeText("System Perform     : ", 2*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("System Setting      : ", 2*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText("System Logbook    : ", 2*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.setStroke(Color.YELLOW);
        count = 0;
        gc.strokeText(Constance.NULL, 2.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText(Constance.NULL, 2.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count++;
        gc.strokeText(Constance.NULL, 2.75*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
        count=0;		
	}
	
//	public void drawElevationLineOnImage(double elAngle) {
//		this.elevationAngle = elAngle;
//        g2d.setColor(java.awt.Color.CYAN);
//        g2d.setStroke(new BasicStroke(2));
//        g2d.drawLine(OFFSET,(int)HEIGHT_OFF,(int)WIDTH_OFF,(int)HEIGHT_OFF);//flat line
//        ModelDrawing.drawLineAtAngle(g2d, OFFSET, (int)HEIGHT_OFF,(int) WIDTH_OFF+2*OFFSET, -elevationAngle);//cross line at 20 degrees
//	}
//	
//	public void drawDistanceGridOnImage(double distance, double deltaDistance) {
//		this.dist = distance;
//		this.deltaDist = deltaDistance;
//        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 16));
//        g2d.setStroke(new BasicStroke((float) 1.5));   
//        Point p = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF, deltaDist, -elevationAngle);
//        for(int i=0;i<(WIDTH_OFF/dist)-1;i++){
//        	if(i==0) {
//        		//write text
//        		g2d.setColor(java.awt.Color.YELLOW);
//        		g2d.drawString(" TD ", (int) (OFFSET+(i+1)*dist), (int) (HEIGHT_OFF+HGAP));
//        		
//        		//dotted red line
//        		g2d.setColor(java.awt.Color.RED);
////        		gc.setLineDashes(OFFSET/2);
//        		g2d.drawLine((int) (OFFSET+dist+2*OFFSET),(int)HEIGHT_OFF,(int)p.getX()+2*OFFSET,(int)p.getY()-OFFSET/2);
////        		gc.setLineDashes(0);
//        		
//        		//reset color
//        		g2d.setColor(java.awt.Color.GRAY);
//        	} else if((i%5)==0) {
//        		//write text NM
//        		g2d.setColor(java.awt.Color.YELLOW);
//        		g2d.drawString(i+"NM", (int) (OFFSET+(i+1)*dist), (int) (HEIGHT_OFF+HGAP));
//        	} else
//        		g2d.setColor(java.awt.Color.GREEN);
//
//        	//draw green lines
//            g2d.drawLine((int) (OFFSET+(i+1)*dist),(int)HEIGHT_OFF,(int)p.getX(),(int)p.getY());
//            p = ModelDrawing.getNextPointAtAngle(p.getX(), p.getY(), deltaDist, -elevationAngle);
//        }
//	}
//	
//	
//	public void drawLandingStripOnImage(double dist, double dAngle) {
//		this.distAngle = dAngle;
//        g2d.setColor(java.awt.Color.LIGHT_GRAY);
//        ModelDrawing.drawLineAtAngle(g2d, OFFSET+dist, (int)HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle-1.5));//below center line
//        ModelDrawing.drawLineAtAngle(g2d, OFFSET+dist, (int)HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle+1.5));//above center line
//        g2d.setColor(java.awt.Color.RED);
//        ModelDrawing.drawLineAtAngle(g2d, OFFSET+dist, (int)HEIGHT_OFF, OFFSET+OFFSET*dist, -distAngle);//center red line
//        g2d.setColor(java.awt.Color.CYAN);
//        g2d.setStroke(new BasicStroke((float) 0.5));
//        Point p1 = ModelDrawing.getNextPointAtAngle(OFFSET+dist, HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle-3.5));//Point below
//        ModelDrawing.drawDashedLine(g2d, OFFSET+dist, HEIGHT_OFF, p1.getX(), p1.getY());
//        Point p2 = ModelDrawing.getNextPointAtAngle(OFFSET+dist, HEIGHT_OFF, OFFSET+(OFFSET-1)*dist, -(distAngle+3.5));//Point above
//        ModelDrawing.drawDashedLine(g2d, OFFSET+dist, HEIGHT_OFF, p2.getX(), p2.getY());
//	}
//	
//	public static Image getImage(BufferedImage bufferedImage) {
//		WritableImage wr = null;
//		Image img = SwingFXUtils.toFXImage(bufferedImage, wr);  
//		return img;
//	}
//	
//	public static BufferedImage drawTextOnImage(Canvas canvas) {
//		int count = 0;
//		BufferedImage bufferedImage = new BufferedImage((int) (canvas.getWidth()), 
//				 (int) canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g2d = bufferedImage.createGraphics();
//        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 16));
//        g2d.setColor(java.awt.Color.RED);
//        g2d.drawString("EL Ang     : "+Constance.EL_ANGLE, OFFSET, TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.setColor(java.awt.Color.YELLOW);
//        g2d.drawString("AZ Tilt      : "+Constance.AZ_TILT, OFFSET, TEXT_OFFSET+HGAP*count);
//        count++;count++;
//        
//        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 12));
//        g2d.setColor(java.awt.Color.CYAN);
//        g2d.drawString("Glide Slope          : "+Constance.GLIDE_SLOPE, OFFSET, TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.drawString("Safety Slope        : "+Constance.SAFETY_SLOPE, OFFSET, TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.drawString("Safety Height      : "+Constance.SAFETY_HEIGHT, OFFSET, TEXT_OFFSET+HGAP*count);
//        count++;count++;
//        g2d.setColor(java.awt.Color.CYAN);
//        g2d.drawString("Distance          : "+Constance.DISTANCE, OFFSET, TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.drawString("Height            : "+Constance.HEIGHT, OFFSET, TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 14));
//        g2d.setColor(java.awt.Color.BLUE);
//        count = 0;
//        g2d.drawString("Channel   : "+Constance.CHANNEL, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.drawString("Control    : "+Constance.CONTROL, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.drawString("Route      : "+Constance.ROUTE, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.drawString("RWY        : "+Constance.RWY, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.drawString("Scale       : "+Constance.SCALE, OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        g2d.setFont(new java.awt.Font("Sans Serif",java.awt.Font.PLAIN, 14));
//        g2d.setColor(java.awt.Color.GREEN);
//        count = 0;
//        g2d.drawString("System Perform     : ", 2*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.drawString("System Setting      : ", 2*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.drawString("System Logbook    : ", 2*OFFSET*HGAP, TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.setColor(java.awt.Color.YELLOW);
//        count = 0;
//        g2d.drawString(Constance.NULL, (int) (2.75*OFFSET*HGAP), TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.drawString(Constance.NULL, (int) (2.75*OFFSET*HGAP), TEXT_OFFSET+HGAP*count);
//        count++;
//        g2d.drawString(Constance.NULL, (int) (2.75*OFFSET*HGAP), TEXT_OFFSET+HGAP*count);
//        count=0;	
//        
//        return bufferedImage;
//	}
//		
//	public static void drawElevationLineOnScene(SubScene scene, double elAngle) {
//		Group root = new Group();
//		double HEIGHT_OFF = scene.getHeight()-TEXT_OFFSET;
//    	double WIDTH_OFF = scene.getWidth()-OFFSET;
//		Line lineFlat = new Line(OFFSET,HEIGHT_OFF,WIDTH_OFF,HEIGHT_OFF);//flat line
//		lineFlat.setStroke(Color.CYAN);
//		lineFlat.setStrokeWidth(2);
//		root.getChildren().add(lineFlat);
//
//		Point p = ModelDrawing.getNextPointAtAngle(OFFSET, HEIGHT_OFF, WIDTH_OFF+2*OFFSET, -elAngle);//Point at 20 degrees
//		Line lineAngle = new Line(OFFSET,HEIGHT_OFF,p.getX(),p.getY());//cross line
//		lineAngle.setStroke(Color.CYAN);
//		lineAngle.setStrokeWidth(2);		
//		root.getChildren().add(lineAngle);
//		
//		scene.setRoot(root);
//	}
//
//	public static void drawTextOnScene(SubScene scene) {
//		int count = 0;
//		Group root = new Group();
//		
//		Text t1 = new Text();
//        t1.setFont(new Font("Sans Serif", 16));
//        t1.setStroke(Color.RED);
//        t1.setText("EL Ang     : "+Constance.EL_ANGLE);
//        t1.setLayoutX(OFFSET);
//        t1.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        Text t2 = new Text();
//        t2.setFont(new Font("Sans Serif", 16));
//        t2.setStroke(Color.YELLOW);
//        t2.setText("AZ Tilt      : "+Constance.AZ_TILT);
//        t2.setLayoutX(OFFSET);
//        t2.setLayoutY(TEXT_OFFSET+HGAP*count);    
//        count++;count++;
//        
//        Text t3 = new Text();      
//        t3.setFont(new Font("Sans Serif", 12));
//        t3.setStroke(Color.ALICEBLUE);
//        t3.setText("Glide Slope          : "+Constance.GLIDE_SLOPE);
//        t3.setLayoutX(OFFSET);
//        t3.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        Text t4 = new Text();
//        t4.setFont(new Font("Sans Serif", 12));
//        t4.setStroke(Color.ALICEBLUE);
//        t4.setText("Safety Slope        : "+Constance.SAFETY_SLOPE);
//        t4.setLayoutX(OFFSET);
//        t4.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        Text t5 = new Text();
//        t5.setFont(new Font("Sans Serif", 12));
//        t5.setStroke(Color.ALICEBLUE);
//        t5.setText("Safety Height      : "+Constance.SAFETY_HEIGHT);
//        t5.setLayoutX(OFFSET);
//        t5.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;count++;
//        
//        Text t6 = new Text();
//        t6.setFont(new Font("Sans Serif", 12));
//        t6.setStroke(Color.AQUA);
//        t6.setText("Distance          : "+Constance.DISTANCE);
//        t6.setLayoutX(OFFSET);
//        t6.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        Text t7 = new Text();
//        t7.setFont(new Font("Sans Serif", 12));
//        t7.setStroke(Color.AQUA);        
//        t7.setText("Height            : "+Constance.HEIGHT);
//        t7.setLayoutX(OFFSET);
//        t7.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        
//        Text t8 = new Text();
//        t8.setFont(new Font("Sans Serif", 14));
//        t8.setStroke(Color.CADETBLUE);
//        count = 0;
//        t8.setText("Channel   : "+Constance.CHANNEL);
//        t8.setLayoutX(OFFSET*HGAP);
//        t8.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        Text t9 = new Text();
//        t9.setFont(new Font("Sans Serif", 14));
//        t9.setStroke(Color.CADETBLUE);
//        t9.setText("Control    : "+Constance.CONTROL);
//        t9.setLayoutX(OFFSET*HGAP);
//        t9.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        Text t10 = new Text();
//        t10.setFont(new Font("Sans Serif", 14));
//        t10.setStroke(Color.CADETBLUE);
//        t10.setText("Route      : "+Constance.ROUTE);
//        t10.setLayoutX(OFFSET*HGAP);
//        t10.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        Text t11 = new Text();
//        t11.setFont(new Font("Sans Serif", 14));
//        t11.setStroke(Color.CADETBLUE);
//        t11.setText("RWY        : "+Constance.RWY);
//        t11.setLayoutX(OFFSET*HGAP);
//        t11.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        Text t12 = new Text();
//        t12.setFont(new Font("Sans Serif", 14));
//        t12.setStroke(Color.CADETBLUE);        
//        t12.setText("Scale       : "+Constance.SCALE);
//        t12.setLayoutX(OFFSET*HGAP);
//        t12.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        
//        Text t13 = new Text(); 
//        t13.setFont(new Font("Sans Serif", 14));
//        t13.setStroke(Color.GREENYELLOW);
//        count = 0;
//        t13.setText("System Perform     : ");
//        t13.setLayoutX(2*OFFSET*HGAP);
//        t13.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        Text t14 = new Text(); 
//        t14.setFont(new Font("Sans Serif", 14));
//        t14.setStroke(Color.GREENYELLOW);        
//        t14.setText("System Setting      : ");
//        t14.setLayoutX(2*OFFSET*HGAP);
//        t14.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        Text t15 = new Text(); 
//        t15.setFont(new Font("Sans Serif", 14));
//        t15.setStroke(Color.GREENYELLOW);  
//        t15.setText("System Logbook    : ");
//        t15.setLayoutX(2*OFFSET*HGAP);
//        t15.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        Text t16 = new Text(); 
//        t16.setFont(new Font("Sans Serif", 14));  
//        t16.setStroke(Color.YELLOW);
//        count = 0;
//        t16.setText(Constance.NULL);
//        t16.setLayoutX(2.75*OFFSET*HGAP);
//        t16.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        
//        Text t17 = new Text(); 
//        t17.setFont(new Font("Sans Serif", 14));  
//        t17.setStroke(Color.YELLOW);
//        t17.setText(Constance.NULL);
//        t17.setLayoutX(2.75*OFFSET*HGAP);
//        t17.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count++;
//        
//        Text t18 = new Text(); 
//        t18.setFont(new Font("Sans Serif", 14));  
//        t18.setStroke(Color.YELLOW);
//        t18.setText(Constance.NULL);
//        t18.setLayoutX(2.75*OFFSET*HGAP);
//        t18.setLayoutY(TEXT_OFFSET+HGAP*count);
//        count=0;        
//
//        root.getChildren().add(t1);
//        root.getChildren().add(t2);
//        root.getChildren().add(t3);
//        root.getChildren().add(t4);
//        root.getChildren().add(t5);
//        root.getChildren().add(t6);
//        root.getChildren().add(t7);
//        root.getChildren().add(t8);
//        root.getChildren().add(t9);
//        root.getChildren().add(t10);
//        root.getChildren().add(t11);
//        root.getChildren().add(t12);
//        root.getChildren().add(t13);
//        root.getChildren().add(t14);
//        root.getChildren().add(t15);
//        root.getChildren().add(t16);
//        root.getChildren().add(t17);
//        root.getChildren().add(t18);
//        
//        scene.setRoot(root);
//	}
	
}
