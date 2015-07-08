package utils;

public class Constance {
	
	public static final String CHANNEL = null;
	public static final String CONTROL = null;
	public static final String ROUTE = null;
	public static final String RWY = null;
	public static final String SCALE = null;
	public static final String NULL = "NULL";
	public static final String AZ_ANGLE = null;
	public static final String EL_TILT = null;
	public static final String APPROACH_ANGLE = null;
	public static final String OFFSET = null;
	public static String AZ_TILT = null;
	public static String GLIDE_SLOPE = null;
	public static String SAFETY_SLOPE = null;
	public static String SAFETY_HEIGHT = null;
	public static String DISTANCE = null;
	public static String HEIGHT = null;
	public static String EL_ANGLE = null;
	
	public static String UNITS = "NM";

	public static long ELEVATION_MAX		= 10000;//MKS or FPS
	public static long ELEVATION_DISP		= 1000;//ft/Km or NM
	public static long ELEVATION_MIN		= 0;//ft or mts
	public static long AZIMUTH_MAX			= 5000;//ft or mts
	public static long AZIMUTH_DISP			= 1000;//ft/Km or NM
	public static long AZIMUTH_MIN			= -5000;//ft or mts
	public static long RANGE_MAX			= 10;//km or NM
	public static long RANGE_DISP			= 1;//km or NM
	public static long RANGE_MIN			= 0;//km or NM
	public static long TOUCH_DOWN			= 1;//km or NM
	
	
	public static boolean IS_CLOSE			= false;
	public static boolean TCPIP 			= false;
	public static boolean UDPIP				= true;
	public static boolean IS_CONNECTED		= false;
	
	public static int MY_TIMEOUT 			= 60*1000;
	public static final String SERVER_IP 	= "127.0.0.1";
	public static int PORT_NO 				= 30401;
    public static final String GROUP_ADDR 	= "237.0.0.1";
    public static final int DGRAM_LEN 		= 2048;
	public static final int MILLI_SECOND 	= 1000;
	public static final int UPDATE_RATE 	= MILLI_SECOND;
	public static final String CURSOR 		= " > ";
	public static final String CURSOR_DUB	= " >> ";
	
}
