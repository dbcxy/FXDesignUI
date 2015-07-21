package utils;

public class Constance {
	
	public static String CHANNEL = null;
	public static String CONTROL = null;
	public static String ROUTE = null;
	public static String SCALE = null;
	public static String AZ_ANGLE = null;
	public static String EL_TILT = null;
	public static String APPROACH_ANGLE = null;
	public static String AZ_TILT = null;
	public static String GLIDE_SLOPE = null;
	public static String SAFETY_SLOPE = null;
	public static String SAFETY_HEIGHT = null;
	public static String DISTANCE = null;
	public static String HEIGHT = null;
	public static String EL_ANGLE = null;

	public static final String NULL = "NULL";
	
	public static final class UNITS {
		public static boolean isKM = true;
		public static boolean isFPS = false;
		public static String getLENGTH() { return(isKM ? "KM":"NM");}
		public static String getHEIGHT() { return(isFPS ? "meters":"feet");}
	}

	public static boolean IS_CONFIG_SET = false;
	public static boolean IS_PREF_SET = false;
	
	public static double ELEVATION_MAX		= 10000;//MKS or FPS
	public static double ELEVATION_DISP		= 1200;//ft/Km or NM
	public static double ELEVATION_MIN		= 0;//ft or mts
	public static double AZIMUTH_MAX		= 5000;//ft or mts
	public static double AZIMUTH_DISP		= 600;//ft/Km or NM
	public static double AZIMUTH_MIN		= -5000;//ft or mts
	public static double RANGE_MAX			= 40;//km or NM , Doesn't matter whether KM or NM only number matters
	public static double RANGE_DISP			= 1;//km or NM
	public static double RANGE_MIN			= 0;//km or NM
	public static double TOUCH_DOWN			= 1;//km or NM
	
	public static class ELEVATION {
		public static double GLIDE_ANGLE			= 10;//degrees
		public static double GLIDE_MAX_DIST			= 20;//KM
		public static double GLIDE_FLAT_START_DIST	= 10;//KM
		public static double USL_ANGLE				= 20;//degrees
		public static double LSL_ANGLE				= 0;//degrees
		public static double UAL_ANGLE				= GLIDE_ANGLE+1.5;//degrees
		public static double LAL_ANGLE				= GLIDE_ANGLE-1.5;//degrees
		public static double USaL_ANGLE				= GLIDE_ANGLE+3.5;//degrees
		public static double LSaL_ANGLE				= GLIDE_ANGLE-3.5;//degrees
		public static double DH						= 200;//feets or meters
	}
	
	public static class AZIMUTH {
		public static double LSL_ANGLE	= 10;//degrees
		public static double RSL_ANGLE	= 10;//degrees
		public static double RCLO		= 200;//feets or meters
		public static double LAL_ANGLE	= 4.75;//degrees
		public static double RAL_ANGLE	= -4.75;//degrees
		public static double LSaL		= 50;//feet or meter
		public static double RSaL		= 50;//feet or meter
	}

	public static class PREF {
		public static String SEL_RUNWAY		= " 1 ";//Number
		public static String RUNWAY_OFFSET	= " 100 ";//feet
		public static String MODE_OP		= " Clear Weather ";
		public static String CHG_POL		= " Horizontal ";
		public static String RANGE_UNITS	= " Kilometers (KM) ";
		public static String EL_AZ_UNITS	= " Feet (ft) ";
		public static String FREQ_SEL		= " 0 ";
		public static String BITE			= " NULL ";
		
	}
	
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
	
	
	public static double getELEVATION_DISP() {
		if(SCALE.contains("5"))
			ELEVATION_DISP		= 2200;//ft
		else if(SCALE.contains("10"))
			ELEVATION_DISP		= 1200;//ft
		else if(SCALE.contains("20"))
			ELEVATION_DISP		= 625;//ft
		else if(SCALE.contains("40"))
			ELEVATION_DISP		= 325;//ft
		
		return ELEVATION_DISP;
	}
	
	public static double getAZIMUTH_DISP() {
		if(SCALE.contains("5"))
			AZIMUTH_DISP		= 1100;//ft
		else if(SCALE.contains("10"))
			AZIMUTH_DISP		= 600;//ft
		else if(SCALE.contains("20"))
			AZIMUTH_DISP		= 300;//ft
		else if(SCALE.contains("40"))
			AZIMUTH_DISP		= 150;//ft
		
		return AZIMUTH_DISP;
	}
	
	public static String getSCALE() {
		if(SCALE.contains("5"))
			SCALE		= " 5 "+Constance.UNITS.getLENGTH();
		else if(SCALE.contains("10"))
			SCALE		= " 10 "+Constance.UNITS.getLENGTH();
		else if(SCALE.contains("20"))
			SCALE		= " 20 "+Constance.UNITS.getLENGTH();
		else if(SCALE.contains("40"))
			SCALE		= " 40 "+Constance.UNITS.getLENGTH();
		
		return SCALE;
	}
	
}
