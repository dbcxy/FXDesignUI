package messages.utils;

import messages.radar.AzimuthPlanePlotsPerCPIMsg;
import messages.radar.AzimuthPlaneTrackMsg;

public class DataManager implements IByteSum{

	public byte[] encodeMsg(String type, Object object) {
		switch (type) {
		case DataIdentifier.AZ_PLOT_MSG:
			return encodeAzPlotmsg(object);

		case DataIdentifier.AZ_TRACK_MSG:
			return encodeAzTrackmsg(object);
			
		default:
			break;
		}
		return null;
	}

	private byte[] encodeAzPlotmsg(Object object) {
		AzimuthPlanePlotsPerCPIMsg aPlotsPerCPIMsg = (AzimuthPlanePlotsPerCPIMsg) object;
				
		return aPlotsPerCPIMsg.getByteBuffer();
	}

	private byte[] encodeAzTrackmsg(Object object) {
		AzimuthPlaneTrackMsg aTrackMsg = (AzimuthPlaneTrackMsg) object;
		
		return aTrackMsg.getByteBuffer().array();
	}

	public Object decodeMsg(final String type, final byte[] data) {
		switch (type) {
		case DataIdentifier.AZ_PLOT_MSG:
			return decodeAzPlotmsg(data);
			
		case DataIdentifier.AZ_TRACK_MSG:
			return decodeAzTrackmsg(data);
			
		default:
			break;
		}
		return null;
	}

	private AzimuthPlaneTrackMsg decodeAzTrackmsg(byte[] data) {
		AzimuthPlaneTrackMsg aTrackMsg = new AzimuthPlaneTrackMsg();
		aTrackMsg.setByteBuffer(data);
		return aTrackMsg;
	}

	private AzimuthPlanePlotsPerCPIMsg decodeAzPlotmsg(byte[] data) {
		AzimuthPlanePlotsPerCPIMsg aPlotsPerCPIMsg = new AzimuthPlanePlotsPerCPIMsg();
		aPlotsPerCPIMsg.setByteBuffer(data);
		return aPlotsPerCPIMsg;
	}

}
