package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;


class TCPServerThread extends Thread {
	
	static final int MSG_ID = 0x00050002;
	static final int SRC_ID = 0x00040005;
	
	private ServerSocket serverSocketWrite;
	private ServerSocket serverSocketRead;

    DataInputStream in;
   
	   public TCPServerThread(int rccTorcd, int rcdTorcc) throws IOException {
		   serverSocketWrite = new ServerSocket(rccTorcd);
		   serverSocketWrite.setSoTimeout(10000);
		   
		   serverSocketRead = new ServerSocket(rcdTorcc);
		   serverSocketRead.setSoTimeout(10000);
	   }
	
	   @Override
	   public void run() {
	      try {
	            System.out.println("Waiting for client on port " + serverSocketWrite.getLocalPort() + "...");
	            System.out.println("Waiting for client on port " + serverSocketRead.getLocalPort() + "...");
	        
	            Socket serverRead = serverSocketRead.accept();
		        System.out.println("Just connected to " + serverRead.getRemoteSocketAddress());

		        Socket serverWrite = serverSocketWrite.accept();
		        System.out.println("Just connected to " + serverWrite.getRemoteSocketAddress());
		        
		        new Thread(new Runnable() {
					
					@Override
					public void run() {
						while(true) {
							try {
								in = new DataInputStream(serverRead.getInputStream());
								int len = in.readInt();
							    byte[] tcpdata = new byte[len];
								// Wait to receive a socket data
							    if (len > 0) {
							    	in.readFully(tcpdata);
							    	ByteBuffer buf = ByteBuffer.wrap(tcpdata);
									System.out.println("Stream Rx: "+C2Server.bytesToHex(tcpdata)+", size: "+len);
									for(int i =0;i<len/4;i++) {
										int val = buf.getInt();
										if(val==MSG_ID)
											System.out.println("Matched MSG ID");
										if(val==SRC_ID)
											System.out.println("Matched SRC ID");
										System.out.println("Decoded Stream Rx: "+val);
									}
							    } 
							} catch (IOException e) {
								e.printStackTrace();
								System.out.println("Exception");
							}
						}
						
					}
				}).start();

//				Timer timer = new Timer();
//				timer.schedule(new TimerTask() {
//					
//					@Override
//					public void run() {
//						
//						try {
//							DataOutputStream out = new DataOutputStream(serverWrite.getOutputStream());
//							out.writeInt(50);
//						    out.write(new byte[50]);
//					        System.out.println("Stream Tx: "+out.size());
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//						
//					}
//				}, 5000, 10000);
		        
		     } catch(SocketTimeoutException s) {
		    	 System.out.println("Socket timed out!");
		     } catch(IOException e) {
		    	 e.printStackTrace();
		     }
	   }
}