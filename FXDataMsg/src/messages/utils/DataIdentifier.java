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
		
	public static final Map<String, Integer> MSG_CLASS_ID;
	static {
		Map<String, Integer> msgType = new HashMap<String, Integer>();

		msgType.put(AZ_PLOT_MSG, 0x00770011);
		msgType.put(AZ_TRACK_MSG, 0x00660011);
		
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