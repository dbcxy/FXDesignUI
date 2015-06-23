package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import utils.Constance;

public class UDP implements Runnable {

	static final Logger logger = Logger.getLogger(UDP.class);
    private DatagramSocket ds = null;

	@Override
	public void run() {
		
		try {
			ds = new DatagramSocket(AppServer.SERVERPORT);
			logger.info("SERVER UDP socket created");
		} catch (SocketException e1) {
			e1.printStackTrace();
			closeUDP();
		}
//		sendUDPData();
		
	}

	public void sendUDPData() {
		final InputStream is = null; 
//				getAssets().open(FILE_XPLOT);
		
	    final BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
	    
	    try {
	        String str="";
		    String s ="";
		    do{
		    	s = str;
		    	str = bfr.readLine();
		    	ds.send(new DatagramPacket(s.getBytes(), s.length()));
		    	System.out.println("SERVER Tx: "+s);
		    	Thread.sleep(AppServer.DELAY);
		    }while(str != null);
			bfr.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	        closeUDP();
	    }			
	}
	
	public void receiveUDPData() {
		byte[] mUDPSocketbuffer = new byte[Constance.DGRAM_LEN];
		DatagramPacket mDatagramInPacket = new DatagramPacket(mUDPSocketbuffer, mUDPSocketbuffer.length);
		try {
			ds.receive(mDatagramInPacket);
		} catch (IOException e) {
			e.printStackTrace();
			closeUDP();
		}
		String data = new String(mDatagramInPacket.getData(), 0, mDatagramInPacket.getLength());
		System.out.println("SERVER Rx: "+data);
	}

	public void closeUDP() {

	    if (ds != null) {
	        ds.close();
	    }
	}
}