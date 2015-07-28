package network;

import java.io.IOException;

public class C2Server {
	
	public static int PORT_AZ_PLOTS		= 7700;
	public static int PORT_AZ_TRACKS	= 8800;
	public static int PORT_EL_PLOTS		= 5500;
	public static int PORT_EL_TRACKS	= 6600;
	public static int PORT_VIDEO		= 12001;
	
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
	
	public void startTCPThreadServer() {
		try {
			TCPServerThread tcpServerThread = new TCPServerThread(PORT_AZ_PLOTS,PORT_AZ_TRACKS);
			tcpServerThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startUDPThreadServer() {
		try {
			UDPServerThread udpServerThread = new UDPServerThread();
			udpServerThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
