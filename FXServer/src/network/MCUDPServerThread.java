package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

import config.AppConfig;
import network.C2Server.Notifier;
import messages.radar.AzimuthPlaneDetectionPlotMsg;
import messages.radar.AzimuthPlanePlotsPerCPIMsg;
import messages.radar.AzimuthPlaneTrackMsg;
import messages.utils.DataIdentifier;
import messages.utils.DataManager;
import messages.utils.ObjectSizeFetcher;
import messages.utils.Serializer;

public class MCUDPServerThread extends Thread{
	
//	private DatagramSocket dSocketAzPlots;
//	private DatagramSocket dSocketAzTracks;
	
	private MulticastSocket datagramSocketAzPlots;
	private MulticastSocket datagramSocketAzTracks;
	private MulticastSocket datagramSocketElPlots;
	private MulticastSocket datagramSocketElTracks;
	private MulticastSocket datagramSocketVideo;
	private MulticastSocket datagramSocketRead;
	InetAddress groupAddr;
	
	boolean pAz = true;
	boolean tAz = true;
	
	DataManager dm = new DataManager();
		
	public MCUDPServerThread() throws IOException {
//		dSocketAzPlots = new DatagramSocket();
//		dSocketAzTracks = new DatagramSocket();
		
		datagramSocketAzPlots = new MulticastSocket();
		datagramSocketAzTracks = new MulticastSocket();
		datagramSocketElPlots = new MulticastSocket();
		datagramSocketElTracks = new MulticastSocket();
		datagramSocketVideo = new MulticastSocket();
		
		datagramSocketAzPlots.setInterface(InetAddress.getLocalHost());
		datagramSocketAzTracks.setInterface(InetAddress.getLocalHost());
		datagramSocketElPlots.setInterface(InetAddress.getLocalHost());
		datagramSocketElTracks.setInterface(InetAddress.getLocalHost());
		datagramSocketVideo.setInterface(InetAddress.getLocalHost());
		
		AppConfig.getInstance().getController().notifyData("Multicast Sockets Created!");
	}
	
	@Override
	public void run() {
		
		try {
			//buffer to receive incoming data
			groupAddr = InetAddress.getByName("225.225.0.1");
			datagramSocketAzPlots.joinGroup(groupAddr);
			datagramSocketAzTracks.joinGroup(groupAddr);
			datagramSocketElPlots.joinGroup(groupAddr);
			datagramSocketElTracks.joinGroup(groupAddr);
			datagramSocketVideo.joinGroup(groupAddr);
			
			AppConfig.getInstance().getController().notifyData("Joined Group IP: "+groupAddr.getHostAddress());
	    } catch(IOException e) {
	    	System.err.println("IOException " + e);
        }
	        
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
        
        
//	            String elplot = "El PLOT ! ";
//	            DatagramPacket ep = new DatagramPacket(elplot.getBytes() , elplot.getBytes().length,groupAddr,C2Server.PORT_EL_PLOTS);
//	            datagramSocketAzPlots.send(ep);
//		        
//	            String eltrack = "El TRACK ! ";
//	            DatagramPacket et = new DatagramPacket(eltrack.getBytes() , eltrack.getBytes().length,groupAddr,C2Server.PORT_EL_TRACKS);
//	            datagramSocketAzTracks.send(et);
//	            
//	            String video = "RAW VIDEO ! ";
//	            DatagramPacket vid = new DatagramPacket(video.getBytes() , video.getBytes().length,groupAddr,C2Server.PORT_VIDEO);
//	            datagramSocketAzPlots.send(vid);
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					if(!pAz && !tAz) {
						AppConfig.getInstance().getController().notifyData("All messages Sent");
				        exitAll();
				        AppConfig.getInstance().getController().notifyData("Multicast Sockets Destroyed!"+"\n");
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
        
	}
	
	public void kill() {
		pAz = false;
		tAz = false;
	}
	
	private void exitAll() {
		try {
			datagramSocketAzPlots.leaveGroup(groupAddr);
			datagramSocketAzTracks.leaveGroup(groupAddr);
			datagramSocketElPlots.leaveGroup(groupAddr);
			datagramSocketElTracks.leaveGroup(groupAddr);
			datagramSocketVideo.leaveGroup(groupAddr);
			
			datagramSocketAzPlots.close();
			datagramSocketAzTracks.close();
			datagramSocketElPlots.close();
			datagramSocketElTracks.close();
			datagramSocketVideo.close();
			
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
//			System.out.println("Range: "+aPlotsPerCPIMsg.getAzimuthPlaneDetectionPlotMsg().getRange());
//			System.out.println("Az: "+aPlotsPerCPIMsg.getAzimuthPlaneDetectionPlotMsg().getAzimuth());
			
			//Send plot data MC UDP
			try {
				byte[] plot = aPlotsPerCPIMsg.getByteBuffer();
	            DatagramPacket dp = new DatagramPacket(plot , plot.length,groupAddr,C2Server.PORT_AZ_PLOTS);			
				datagramSocketAzPlots.send(dp);
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
		        DatagramPacket dt = new DatagramPacket(track , track.length,groupAddr,C2Server.PORT_AZ_TRACKS);
		        datagramSocketAzTracks.send(dt);
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
