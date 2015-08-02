package messages.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DataIdentifier implements IByteSum{
	
	public static final String AZ_PLOT_MSG = "AzPlotMsgClass";
	public static final String AZ_TRACK_MSG = "AzTrackMsgClass";
	public static final String EL_PLOT_MSG = "ElPlotMsgClass";
	public static final String EL_TRACK_MSG = "ElTrackMsgClass";
	public static final String VIDEO_MSG = "VideoMsgClass";
		
	public static final Map<String, Integer> MSG_CLASS_ID;
	static {
		Map<String, Integer> msgType = new HashMap<String, Integer>();

		msgType.put(AZ_PLOT_MSG, 0x00770011);
		msgType.put(AZ_TRACK_MSG, 0x00660011);
		msgType.put(EL_PLOT_MSG, 0x00550011);
		msgType.put(EL_TRACK_MSG, 0x00440011);
		msgType.put(VIDEO_MSG, 0x00330011);
		
		MSG_CLASS_ID = Collections.unmodifiableMap(msgType);
	}	
	
	public static String getMessageType(final byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
		int clsid = (int)bb.getInt();
		bb.clear();

		for (Entry<String, Integer> entry : MSG_CLASS_ID.entrySet()) {
            if (entry.getValue().equals(clsid)) {
            	return (entry.getKey());
            }
        }
		return null;
	}
}