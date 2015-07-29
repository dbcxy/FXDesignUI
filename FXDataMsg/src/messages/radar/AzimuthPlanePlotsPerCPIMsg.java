package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AzimuthPlanePlotsPerCPIMsg implements Serializable {
//	final static int MSG_SIZE = 8;//Word2*4
//	ByteBuffer buffer = ByteBuffer.allocate(MSG_SIZE);
//	AzimuthPlaneDetectionPlotMsg aPlotMsg = null;
//	
//	public AzimuthPlanePlotsPerCPIMsg() {
//		
//	}
//	
//	public ByteBuffer getByteBuffer() {
//		byte[] b1 = buffer.array();
//		byte[] b2 = aPlotMsg.getByteBuffer().array();
//		byte[] res = new byte[b1.length+b2.length];
//		System.arraycopy(b1	, 0, res, 0, b1.length);
//		System.arraycopy(b2, 0, res, b1.length, b2.length);
//		ByteBuffer bb = ByteBuffer.wrap(res);
//		return bb;
//	}
//	
//	public void putMessageClass(short val) {
//		buffer.putShort(val);
//	}
//	
//	public void putMessageId(short val) {
//		buffer.putShort(val);
//	}
//	
//	public void putSource(short val) {
//		buffer.putShort(val);
//	}
//	
//	public void putPlotCount(short val) {
//		buffer.putShort(val);
//	}
//	
//	public void setAzimuthDetectionPlotMsg(AzimuthPlaneDetectionPlotMsg aPlotMsg) {
//		this.aPlotMsg = aPlotMsg;
//	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private short messageClass;
	private short messageId;
	private short source;
	private short plotCount;
	private List<AzimuthPlaneDetectionPlotMsg> azimuthPlaneDetectionPlotMsg = new ArrayList<AzimuthPlaneDetectionPlotMsg>();
	
	public AzimuthPlanePlotsPerCPIMsg() {
		
	}

	public short getMessageClass() {
		return messageClass;
	}

	public void setMessageClass(short messageClass) {
		this.messageClass = messageClass;
	}

	public short getMessageId() {
		return messageId;
	}

	public void setMessageId(short messageId) {
		this.messageId = messageId;
	}

	public short getSource() {
		return source;
	}

	public void setSource(short source) {
		this.source = source;
	}

	public short getPlotCount() {
		return plotCount;
	}

	public void setPlotCount(short plotCount) {
		this.plotCount = plotCount;
	}

	public List<AzimuthPlaneDetectionPlotMsg> getAzimuthPlaneDetectionPlotMsg() {
		return azimuthPlaneDetectionPlotMsg;
	}

	public void setAzimuthPlaneDetectionPlotMsg(
			List<AzimuthPlaneDetectionPlotMsg> azimuthPlaneDetectionPlotMsg) {
		this.azimuthPlaneDetectionPlotMsg = azimuthPlaneDetectionPlotMsg;
	}
	
	public void addAzimuthPlaneDetectionPlotMsg(AzimuthPlaneDetectionPlotMsg aPlotMsg) {
		this.azimuthPlaneDetectionPlotMsg.add(aPlotMsg);
	}
	
}
