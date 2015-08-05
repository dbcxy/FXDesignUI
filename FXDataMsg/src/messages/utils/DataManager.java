package messages.utils;

import messages.radar.AzimuthPlanePlotsPerCPIMsg;
import messages.radar.AzimuthPlaneTrackMsg;
import messages.radar.ElevationPlanePlotsPerCPIMsg;
import messages.radar.ElevationPlaneTrackMsg;
import messages.radar.PlaneRAWVideoMsg;

public class DataManager implements IByteSum{

	public byte[] encodeMsg(String type, Object object) {
		switch (type) {
		case DataIdentifier.AZ_PLOT_MSG:
			return encodeAzPlotmsg(object);

		case DataIdentifier.AZ_TRACK_MSG:
			return encodeAzTrackmsg(object);
			
		case DataIdentifier.EL_PLOT_MSG:
			return encodeElPlotmsg(object);
			
		case DataIdentifier.EL_TRACK_MSG:
			return encodeElTrackmsg(object);
			
		case DataIdentifier.VIDEO_MSG:
			return encodeVideomsg(object);
			
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

	private byte[] encodeElPlotmsg(Object object) {
		ElevationPlanePlotsPerCPIMsg ePlotsPerCPIMsg = (ElevationPlanePlotsPerCPIMsg) object;
		
		return ePlotsPerCPIMsg.getByteBuffer();
	}
	
	private byte[] encodeElTrackmsg(Object object) {
		ElevationPlaneTrackMsg eTrackMsg = (ElevationPlaneTrackMsg) object;
		
		return eTrackMsg.getByteBuffer().array();
	}
	
	private byte[] encodeVideomsg(Object object) {
		PlaneRAWVideoMsg pVideoMsg = (PlaneRAWVideoMsg) object;
		
//		return pVideoMsg.getByteBuffer().array();
		return null;
	}
	
	public Object decodeMsg(final String type, final byte[] data) {
		switch (type) {
		case DataIdentifier.AZ_PLOT_MSG:
			return decodeAzPlotmsg(data);
			
		case DataIdentifier.AZ_TRACK_MSG:
			return decodeAzTrackmsg(data);
			
		case DataIdentifier.EL_PLOT_MSG:
			return decodeElPlotmsg(data);
			
		case DataIdentifier.EL_TRACK_MSG:
			return decodeElTrackmsg(data);
			
		case DataIdentifier.VIDEO_MSG:
			return decodeVideomsg(data);
			
		default:
			break;
		}
		return null;
	}
	
	private PlaneRAWVideoMsg decodeVideomsg(byte[] data) {
		PlaneRAWVideoMsg pVideoMsg = new PlaneRAWVideoMsg();
		//TODO
		
		//return pVideoMsg;
		return null;
	}

	private ElevationPlaneTrackMsg decodeElTrackmsg(byte[] data) {
		ElevationPlaneTrackMsg eTrackMsg = new ElevationPlaneTrackMsg();
		eTrackMsg.setByteBuffer(data);
		return eTrackMsg;
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
	
	private ElevationPlanePlotsPerCPIMsg decodeElPlotmsg(byte[] data) {
		ElevationPlanePlotsPerCPIMsg ePlotsPerCPIMsg = new ElevationPlanePlotsPerCPIMsg();
		ePlotsPerCPIMsg.setByteBuffer(data);
		return ePlotsPerCPIMsg;
	}

}
