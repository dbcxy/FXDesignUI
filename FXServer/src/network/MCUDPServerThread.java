package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

import config.AppConfig;
import messages.radar.AzimuthPlaneDetectionPlotMsg;
import messages.radar.AzimuthPlanePlotsPerCPIMsg;
import messages.radar.AzimuthPlaneTrackMsg;
import messages.radar.ElevationPlaneTrackMsg;
import messages.utils.DataManager;

public class MCUDPServerThread extends Thread{
	
	private MulticastSocket datagramSocketAzPlots;
	private MulticastSocket datagramSocketAzTracks;
	private MulticastSocket datagramSocketElPlots;
	private MulticastSocket datagramSocketElTracks;
	private MulticastSocket datagramSocketVideo;
	private MulticastSocket datagramSocketRead;
	InetAddress groupAddr;
	
	boolean pAz = true;
	boolean tAz = true;
	boolean pEl = true;
	boolean tEl = true;
	boolean Vid = true;
	
	DataManager dm = new DataManager();
	
//	x = r .* cos(elevation) .* cos(azimuth)
//	y = r .* cos(elevation) .* sin(azimuth)
//	z = r .* sin(elevation)
	
//	azimuth = atan2(y,x)
//	elevation = atan2(z,sqrt(x.^2 + y.^2))
//	r = sqrt(x.^2 + y.^2 + z.^2)
		
	public MCUDPServerThread() throws IOException {
		
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

        new Thread(new Runnable() {
			
			@Override
			public void run() {
				tEl = true;
		        sendElTrackData();	
		        tEl = false;
			}
		}).start();
        
//	            String video = "RAW VIDEO ! ";
//	            DatagramPacket vid = new DatagramPacket(video.getBytes() , video.getBytes().length,groupAddr,C2Server.PORT_VIDEO);
//	            datagramSocketAzPlots.send(vid);
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					if(!pAz && !tAz && !tEl) {
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
		int angle = 0;
		int az = 0;
		
		for ( int i =0 ; i<C2Server.NO_SCANS; i++) {
			 // Read Data and send at each 0.5 second
			AzimuthPlanePlotsPerCPIMsg aPlotsPerCPIMsg = new AzimuthPlanePlotsPerCPIMsg();			
			aPlotsPerCPIMsg.setMessageHeader((int)0x7711);
			aPlotsPerCPIMsg.setPlotCount((short) 25);//25plots
			
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
	            DatagramPacket dp = new DatagramPacket(plot , plot.length,groupAddr,C2Server.PORT_AZ_PLOTS);
	            if(datagramSocketAzPlots.isClosed())
	            	break;
				datagramSocketAzPlots.send(dp);
				AppConfig.getInstance().getController().notifyData("Az Plot Sending... "+plot.length);
				Thread.sleep(25);//1 plot ~ 1ms => 500 plots ~ 500ms
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
		        DatagramPacket dt = new DatagramPacket(track , track.length,groupAddr,C2Server.PORT_AZ_TRACKS);
		        if(datagramSocketAzTracks.isClosed())
		        	break;
		        datagramSocketAzTracks.send(dt);
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
		        DatagramPacket dt = new DatagramPacket(track , track.length,groupAddr,C2Server.PORT_EL_TRACKS);
		        if(datagramSocketElTracks.isClosed())
		        	break;
		        datagramSocketElTracks.send(dt);
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
