package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MCUDPServerThread extends Thread{
	
	private MulticastSocket datagramSocketAzPlots;
	private MulticastSocket datagramSocketAzTracks;
	private MulticastSocket datagramSocketElPlots;
	private MulticastSocket datagramSocketElTracks;
	private MulticastSocket datagramSocketVideo;
	private MulticastSocket datagramSocketRead;
	
	int count = 10;
	
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
	}
	
	@Override
	public void run() {
		
		try {
			//buffer to receive incoming data
			InetAddress groupAddr = InetAddress.getByName("225.225.0.1");
			datagramSocketAzPlots.joinGroup(groupAddr);
			datagramSocketAzTracks.joinGroup(groupAddr);
			datagramSocketElPlots.joinGroup(groupAddr);
			datagramSocketElTracks.joinGroup(groupAddr);
			datagramSocketVideo.joinGroup(groupAddr);
	        
            System.out.println("Server socket created. Sending for data...");
	        while(count>0) {
	        	String plot = "Az PLOT ! ";
	            DatagramPacket dp = new DatagramPacket(plot.getBytes() , plot.getBytes().length,groupAddr,C2Server.PORT_AZ_PLOTS);
	            datagramSocketAzPlots.send(dp);
		        
	            String track = "Az TRACK ! ";
	            DatagramPacket dt = new DatagramPacket(track.getBytes() , track.getBytes().length,groupAddr,C2Server.PORT_AZ_TRACKS);
	            datagramSocketAzTracks.send(dt);
	            
	            String elplot = "El PLOT ! ";
	            DatagramPacket ep = new DatagramPacket(elplot.getBytes() , elplot.getBytes().length,groupAddr,C2Server.PORT_EL_PLOTS);
	            datagramSocketAzPlots.send(ep);
		        
	            String eltrack = "El TRACK ! ";
	            DatagramPacket et = new DatagramPacket(eltrack.getBytes() , eltrack.getBytes().length,groupAddr,C2Server.PORT_EL_TRACKS);
	            datagramSocketAzTracks.send(et);
	            
	            String video = "RAW VIDEO ! ";
	            DatagramPacket vid = new DatagramPacket(video.getBytes() , video.getBytes().length,groupAddr,C2Server.PORT_VIDEO);
	            datagramSocketAzPlots.send(vid);
	            
	            count--;
	            System.out.println("Sent");
	            
	            try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	    } catch(IOException e) {
	    	System.err.println("IOException " + e);
        }
	}

}
