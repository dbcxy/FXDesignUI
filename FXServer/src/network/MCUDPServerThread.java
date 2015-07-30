package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import messages.radar.AzimuthPlaneDetectionPlotMsg;
import messages.radar.AzimuthPlanePlotsPerCPIMsg;
import messages.radar.AzimuthPlaneTrackMsg;
import messages.utils.ObjectSizeFetcher;
import messages.utils.Serializer;

public class MCUDPServerThread extends Thread{
	
	static final int targetSpeed = 50;//mps
	static final int initRange = 4000;//4km
	static final double initAz = -0.174532925;//10degrees in rad
	static final double scanTime = 0.5;//sec
	static final int noOfScans = 10;
	
//	private DatagramSocket dSocketAzPlots;
//	private DatagramSocket dSocketAzTracks;
	
	private MulticastSocket datagramSocketAzPlots;
	private MulticastSocket datagramSocketAzTracks;
	private MulticastSocket datagramSocketElPlots;
	private MulticastSocket datagramSocketElTracks;
	private MulticastSocket datagramSocketVideo;
	private MulticastSocket datagramSocketRead;
	InetAddress groupAddr;
	
	int count = 10;
	
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
	    } catch(IOException e) {
	    	System.err.println("IOException " + e);
        }
	        
        System.out.println("Server socket created. Sending for data...");

        try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        sendAzPlotData();
        sendAzTrackData();
        
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
	    
        System.out.println("Sent");
        exitAll();

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
			
		int range = initRange;
		double az = initAz;
		
		for ( int i =0 ; i<noOfScans; i++) {

			 // Read Data and send at each 0.5 second
			AzimuthPlanePlotsPerCPIMsg aPlotsPerCPIMsg = new AzimuthPlanePlotsPerCPIMsg();
			range = (int) (range - (targetSpeed * scanTime ));
			
			aPlotsPerCPIMsg.setMessageClass((short) 0x77);
			aPlotsPerCPIMsg.setMessageId((short) 0x11);
			aPlotsPerCPIMsg.setPlotCount((short) 1);			
			for (int j=0;j<aPlotsPerCPIMsg.getPlotCount(); j++) {
				AzimuthPlaneDetectionPlotMsg aPlotMsg = new AzimuthPlaneDetectionPlotMsg();
				aPlotMsg.setMessageClass((short) 0x55);
				aPlotMsg.setMessageId((short) 0x11);
				aPlotMsg.setRange(range);
				
				// scale the azimuth so that it flaot can be converted to Integer.later during display scale it down by 1000;
				aPlotMsg.setAzimuth((int) (az*10000));
				// scale the strength that float can be converted to Integer.later during display scale it down by 1000;
				aPlotMsg.setStrength((int) (0.1234*10000));
				aPlotsPerCPIMsg.addAzimuthPlaneDetectionPlotMsg(aPlotMsg);				
			}
//			System.out.println("Range: "+aPlotsPerCPIMsg.getAzimuthPlaneDetectionPlotMsg().getRange());
//			System.out.println("Az: "+aPlotsPerCPIMsg.getAzimuthPlaneDetectionPlotMsg().getAzimuth());
			
			//Send plot data MC UDP
			try {
				byte[] plot = Serializer.serialize(aPlotsPerCPIMsg);
				System.out.println("Size: "+plot.length);
	            DatagramPacket dp = new DatagramPacket(plot , plot.length,groupAddr,C2Server.PORT_AZ_PLOTS);			
				datagramSocketAzPlots.send(dp);
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
		int range = initRange;
		double az = initAz;
		
		// Read Data and send at each 0.5 second
		AzimuthPlaneTrackMsg aTrackMsg = new AzimuthPlaneTrackMsg();
		range = (int) (range - (targetSpeed * scanTime ));
				
		aTrackMsg.setMessageClass((short) 0x66);
		aTrackMsg.setMessageId((short) 0x11);
		aTrackMsg.setY((int) (range*Math.sin(az)));
		aTrackMsg.setX((int) (range*Math.cos(az)));
		aTrackMsg.setTrackStatus((short) 1);
		aTrackMsg.setTimeStampLow(0);
		aTrackMsg.setTimeStampHigh(0);
		
		System.out.println("X: "+aTrackMsg.getX());
		System.out.println("Y: "+aTrackMsg.getY());
		
		//Send plot data MC UDP		
		try {
			byte[] track = Serializer.serialize(aTrackMsg);
			System.out.println("Size: "+track.length);
	        DatagramPacket dt = new DatagramPacket(track , track.length,groupAddr,C2Server.PORT_AZ_TRACKS);
	        datagramSocketAzTracks.send(dt);
//	        dSocketAzTracks.send(dt);
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
