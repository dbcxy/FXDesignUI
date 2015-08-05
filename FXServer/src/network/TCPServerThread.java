package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import messages.radar.AzimuthPlaneDetectionPlotMsg;
import messages.radar.AzimuthPlanePlotsPerCPIMsg;
import messages.radar.AzimuthPlaneTrackMsg;
import messages.radar.ElevationPlaneTrackMsg;

import org.apache.log4j.Logger;

import config.AppConfig;

class TCPServerThread extends Thread {
	
	static final Logger logger = Logger.getLogger(TCPServerThread.class);
	
	private ServerSocket  mSocketAzPlot = null;
	private ServerSocket  mSocketElPlot = null;
	private ServerSocket  mSocketAzTrack = null;
	private ServerSocket  mSocketElTrack = null;
	private ServerSocket  mSocketVideo = null;
	private ServerSocket  mSocketRead = null;
	
	private Socket  socketAzPlot = null;
	private Socket  socketElPlot = null;
	private Socket  socketAzTrack = null;
	private Socket  socketElTrack = null;
	private Socket  socketVideo = null;
	private Socket  socketRead = null;

	boolean pAz = true;
	boolean tAz = true;
	boolean pEl = true;
	boolean tEl = true;
	boolean Vid = true;
    
    boolean EXIT = false;
    int val = 0;
   
	   public TCPServerThread() throws IOException {
		   	mSocketAzPlot = new ServerSocket (C2Server.PORT_AZ_PLOTS);
			mSocketElPlot = new ServerSocket (C2Server.PORT_EL_PLOTS);
			mSocketAzTrack = new ServerSocket (C2Server.PORT_AZ_TRACKS);
			mSocketElTrack = new ServerSocket (C2Server.PORT_EL_TRACKS);
			mSocketVideo = new ServerSocket (C2Server.PORT_VIDEO);
			mSocketRead = new ServerSocket (C2Server.PORT_WRITE);
			AppConfig.getInstance().getController().notifyData("TCP server socketed Created!");
	   }
	
	   @Override
	   public void run() {
	      try {
	    	  	AppConfig.getInstance().getController().notifyData("Waitin for a client TCP connection....");
	            socketAzPlot = mSocketAzPlot.accept();
	            AppConfig.getInstance().getController().notifyData("Just connected to " + socketAzPlot.getRemoteSocketAddress());
		        socketAzTrack = mSocketAzTrack.accept();
		        AppConfig.getInstance().getController().notifyData("Just connected to " + socketAzTrack.getRemoteSocketAddress());
		        socketElPlot = mSocketElPlot.accept();
		        AppConfig.getInstance().getController().notifyData("Just connected to " + socketElPlot.getRemoteSocketAddress());
	            socketElTrack = mSocketElTrack.accept();
	            AppConfig.getInstance().getController().notifyData("Just connected to " + socketElTrack.getRemoteSocketAddress());
		        socketVideo = mSocketVideo.accept();
		        AppConfig.getInstance().getController().notifyData("Just connected to " + socketVideo.getRemoteSocketAddress());
		        socketRead = mSocketRead.accept();
		        AppConfig.getInstance().getController().notifyData("Just connected to " + socketVideo.getRemoteSocketAddress());
		        
		        AppConfig.getInstance().getController().notifyData("Preparing: Sending for data...");

		        try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		        
		        new Thread(new Runnable() {
					
					@Override
					public void run() {
						pAz = true;
				        sendAzPlotData();
				        pAz = false;
					}
				}).start();
		        
		        new Thread(new Runnable() {
					
					@Override
					public void run() {
						tAz = true;
				        sendAzTrackData();	
				        tAz = false;
					}
				}).start();
		        
		        
//			            String elplot = "El PLOT ! ";
//			            DatagramPacket ep = new DatagramPacket(elplot.getBytes() , elplot.getBytes().length,groupAddr,C2Server.PORT_EL_PLOTS);
//			            datagramSocketAzPlots.send(ep);

		        new Thread(new Runnable() {
					
					@Override
					public void run() {
						tEl = true;
				        sendElTrackData();	
				        tEl = false;
					}
				}).start();
		        
//			            String video = "RAW VIDEO ! ";
//			            DatagramPacket vid = new DatagramPacket(video.getBytes() , video.getBytes().length,groupAddr,C2Server.PORT_VIDEO);
//			            datagramSocketAzPlots.send(vid);
		        
		        new Thread(new Runnable() {
					
					@Override
					public void run() {
						while(true) {
							if(!pAz && !tAz && !tEl) {
								AppConfig.getInstance().getController().notifyData("All messages Sent");
						        exitAll();
						        AppConfig.getInstance().getController().notifyData("TCP Sockets Destroyed!"+"\n");
						        AppConfig.getInstance().getController().updateStartButton(0);
						        break;
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
	      } catch(Exception e) {
	    	  e.printStackTrace();
	      }
		        
	}
	   
		
	public void kill() {
		pAz = false;
		tAz = false;
	}
			
	private void exitAll() {
		try {			
			mSocketAzPlot.close();
			mSocketAzTrack.close();
			mSocketElPlot.close();
			mSocketElTrack.close();
			mSocketVideo.close();
			
			socketAzPlot.close();
			socketAzTrack.close();
			socketElPlot.close();
			socketElTrack.close();
			socketVideo.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendAzPlotData() {
		
		int range = C2Server.INIT_RANGE;
		int angle = 0;
		int az = 0;
		
		for ( int i =0 ; i<C2Server.NO_SCANS; i++) {
			 // Read Data and send at each 0.5 second
			AzimuthPlanePlotsPerCPIMsg aPlotsPerCPIMsg = new AzimuthPlanePlotsPerCPIMsg();			
			aPlotsPerCPIMsg.setMessageHeader((int)0x7711);
			aPlotsPerCPIMsg.setPlotCount((short) 500);//25plots
			
			az = (int)(Math.toRadians(angle++)*10000);
			if(angle>20)
				angle = 0;
			
			for (int j=0;j<aPlotsPerCPIMsg.getPlotCount(); j++) {
				range = (int) (range - (C2Server.TARGET_SPEED * C2Server.SCAN_TIME ));
				if(range < 0) 
					range = C2Server.INIT_RANGE;
				
				AzimuthPlaneDetectionPlotMsg aPlotMsg = new AzimuthPlaneDetectionPlotMsg();
				aPlotMsg.setMessageHeader((int) 0x5511);
				aPlotMsg.setRange(range);
				aPlotMsg.setAzimuth(az);
				aPlotMsg.setStrength((int) (0.1234*10000));
				aPlotsPerCPIMsg.addAzimuthPlaneDetectionPlotMsg(aPlotMsg);
			}
			
			//Send plot data MC UDP
			try {
				byte[] plot = aPlotsPerCPIMsg.getByteBuffer();				
				DataOutputStream out = new DataOutputStream(socketAzPlot.getOutputStream());
				out.writeInt(plot.length);
			    out.write(plot);
				AppConfig.getInstance().getController().notifyData("Az Plot Sending... "+plot.length);
				Thread.sleep(500);//1 plot ~ 1ms => 500 plots ~ 500ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendAzTrackData() {
		int range = C2Server.INIT_RANGE;
		int angle = 0;
		double az = Math.toRadians(angle);
		
		for ( int i =0 ; i<C2Server.NO_SCANS; i++) {
			// Read Data and send at each 0.5 second
			AzimuthPlaneTrackMsg aTrackMsg = new AzimuthPlaneTrackMsg();
			range = (int) (range - (C2Server.TARGET_SPEED * C2Server.SCAN_TIME ));
			if(range < 0) 
				range = C2Server.INIT_RANGE;
			az = Math.toRadians(angle++);
			if(angle>20)
				angle = 0;
					
			aTrackMsg.setMessageHeader((int) 0x6611);
			aTrackMsg.setY((int) (range*Math.sin(az)));
			aTrackMsg.setX((int) (range*Math.cos(az)));
			aTrackMsg.setTrackStatus((short) 1);
			aTrackMsg.setTrackName(i);
			aTrackMsg.setTimeStampLow(0);
			aTrackMsg.setTimeStampHigh(0);
			
			//Send plot data MC UDP		
			try {
				byte[] track = aTrackMsg.getByteBuffer().array();
				DataOutputStream out = new DataOutputStream(socketAzTrack.getOutputStream());
				out.writeInt(track.length);
			    out.write(track);
		        AppConfig.getInstance().getController().notifyData("Az Track Sending... "+track.length);
				Thread.sleep(5);//1 Track ~ 5ms => 100Tracks ~500ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendElTrackData() {
		int range = C2Server.INIT_RANGE;
		int angle = 0;
		double el = Math.toRadians(angle);
		
		for ( int i =0 ; i<C2Server.NO_SCANS; i++) {
			// Read Data and send at each 0.5 second
			ElevationPlaneTrackMsg eTrackMsg = new ElevationPlaneTrackMsg();
			range = (int) (range - (C2Server.TARGET_SPEED * C2Server.SCAN_TIME ));
			if(range < 0) 
				range = C2Server.INIT_RANGE;
			el = Math.toRadians(angle++);
			if(angle>20)
				angle = 0;
					
			eTrackMsg.setMessageHeader((int) 0x4411);
			eTrackMsg.setZ((int) (range*Math.sin(el)));
			eTrackMsg.setX((int) (range*Math.cos(el)));
			eTrackMsg.setTrackStatus((short) 1);
			eTrackMsg.setTrackName(i);
			eTrackMsg.setTimeStampLow(0);
			eTrackMsg.setTimeStampHigh(0);
			
			//Send plot data MC UDP		
			try {
				byte[] track = eTrackMsg.getByteBuffer().array();
				DataOutputStream out = new DataOutputStream(socketElTrack.getOutputStream());
				out.writeInt(track.length);
			    out.write(track);
		        AppConfig.getInstance().getController().notifyData("El Track Sending... "+track.length);
				Thread.sleep(5);//1 Track ~ 5ms => 100Tracks ~500ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}