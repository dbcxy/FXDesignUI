package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import model.DataObserver;

import org.apache.log4j.Logger;

import application.Master;
import utils.Constance;

public class TaskObserver implements Runnable{

	static final Logger logger = Logger.getLogger(TaskObserver.class);
	
	private IControlManager iCManager;
	
	private Socket mSocket = null;
	private BufferedReader mServerSocketInPacket = null;
	private BufferedWriter mServerSocketOutPacket = null;
	
	private DatagramSocket mDatagramSocket = null;
	private DatagramPacket mDatagramInPacket = null;
	private DatagramPacket mDatagramOutPacket = null;
	
	private String mDataMsg = null;
	private String mSendMsg = null;
	
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
		
		//Init Read Write heads
		initStreamHead();
		
		//Connection Status
		notifyStatus();
		
		// Now loop forever, waiting to receive packets and printing them.
		while (Constance.IS_CONNECTED && isDataReady()) {
					
			//Get Data
			parseData();
					
			//Make Packet Object
			makeData();
			
			//write on to DB for history
			recordData();
			
		}
	}

	private void createSockets() {
    	
		try {
			InetAddress serverAddr = InetAddress.getByName(Constance.SERVER_IP);

			if(Constance.TCPIP) {
				mSocket = new Socket(serverAddr, Constance.PORT_NO);
//				m_ssocket.setSoTimeout(Constance.MY_TIMEOUT);
				logger.info("TCP socket created at IP:"+mSocket.getInetAddress()+" PORT: "+mSocket.getPort());
			} else if (Constance.UDPIP) {				
				mDatagramSocket = new DatagramSocket(Constance.PORT_NO,serverAddr);
				logger.info("UDP socket created at IP:"+mDatagramSocket.getLocalAddress()+" PORT: "+mDatagramSocket.getLocalPort());
			}
	    	
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Socket creation failed",e);
		}
    }
	
	private void initStreamHead() {
    	
		if(Constance.TCPIP) {
			try {
				mServerSocketInPacket = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
				mServerSocketOutPacket = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
				logger.info("TCP socket input/ouput stream init: "+mSocket);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("TCP socket read failed for PORT: "+Constance.PORT_NO,e);
			}
		} else if (Constance.UDPIP) {
			byte[] mUDPSocketbuffer = new byte[Constance.DGRAM_LEN];
			mDatagramInPacket = new DatagramPacket(mUDPSocketbuffer, mUDPSocketbuffer.length);
			mDatagramOutPacket = new DatagramPacket(mUDPSocketbuffer, mUDPSocketbuffer.length);
			logger.info("UDP socket input/output stream init: "+mSocket);
		}		
	}

	private void notifyStatus() {		
		//Connection Status
		if(mSocket.isConnected() && Constance.TCPIP)
			Constance.IS_CONNECTED = true;
		else if(mDatagramSocket.isConnected() && Constance.UDPIP)
			Constance.IS_CONNECTED = true;
	}
	
	private boolean isDataReady() {
		if(Constance.TCPIP) {
			try {
				return (mServerSocketInPacket.readLine()!=null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(Constance.UDPIP) {
			return true;
		}
		return false;
	}
	
	public void closeActiveConnection() {
        if(Constance.TCPIP) {
				try {
					mSocket.close();
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
	
	private void sendData() {
		try {		
			if(Constance.TCPIP) {
				mServerSocketOutPacket.write(mSendMsg);
				logger.info("TCP Server Data Sent: "+mSendMsg);
			} else if(Constance.UDPIP) {
				mDatagramSocket.send(mDatagramOutPacket);
				logger.info("UDP Server Data sent: "+mSendMsg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Parsing Data from Socket failed", e);
		}
	}
	
	private void parseData() {
		
		try {		
			if(Constance.TCPIP) {
				// Wait to receive a socket data
				mDataMsg = mServerSocketInPacket.readLine();
				logger.info("TCP Server Data received: "+mDataMsg);
			} else if(Constance.UDPIP) {
				// Wait to receive a datagram
				mDatagramSocket.receive(mDatagramInPacket);
				// Convert the contents to a string, and display them
				mDataMsg = new String(mDatagramInPacket.getData(), 0, mDatagramInPacket.getLength());
				logger.info("UDP Server Data received: "+mDataMsg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Parsing Data from Socket failed", e);
		}
	}
	
	private void makeData() {
		//separate class
		DataObserver mDataObserver = new DataObserver();
		mDataObserver.analyseData(mDataMsg);
		logger.info("Server Data analyzed");
		iCManager.manageData(mDataObserver);
		
	}
	
	private void recordData() {
		//separate class
		
	}
}
