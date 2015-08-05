package utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Test {
	
	public void runTest() {

        //TESTING
//        long startTime = System.currentTimeMillis();
//        //drawing all pixel with Rect size 1x1
//        Test.gcDrawEveryPixelRect(cBtmL1);//470ms-528ms 
//        Test.g2dImgDrawEveryPixelRect(cBtmL1);//about 201ms 
//        //drawing/filling single Rect size 100x100
//        Test.gcDrawSingleRect(cBtmL1);//about 5ms 
//        Test.g2dImgDrawSingleRect(cBtmL1);//about 51ms 
//        long endTime = System.currentTimeMillis();
//        System.out.println("TotalTime: "+(endTime - startTime));
	}
	
	
	public static void gcDrawSingleRect(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();

		 //single Rect
		gc.setFill(Color.RED);
		gc.strokeRect(100, 100, 100, 100);
	}
	
	public static void gcDrawEveryPixelRect(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		//Every pixel
		Random r = new Random();
		for(int i = 0; i<=canvas.getWidth(); i++) {
            for(int j = 0; j<=canvas.getHeight(); j++) {
                if (r.nextBoolean()) {
                    gc.setFill(Color.RED);
                } else {
                    gc.setFill(Color.GREEN);
                }
                gc.fillRect(i, j, 1, 1);
            }
        }
	}
	
	public static void g2dImgDrawSingleRect(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.save();
		gc.drawImage(drawPixel(canvas,true),0,0);
		gc.restore();
	}
	
	public static void g2dImgDrawEveryPixelRect(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.save();
		gc.drawImage(drawPixel(canvas,false),0,0);
		gc.restore();
	}
	
	private static Image drawPixel (Canvas canvas, boolean isSingle) { 
		 BufferedImage bufferedImage = new BufferedImage((int) (canvas.getWidth()), 
				 (int) canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
		 Graphics2D g2d = bufferedImage.createGraphics();

		 if(isSingle) {
			 //single Rect
			 g2d.setColor(java.awt.Color.red);
			 g2d.drawRect(100, 100, 100, 100);
		 } else {
			//Every pixel
			 Random r = new Random();		 
			 for(int i = 0; i<=bufferedImage.getWidth(); i++) {
		            for(int j = 0; j<=bufferedImage.getHeight(); j++) {
		                if (r.nextBoolean())
		                	g2d.setColor(java.awt.Color.red);
		                else
		                	g2d.setColor(java.awt.Color.green);
		                g2d.drawRect(i, j, 1, 1);
		            }
			 }
		 }
		 
		 WritableImage wr = null;
		 Image img = SwingFXUtils.toFXImage(bufferedImage, wr);     
		 return img;
 
		 // start working block         
//		 WritableImage wr = null;         
//		 if (bufferedImage != null) {
//			 wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight()); 
//			 PixelWriter pw = wr.getPixelWriter();             
//			 for (int x = 0; x < bufferedImage.getWidth(); x++) { 
//				 for (int y = 0; y < bufferedImage.getHeight(); y++) {
//					 if (r.nextBoolean()) {
//						 pw.setColor(x,y, Color.RED);
//		                } else {
//		                	pw.setColor(x,y, Color.BLUE);
//		                }
//					                 
//					 }             
//				 }        
//			 }  
//		 return (Image)wr;
	 }

}
