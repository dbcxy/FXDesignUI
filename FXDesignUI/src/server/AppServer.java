package server;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class AppServer {

	static final Logger logger = Logger.getLogger(AppServer.class);
	
	public static final int SERVERPORT 		= 30401;
	public static final int DELAY			= 10000;
		
	private static boolean TCPIP = true;
	
	public static void main(String[] args) {
		loadLoggerSettings();
		if(TCPIP) {
			Runnable TCP = new TCP();
			Thread serverTCPThread = new Thread(TCP);
			serverTCPThread.start();
		} else {
			Runnable UDP = new UDP();
			Thread serverUDPThread = new Thread(UDP);
			serverUDPThread.start();
		}

	}
	
	private static void loadLoggerSettings() {
		PropertyConfigurator.configure("res/log4j.properties");
		logger.info("AppServer Started");
	}
}
