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
//				        
//			            String eltrack = "El TRACK ! ";
//			            DatagramPacket et = new DatagramPacket(eltrack.getBytes() , eltrack.getBytes().length,groupAddr,C2Server.PORT_EL_TRACKS);
//			            datagramSocketAzTracks.send(et);
//			            
//			            String video = "RAW VIDEO ! ";
//			            DatagramPacket vid = new DatagramPacket(video.getBytes() , video.getBytes().length,groupAddr,C2Server.PORT_VIDEO);
//			            datagramSocketAzPlots.send(vid);
		        
		        new Thread(new Runnable() {
					
					@Override
					public void run() {
						while(true) {
							if(!pAz && !tAz) {
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
		double az = Math.toRadians(C2Server.INIT_AZ)*10000;
		
		for ( int i =0 ; i<C2Server.NO_SCANS; i++) {
			 // Read Data and send at each 0.5 second
			AzimuthPlanePlotsPerCPIMsg aPlotsPerCPIMsg = new AzimuthPlanePlotsPerCPIMsg();
			range = (int) (range - (C2Server.TARGET_SPEED * C2Server.SCAN_TIME ));
			
			aPlotsPerCPIMsg.setMessageClass((short)0x77);
			aPlotsPerCPIMsg.setMessageId((short)0x11);
			aPlotsPerCPIMsg.setPlotCount((short) 1);			
			for (int j=0;j<aPlotsPerCPIMsg.getPlotCount(); j++) {
				AzimuthPlaneDetectionPlotMsg aPlotMsg = new AzimuthPlaneDetectionPlotMsg();
				aPlotMsg.setMessageClass((short) 0x55);
				aPlotMsg.setMessageId((short)0x11);
				aPlotMsg.setRange(range);
				
				// scale the azimuth so that it flaot can be converted to Integer.later during display scale it down by 1000;
				aPlotMsg.setAzimuth((int) (az));
				// scale the strength that float can be converted to Integer.later during display scale it down by 1000;
				aPlotMsg.setStrength((int) (0.1234*10000));
				aPlotsPerCPIMsg.addAzimuthPlaneDetectionPlotMsg(aPlotMsg);				
			}
			
			//Send plot data MC UDP
			try {
				byte[] plot = aPlotsPerCPIMsg.getByteBuffer();
				DataOutputStream out = new DataOutputStream(socketAzPlot.getOutputStream());
				out.writeInt(plot.length);
			    out.write(plot);
				AppConfig.getInstance().getController().notifyData("Plot Sending... "+plot.length);
//				dSocketAzPlots.send(dp);
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendAzTrackData() {
		int range = C2Server.INIT_RANGE;
		double az = Math.toRadians(C2Server.INIT_AZ);
		
		for ( int i =0 ; i<C2Server.NO_SCANS; i++) {
			// Read Data and send at each 0.5 second
			AzimuthPlaneTrackMsg aTrackMsg = new AzimuthPlaneTrackMsg();
			range = (int) (range - (C2Server.TARGET_SPEED * C2Server.SCAN_TIME ));
					
			aTrackMsg.setMessageClass((short) 0x66);
			aTrackMsg.setMessageId((short) 0x11);
			aTrackMsg.setY((int) (range*Math.sin(az)));
			aTrackMsg.setX((int) (range*Math.cos(az)));
			aTrackMsg.setTrackStatus((short) 1);
			aTrackMsg.setTrackName((short) 3);
			aTrackMsg.setTimeStampLow(0);
			aTrackMsg.setTimeStampHigh(0);
			
			//Send plot data MC UDP		
			try {
				byte[] track = aTrackMsg.getByteBuffer().array();
				DataOutputStream out = new DataOutputStream(socketAzPlot.getOutputStream());
				out.writeInt(track.length);
			    out.write(track);
		        AppConfig.getInstance().getController().notifyData("Track Sending... "+track.length);
//		        dSocketAzTracks.send(dt);
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}