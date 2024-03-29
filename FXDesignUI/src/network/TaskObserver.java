package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import messages.radar.AzimuthPlanePlotsPerCPIMsg;
import messages.radar.AzimuthPlaneTrackMsg;
import messages.radar.ElevationPlanePlotsPerCPIMsg;
import messages.radar.ElevationPlaneTrackMsg;
import messages.radar.PlaneRAWVideoMsg;
import messages.utils.DataIdentifier;
import messages.utils.DataManager;
import messages.utils.IByteSum;
import model.DataObserver;

import org.apache.log4j.Logger;

import utils.Constance;

public class TaskObserver extends Thread implements IByteSum{

	static final Logger logger = Logger.getLogger(TaskObserver.class);
	
	private IControlManager iCManager;
	
	private Socket mSocketAzPlot = null;
	private Socket mSocketElPlot = null;
	private Socket mSocketAzTrack = null;
	private Socket mSocketElTrack = null;
	private Socket mSocketVideo = null;
	private Socket mSocketWrite = null;
	
	private DatagramSocket mDatagramSocketAzPlot = null;
	private DatagramSocket mDatagramSocketElPlot = null;
	private DatagramSocket mDatagramSocketAzTrack = null;
	private DatagramSocket mDatagramSocketElTrack = null;
	private DatagramSocket mDatagramSocketVideo = null;
	private DatagramSocket mDatagramSocketWrite = null;
	
	private InetAddress groupAddr;
	private MulticastSocket mMCSocketAzPlot = null;
	private MulticastSocket mMCSocketElPlot = null;
	private MulticastSocket mMCSocketAzTrack = null;
	private MulticastSocket mMCSocketElTrack = null;
	private MulticastSocket mMCSocketVideo = null;
	private MulticastSocket mMCSocketWrite = null;
	
	List<Thread> TaskManager = new ArrayList<Thread>();
	DataObserver mDataObserver;
	DataManager mDataManager;
			
	public TaskObserver(IControlManager iControlManager) {
		this.iCManager = iControlManager;
		mDataObserver = new DataObserver();
		mDataManager = new DataManager();
	}
	
	@Override
	public void run() {		
		runNetworkTask();
		logger.info("Network Task Launched");
	}
	
	private void runNetworkTask() {
		
		//create sockets 
		createSockets();
		Constance.IS_CONNECTED = true;
		
		//Creating Individual Threads for simultaneous read of data
		addThreadReadAzPlots();
		addThreadReadElPlots();
		addThreadReadAzTracks();
		addThreadReadElTracks();
		addThreadVideo();
		
		//Starting all threads
		logger.info("Starting All Threads");
		for(Thread thread: TaskManager)
			thread.start();

	}

	private void addThreadReadAzPlots() {
		// Now loop forever, waiting to receive packets and printing them.	
		Thread AzPlot = new Thread(new Runnable() {
			
			@Override
			public void run() {
				logger.info("Waiting to Read Thread Az Plots Looper");	
				while (Constance.IS_CONNECTED) {
					//Get Data
					byte[] mData = null;
					try {
						if(Constance.TCPIP)
							mData = parseTCPData(mSocketAzPlot);
						else if(Constance.UDPIP)
							mData = parseUDPData(mDatagramSocketAzPlot);
						else if(Constance.MCUDP)
							mData = parseMCUDPData(mMCSocketAzPlot);
					} catch (IOException e) {
						Constance.IS_CONNECTED = false;
						e.printStackTrace();
						break;
					}
							
					//Make Packet Object
					makeData(mData);
					
					//write on to DB for history
					recordData(mData);
					
				}
				logger.info("Ending Network Read Thread Az Plots Looper");				
			}
		});
		TaskManager.add(AzPlot);
	}

	private void addThreadReadElPlots() {
		// Now loop forever, waiting to receive packets and printing them.	
		Thread ElPlot = new Thread(new Runnable() {
			
			@Override
			public void run() {
				logger.info("Waiting to Read Thread El Plots Looper");	
				while (Constance.IS_CONNECTED) {
					//Get Data
					byte[] mData = null;
					try {
						if(Constance.TCPIP)
							mData = parseTCPData(mSocketElPlot);
						else if(Constance.UDPIP)
							mData = parseUDPData(mDatagramSocketElPlot);
						else if(Constance.MCUDP)
							mData = parseMCUDPData(mMCSocketElPlot);
					} catch (IOException e) {
						Constance.IS_CONNECTED = false;
						e.printStackTrace();
						break;
					}
							
					//Make Packet Object
					makeData(mData);
					
					//write on to DB for history
					recordData(mData);
					
				}
				logger.info("Ending Network Read Thread El Plots Looper");				
			}
		});
		TaskManager.add(ElPlot);		
	}

	private void addThreadReadAzTracks() {
		// Now loop forever, waiting to receive packets and printing them.	
		Thread AzTrack = new Thread(new Runnable() {
			
			@Override
			public void run() {
				logger.info("Waiting to Read Thread Az Tracks Looper");	
				while (Constance.IS_CONNECTED) {
					//Get Data
					byte[] mData = null;
					try {
						if(Constance.TCPIP)
							mData = parseTCPData(mSocketAzTrack);
						else if(Constance.UDPIP)
							mData = parseUDPData(mDatagramSocketAzTrack);
						else if(Constance.MCUDP)
							mData = parseMCUDPData(mMCSocketAzTrack);
					} catch (IOException e) {
						Constance.IS_CONNECTED = false;
						e.printStackTrace();
						break;
					}
							
					//Make Packet Object
					makeData(mData);
					
					//write on to DB for history
					recordData(mData);
					
				}
				logger.info("Ending Network Read Thread Az Tracks Looper");				
			}
		});
		TaskManager.add(AzTrack);		
	}

	private void addThreadReadElTracks() {
		// Now loop forever, waiting to receive packets and printing them.	
		Thread ElTrack = new Thread(new Runnable() {
			
			@Override
			public void run() {
				logger.info("Waiting to Read Thread El Tracks Looper");	
				while (Constance.IS_CONNECTED) {
					//Get Data
					byte[] mData = null;
					try {
						if(Constance.TCPIP)
							mData = parseTCPData(mSocketElTrack);
						else if(Constance.UDPIP)
							mData = parseUDPData(mDatagramSocketElTrack);
						else if(Constance.MCUDP)
							mData = parseMCUDPData(mMCSocketElTrack);
					} catch (IOException e) {
						Constance.IS_CONNECTED = false;
						e.printStackTrace();
						break;
					}
							
					//Make Packet Object
					makeData(mData);
					
					//write on to DB for history
					recordData(mData);
					
				}
				logger.info("Ending Network Read Thread El Tracks Looper");				
			}
		});
		TaskManager.add(ElTrack);		
	}

	private void addThreadVideo() {
		// Now loop forever, waiting to receive packets and printing them.
		Thread Video = new Thread(new Runnable() {
			
			@Override
			public void run() {
				logger.info("Waiting to Read Thread Video Looper");
				while (Constance.IS_CONNECTED) {
					//Get Data
					byte[] mData = null;
					try {
						if(Constance.TCPIP)
							mData = parseTCPData(mSocketVideo);
						else if(Constance.UDPIP)
							mData = parseUDPData(mDatagramSocketVideo);
						else if(Constance.MCUDP)
							mData = parseMCUDPData(mMCSocketVideo);
					} catch (IOException e) {
						Constance.IS_CONNECTED = false;
						e.printStackTrace();
						break;
					}
							
					//Make Packet Object
					makeVideoData(mData);
					
					//write on to DB for history
					recordData(mData);
					
				}
				logger.info("Ending Network Read Thread Video Looper");				
			}
		});
		TaskManager.add(Video);		
	}

	private void createSockets() {
    	
		try {
			if(Constance.TCPIP) {
				InetAddress serverAddr = InetAddress.getByName(Constance.SERVER_IP);
				mSocketAzPlot = new Socket(serverAddr, Constance.PORT_AZ_PLOTS);
				mSocketElPlot = new Socket(serverAddr, Constance.PORT_EL_PLOTS);
				mSocketAzTrack = new Socket(serverAddr, Constance.PORT_AZ_TRACKS);
				mSocketElTrack = new Socket(serverAddr, Constance.PORT_EL_TRACKS);
				mSocketVideo = new Socket(serverAddr, Constance.PORT_VIDEO);
				mSocketWrite = new Socket(serverAddr, Constance.PORT_WRITE);
//				m_ssocket.setSoTimeout(Constance.MY_TIMEOUT);
				
				logger.info("TCP (mSocketAzPlot)socket created at IP:"+mSocketAzPlot.getInetAddress()+" PORT: "+mSocketAzPlot.getPort());
				logger.info("TCP (mSocketElPlot)socket created at IP:"+mSocketElPlot.getInetAddress()+" PORT: "+mSocketElPlot.getPort());
				logger.info("TCP (mSocketAzTrack)socket created at IP:"+mSocketAzTrack.getInetAddress()+" PORT: "+mSocketAzTrack.getPort());
				logger.info("TCP (mSocketElTrack)socket created at IP:"+mSocketElTrack.getInetAddress()+" PORT: "+mSocketElTrack.getPort());
				logger.info("TCP (mSocketVideo)socket created at IP:"+mSocketVideo.getInetAddress()+" PORT: "+mSocketVideo.getPort());
				logger.info("TCP (mSocketWrite)socket created at IP:"+mSocketWrite.getInetAddress()+" PORT: "+mSocketWrite.getPort());
			} else if (Constance.UDPIP) {				
				mDatagramSocketAzPlot = new DatagramSocket(Constance.PORT_AZ_PLOTS);
				mDatagramSocketElPlot = new DatagramSocket(Constance.PORT_EL_PLOTS);
				mDatagramSocketAzTrack = new DatagramSocket(Constance.PORT_AZ_TRACKS);
				mDatagramSocketElTrack = new DatagramSocket(Constance.PORT_EL_TRACKS);
				mDatagramSocketVideo = new DatagramSocket(Constance.PORT_VIDEO);
				mDatagramSocketWrite = new DatagramSocket(Constance.PORT_WRITE);
				
				logger.info("UDP (mDatagramSocketAzPlot)socket created at IP:"+mDatagramSocketAzPlot.getLocalAddress()+" PORT: "+mDatagramSocketAzPlot.getLocalPort());
				logger.info("UDP (mDatagramSocketElPlot)socket created at IP:"+mDatagramSocketElPlot.getLocalAddress()+" PORT: "+mDatagramSocketElPlot.getLocalPort());
				logger.info("UDP (mDatagramSocketAzTrack)socket created at IP:"+mDatagramSocketAzTrack.getLocalAddress()+" PORT: "+mDatagramSocketAzTrack.getLocalPort());
				logger.info("UDP (mDatagramSocketElTrack)socket created at IP:"+mDatagramSocketElTrack.getLocalAddress()+" PORT: "+mDatagramSocketElTrack.getLocalPort());
				logger.info("UDP (mDatagramSocketVideo)socket created at IP:"+mDatagramSocketVideo.getLocalAddress()+" PORT: "+mDatagramSocketVideo.getLocalPort());
				logger.info("UDP (mDatagramSocketWrite)socket created at IP:"+mDatagramSocketWrite.getLocalAddress()+" PORT: "+mDatagramSocketWrite.getLocalPort());
			} else if(Constance.MCUDP) {
				mMCSocketAzPlot = new MulticastSocket(Constance.PORT_AZ_PLOTS);
				mMCSocketElPlot = new MulticastSocket(Constance.PORT_EL_PLOTS);
				mMCSocketAzTrack = new MulticastSocket(Constance.PORT_AZ_TRACKS);
				mMCSocketElTrack = new MulticastSocket(Constance.PORT_EL_TRACKS);
				mMCSocketVideo = new MulticastSocket(Constance.PORT_VIDEO);
				mMCSocketWrite = new MulticastSocket(Constance.PORT_WRITE);
				
				groupAddr = InetAddress.getByName(Constance.GROUP_ADDR);
				mMCSocketAzPlot.joinGroup(groupAddr);
				mMCSocketElPlot.joinGroup(groupAddr);
				mMCSocketAzTrack.joinGroup(groupAddr);
				mMCSocketElTrack.joinGroup(groupAddr);
				mMCSocketVideo.joinGroup(groupAddr);
				mMCSocketWrite.joinGroup(groupAddr);
				
				mMCSocketAzPlot.setInterface(InetAddress.getLocalHost());
				mMCSocketAzTrack.setInterface(InetAddress.getLocalHost());
				mMCSocketElPlot.setInterface(InetAddress.getLocalHost());
				mMCSocketElTrack.setInterface(InetAddress.getLocalHost());
				mMCSocketVideo.setInterface(InetAddress.getLocalHost());
				
				logger.info("MC-UDP (mMCSocketAzPlot)socket created at IP:"+mMCSocketAzPlot.getInterface()+" PORT: "+mMCSocketAzPlot.getLocalPort());
				logger.info("MC-UDP (mMCSocketElPlot)socket created at IP:"+mMCSocketElPlot.getInterface()+" PORT: "+mMCSocketElPlot.getLocalPort());
				logger.info("MC-UDP (mMCSocketAzTrack)socket created at IP:"+mMCSocketAzTrack.getInterface()+" PORT: "+mMCSocketAzTrack.getLocalPort());
				logger.info("MC-UDP (mMCSocketElTrack)socket created at IP:"+mMCSocketElTrack.getInterface()+" PORT: "+mMCSocketElTrack.getLocalPort());
				logger.info("MC-UDP (mMCSocketVideo)socket created at IP:"+mMCSocketVideo.getInterface()+" PORT: "+mMCSocketVideo.getLocalPort());
				logger.info("MC-UDP (mMCSocketWrite)socket created at IP:"+mMCSocketWrite.getInterface()+" PORT: "+mMCSocketWrite.getLocalPort());
			}
	    	
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Socket creation failed",e);
		}
    }
	
	public void closeActiveConnection() {
        if(Constance.TCPIP) {
				try {
					mSocketAzPlot.close();
					mSocketElPlot.close();
					mSocketAzTrack.close();
					mSocketElTrack.close();
					mSocketVideo.close();
					mSocketWrite.close();
					logger.info("Network Task TCP socket Closed/Ended");
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("TCP socket close failed",e);
				}
		} else if(Constance.UDPIP) {
				try {
					mDatagramSocketAzPlot.close();
					mDatagramSocketElPlot.close();
					mDatagramSocketAzTrack.close();
					mDatagramSocketElTrack.close();
					mDatagramSocketVideo.close();
					mDatagramSocketWrite.close();
					logger.info("Network Task UDP socket Closed/Ended");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("UDP socket close failed",e);
				}
		} else if(Constance.MCUDP) {
			try {				
				mMCSocketAzPlot.close();
				mMCSocketElPlot.close();
				mMCSocketAzTrack.close();
				mMCSocketElTrack.close();
				mMCSocketVideo.close();
				mMCSocketWrite.close();
				logger.info("Network Task MC-UDP socket Closed/Ended");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("MC-UDP socket close failed",e);
			}
		}
	}
	
	public void InterruptableUDPThread(DatagramSocket socket){
		this.mDatagramSocketAzPlot = socket;
	    this.mDatagramSocketElPlot = socket;
	    this.mDatagramSocketAzTrack = socket;
	    this.mDatagramSocketElTrack = socket;
	    this.mDatagramSocketVideo = socket;
	    this.mDatagramSocketWrite = socket;
	}
	
	public void sendBytes(byte[] myByteArray) throws IOException {
	    sendBytes(myByteArray, 0, myByteArray.length);
	}

	
	public void sendBytes(byte[] data, int start, int len) throws IOException {
	    if (len < 0)
	        throw new IllegalArgumentException("Negative length not allowed");
	    if (start < 0 || start >= data.length)
	        throw new IndexOutOfBoundsException("Out of bounds: " + start);
	
		if(Constance.TCPIP) {
			DataOutputStream mServerSocketOutPacket = new DataOutputStream(mSocketWrite.getOutputStream());
			mServerSocketOutPacket.writeInt(len);
		    if (len > 0)
		    	mServerSocketOutPacket.write(data);
			logger.info("TCP Server Data Sent: "+len);
		} else if(Constance.UDPIP) {
			DatagramPacket mDatagramOutPacket = new DatagramPacket(data, len);
			mDatagramOutPacket.setLength(len);
			if (len > 0)
				mDatagramSocketWrite.send(mDatagramOutPacket);
			logger.info("UDP Server Data sent: "+len);
		}	else if(Constance.MCUDP) {
			DatagramPacket mDatagramOutPacket = new DatagramPacket(data, len, InetAddress.getByName(Constance.GROUP_ADDR), Constance.PORT_WRITE);
			mDatagramOutPacket.setLength(len);
			if (len > 0)
				mMCSocketWrite.send(mDatagramOutPacket);
			logger.info("MC-UDP Server Data sent: "+len);
		}
	}
	
	private byte[] parseTCPData(Socket socket) throws IOException{

		DataInputStream mServerSocketInPacket = new DataInputStream(socket.getInputStream());
		int len = mServerSocketInPacket.readInt();
	    byte[] tcpdata = new byte[len];
		// Wait to receive a socket data
	    if (len > 0) {
	    	mServerSocketInPacket.readFully(tcpdata);
//			logger.info("TCP Server Data received: "+len);
			return tcpdata;
	    }
		return null; 
	}
	
	private byte[] parseUDPData(DatagramSocket datagramSocket) throws IOException{

		byte[] mUDPSocketbuffer = new byte[Constance.DGRAM_LEN];
		int len = mUDPSocketbuffer.length;
		DatagramPacket mDatagramInPacket = new DatagramPacket(mUDPSocketbuffer, mUDPSocketbuffer.length);
		// Wait to receive a datagram
    	datagramSocket.receive(mDatagramInPacket);
		logger.info("UDP Server Data received: "+len);
		return mUDPSocketbuffer;
	}
	
	private byte[] parseMCUDPData(MulticastSocket multicastSocket) throws IOException{

		byte[] mMCUDPSocketbuffer = new byte[Constance.DGRAM_LEN];
		int len = mMCUDPSocketbuffer.length;
		DatagramPacket mDatagramInPacket = new DatagramPacket(mMCUDPSocketbuffer, len);
		// Wait to receive a datagram
    	multicastSocket.receive(mDatagramInPacket);
//		logger.info("MC-UDP Server Data received");
		return mMCUDPSocketbuffer;
	}
	
	private void makeData(byte[] mData) {
		//identify data
		final String msgName = DataIdentifier.getMessageType(mData);
//		logger.info("Server Data Identified: "+msgName);
		
		//decode data
		if(msgName != null) {
			Object object = mDataManager.decodeMsg(msgName, mData);
//			logger.info("Server Data Decoded");
			if(object instanceof AzimuthPlanePlotsPerCPIMsg) {
				AzimuthPlanePlotsPerCPIMsg aPlotsPerCPIMsg = (AzimuthPlanePlotsPerCPIMsg) object;
//				logger.info("Server Data AzimuthPlanePlotsPerCPIMsg added: "+aPlotsPerCPIMsg.toString());
				//add data
				mDataObserver.addAzPlots(aPlotsPerCPIMsg);
			} else if(object instanceof AzimuthPlaneTrackMsg) {
				AzimuthPlaneTrackMsg aTrackMsg = (AzimuthPlaneTrackMsg) object;
//				logger.info("Server Data AzimuthPlaneTrackMsg added: "+aTrackMsg.toString());
				//add data
				mDataObserver.addAzTracks(aTrackMsg);
			} else if(object instanceof ElevationPlanePlotsPerCPIMsg) {
				ElevationPlanePlotsPerCPIMsg ePlotsPerCPIMsg = (ElevationPlanePlotsPerCPIMsg) object;
//				logger.info("Server Data ElevationPlanePlotsPerCPIMsg added: "+ePlotsPerCPIMsg.toString());
				//add data
				mDataObserver.addElPlots(ePlotsPerCPIMsg);
			} else if(object instanceof ElevationPlaneTrackMsg) {
				ElevationPlaneTrackMsg eTrackMsg = (ElevationPlaneTrackMsg) object;
//				logger.info("Server Data ElevationPlaneTrackMsg added: "+eTrackMsg.toString());
				//add data
				mDataObserver.addElTracks(eTrackMsg);
			}
			
			//notify UI
			iCManager.manageData(mDataObserver);
		}
	}
	
	private void makeVideoData(byte[] data) {
		//identify data
		final String msgName = DataIdentifier.verifyVideoMessage(data);
//		logger.info("Server Data Identified: "+msgName);
		
		//decode data
		if(msgName != null) {
			Object object = mDataManager.decodeMsg(msgName, data);
//			logger.info("Server Data Decoded");
			if(object instanceof PlaneRAWVideoMsg) {
				PlaneRAWVideoMsg vidMsg = (PlaneRAWVideoMsg) object;
//				logger.info("Server Data PlaneRAWVideoMsg added: "+vidMsg.toString());
				//add data
				mDataObserver.addRAWVideo(vidMsg);
			}
			
			//notify UI
			iCManager.manageData(mDataObserver);
		}
	}
	
	private void recordData(byte[] mData) {
		//record data
		
	}

}