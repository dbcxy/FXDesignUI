package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import application.Master;
import utils.Constance;

public class TaskObserver implements Runnable{

	static final Logger logger = Logger.getLogger(TaskObserver.class);
	
	private IControlManager iCManager;
	
	private Socket mSocket = null;
	private ServerSocket mServerSocket = null;
	private BufferedReader mServerSocketInPacket = null;
	
	private DatagramSocket mDatagramSocket = null;
	private DatagramPacket mDatagramInPacket = null;
	
	private String mDataMsg = null;
	
	public TaskObserver(IControlManager iControlManager) {
		this.iCManager = iControlManager;
	}
	
	@Override
	public void run() {
		
		runNetworkTask();
	}
	
	private void runNetworkTask() {
		
		//create sockets 
		createSockets();
		
		//Wait for read
		waitToRead();
		
		//Connection Status
		notifyStatus();
		
		// Now loop forever, waiting to receive packets and printing them.
		while (Constance.IS_CONNECTED) {
					
			//Get Data
			parseData();
					
			//Make Packet Object
			makeData();
			
			//write on to DB for history
			recordData();
			
		}
	}

	private void createSockets() {
    	
    	if(Constance.TCPIP) {
			//Creation of TCPIP Sockets
			try {
				mServerSocket= new ServerSocket(Constance.PORT_NO);
//				m_ssocket.setSoTimeout(Constance.MY_TIMEOUT);

			} catch (IOException e) {
				e.printStackTrace();
				logger.error("TCP socket creation failed for PORT: "+Constance.PORT_NO,e);
			}
		}
		else if (Constance.UDPIP) {				
			//Creation of UDP Sockets
			try {
				mDatagramSocket = new DatagramSocket(Constance.PORT_NO);
			} catch (SocketException e) {
				e.printStackTrace();
				logger.error("UDP socket creation failed for PORT: "+Constance.PORT_NO,e);
			}
		}
    }
	
	private void waitToRead() {
    	
		//Wait for read
		if(Constance.TCPIP) {
			try {
				mSocket = mServerSocket.accept();
				logger.info("Accepted: "+mSocket);
				mServerSocketInPacket = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("TCP socket read failed for PORT: "+Constance.PORT_NO,e);
			}
		} else {
			// Create a buffer to read datagrams into.
			byte[] mUDPSocketbuffer = new byte[Constance.DGRAM_LEN];
			// Create a packet to receive data into the buffer
			mDatagramInPacket = new DatagramPacket(mUDPSocketbuffer, mUDPSocketbuffer.length);
		}		
	}

	private void notifyStatus() {		
		//Connection Status
		if(mSocket.isConnected() && Constance.TCPIP)
			Constance.IS_CONNECTED = true;
		else if(mDatagramSocket.isConnected() && Constance.UDPIP)
			Constance.IS_CONNECTED = true;
	}
	
	public void closeActiveConnection() {
        if(Constance.TCPIP) {
				try {
					mServerSocket.close();
					logger.info("Network Task TCP socket Closed/Ended");
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("TCP socket close failed",e);
				}
		} else if(Constance.UDPIP) {
				try {
					mDatagramSocket.close();
					logger.info("Network Task UDP socket Closed/Ended");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("UDP socket close failed",e);
				}
		}
	}
	
	private void parseData() {
		
		try {		
			if(Constance.TCPIP) {
				// Wait to receive a socket data
				mDataMsg = mServerSocketInPacket.readLine();
			} else if(Constance.UDPIP) {
				// Wait to receive a datagram
				mDatagramSocket.receive(mDatagramInPacket);
				
				// Convert the contents to a string, and display them
				mDataMsg = new String(mDatagramInPacket.getData(), 0, mDatagramInPacket.getLength());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Parsing Data from Socket failed", e);
		}
	}
	
	private void makeData() {
		
		
	}
	
	private void recordData() {
		
		
	}
}
