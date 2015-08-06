package network;

import java.io.IOException;
import java.util.Random;

public class C2Server {
	
	public static int PORT_AZ_PLOTS		= 7700;
	public static int PORT_AZ_TRACKS	= 8800;
	public static int PORT_EL_PLOTS		= 5500;
	public static int PORT_EL_TRACKS	= 6600;
	public static int PORT_VIDEO		= 12001;
	public static int PORT_WRITE		= 13001;
	
	public static int TARGET_SPEED 		= 50;//mps
	public static int INIT_RANGE 		= 40000;//4km
	public static double INIT_AZ 		= 10;//10degrees in rad
	public static double SCAN_TIME 		= 0.5;//sec
	public static int NO_SCANS	 		= 10000;
	
	public interface Notifier {
		public void notifyData(String str);
		public void updateStartButton(int i);
	}
	
	TCPServerThread tcpServerThread;
	MCUDPServerThread udpServerThread;
	
	public C2Server() {
		
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public void startMCUDPThreadServer() {
		try {
			udpServerThread = new MCUDPServerThread();
			udpServerThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void stopMCUDPThreadServer() {
		udpServerThread.kill();
	}
	
	public void startTCPThreadServer() {
		try {
			tcpServerThread = new TCPServerThread();
			tcpServerThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void stopTCPThreadServer() {
		tcpServerThread.kill();
	}
}
