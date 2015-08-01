package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import messages.utils.IByteSum;

public class AzimuthPlanePlotsPerCPIMsg implements Serializable,IByteSum {

	private static final long serialVersionUID = 1L;
	final static int MSG_SIZE = 8;//Word2*4
	ByteBuffer buffer = ByteBuffer.allocate(MSG_SIZE);

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

	public void setMessageClass(short messageType) {
		this.messageClass = messageType;
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
	
	public byte[] getByteBuffer() {
		putMessageId(messageId);
		putMessageClass(messageClass);
		putPlotCount(plotCount);
		putSource(source);
		byte[] b1 = buffer.array();
		for(AzimuthPlaneDetectionPlotMsg aMsg: azimuthPlaneDetectionPlotMsg){

		}
		
		//JUGAD
		byte[] b2 = azimuthPlaneDetectionPlotMsg.get(azimuthPlaneDetectionPlotMsg.size()-1).getByteBuffer().array();
		byte[] res = new byte[b1.length+b2.length];
		System.arraycopy(b1	, 0, res, 0, b1.length);
		System.arraycopy(b2, 0, res, b1.length, b2.length);
		
		return res;
	}
	
	private void putMessageClass(short val) {
		buffer.putShort(val);
	}
	
	private void putMessageId(short val) {
		buffer.putShort(val);
	}
	
	private void putSource(short val) {
		buffer.putShort(val);
	}
	
	private void putPlotCount(short val) {
		buffer.putShort(val);
	}
	
	/*
	 * Byte array to Rx and decode object	
	 */
	public void setByteBuffer(byte[] bArr) {
		ByteBuffer bb = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
		
		int index = 0;
		messageId = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		messageClass = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		plotCount = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		source = (short)bb.getShort(index);index += BYTES_PER_SHORT;

		for(int i=0;i<plotCount;i++) {
			byte [] subArray = Arrays.copyOfRange(bArr, index, index+AzimuthPlaneDetectionPlotMsg.MSG_SIZE);
			AzimuthPlaneDetectionPlotMsg aPlotMsg = new AzimuthPlaneDetectionPlotMsg();
			aPlotMsg.setByteBuffer(subArray);
			addAzimuthPlaneDetectionPlotMsg(aPlotMsg);
			index = index+AzimuthPlaneDetectionPlotMsg.MSG_SIZE; 
		}
		
	}

	
}
