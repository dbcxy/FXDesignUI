package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class TCP implements Runnable{

	static final Logger logger = Logger.getLogger(TCP.class);
	
	private ServerSocket serverSocket;
	private Socket socket;
	
	@Override
	public void run() {


			try {
				serverSocket = new ServerSocket(AppServer.SERVERPORT);
				logger.info("SERVER TCP socket created");
				socket = serverSocket.accept();
				logger.info("SERVER TCP client accepted");
			} catch (IOException e) {
				e.printStackTrace();
				closeTCP();
			}
			sendTCPData();
	}
	
	public void sendTCPData() {
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())),
					true);
		
//			final InputStream is = null;
//		    BufferedReader bfr = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		    String str="Hello";
		    String s ="";
		    do{
		    	s = str;
//		    	str = bfr.readLine();
		    	out.println(s);
		    	System.out.println("SERVER Tx:" +s);
		    	Thread.sleep(AppServer.DELAY);
		    	
		    }while(str != null);
//		    bfr.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			closeTCP();
		}
	}
	
	public void receiveTCPData() {
		try {
			BufferedReader mBReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    while(mBReader.readLine()!=null){
		    	String data = mBReader.readLine();
		    	System.out.println("SERVER Rx:" +data);
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
			closeTCP();
		}
	}
	
	public void closeTCP() {

	    if (socket != null) {
	        try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}

}