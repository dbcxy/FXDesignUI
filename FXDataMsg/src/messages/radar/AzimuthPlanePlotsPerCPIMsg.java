package messages.radar;

import java.nio.ByteBuffer;

public class AzimuthPlanePlotsPerCPIMsg {
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
	
	private short messageClass;
	private short messageId;
	private short source;
	private short plotCount;
	private AzimuthPlaneDetectionPlotMsg azimuthPlaneDetectionPlotMsg;
	
	public AzimuthPlanePlotsPerCPIMsg() {
		// TODO Auto-generated constructor stub
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

	public AzimuthPlaneDetectionPlotMsg getAzimuthPlaneDetectionPlotMsg() {
		return azimuthPlaneDetectionPlotMsg;
	}

	public void setAzimuthPlaneDetectionPlotMsg(
			AzimuthPlaneDetectionPlotMsg azimuthPlaneDetectionPlotMsg) {
		this.azimuthPlaneDetectionPlotMsg = azimuthPlaneDetectionPlotMsg;
	}
	
}
