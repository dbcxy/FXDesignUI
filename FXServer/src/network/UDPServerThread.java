package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServerThread extends Thread{
	
	private DatagramSocket datagramSocketAzPlots;
	private DatagramSocket datagramSocketAzTracks;
	private DatagramSocket datagramSocketElPlots;
	private DatagramSocket datagramSocketElTracks;
	private DatagramSocket datagramSocketVideo;
	private DatagramSocket datagramSocketRead;
	
	public UDPServerThread() throws IOException {
		datagramSocketAzPlots = new DatagramSocket();
		datagramSocketAzTracks = new DatagramSocket();
	}
	
	@Override
	public void run() {
		
		try {
			//buffer to receive incoming data
			InetAddress groupAddr = InetAddress.getByName("225.225.0.1");

            String plot = "PLOT ! ";
            DatagramPacket dp = new DatagramPacket(plot.getBytes() , plot.getBytes().length,groupAddr,C2Server.PORT_AZ_PLOTS);
	        
            String track = "TRACK ! ";
            DatagramPacket dt = new DatagramPacket(track.getBytes() , track.getBytes().length,groupAddr,C2Server.PORT_AZ_TRACKS);
	        
            System.out.println("Server socket created. Sending for data...");
	        while(true) {
	            datagramSocketAzPlots.send(dp);	            
	            datagramSocketAzTracks.send(dt);
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
