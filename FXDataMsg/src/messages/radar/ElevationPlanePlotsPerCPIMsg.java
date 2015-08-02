package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import messages.utils.IByteSum;

public class ElevationPlanePlotsPerCPIMsg implements Serializable,IByteSum {

	private static final long serialVersionUID = 1L;
	final static int MSG_SIZE = 8;//Word2*4
	ByteBuffer buffer = ByteBuffer.allocate(MSG_SIZE);

	private short messageClass;
	private short messageId;
	private short source;
	private short plotCount;
	private List<ElevationPlaneDetectionPlotMsg> elevationPlaneDetectionPlotMsg = new ArrayList<ElevationPlaneDetectionPlotMsg>();
	
	private StringBuilder strBuilder = new StringBuilder();
	
	public ElevationPlanePlotsPerCPIMsg() {
		
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

	public List<ElevationPlaneDetectionPlotMsg> getElevationPlaneDetectionPlotMsg() {
		return elevationPlaneDetectionPlotMsg;
	}

	public void setElevationPlaneDetectionPlotMsg(
			List<ElevationPlaneDetectionPlotMsg> elevationPlaneDetectionPlotMsg) {
		this.elevationPlaneDetectionPlotMsg = elevationPlaneDetectionPlotMsg;
	}
	
	public void addElevationPlaneDetectionPlotMsg(ElevationPlaneDetectionPlotMsg aPlotMsg) {
		this.elevationPlaneDetectionPlotMsg.add(aPlotMsg);
	}
	
	public byte[] getByteBuffer() {
		buffer.putShort(messageId);
		buffer.putShort(messageClass);
		buffer.putShort(plotCount);
		buffer.putShort(source);
		byte[] b1 = buffer.array();
		for(ElevationPlaneDetectionPlotMsg aMsg: elevationPlaneDetectionPlotMsg){
			
		}
		
		//JUGAD
		byte[] b2 = elevationPlaneDetectionPlotMsg.get(elevationPlaneDetectionPlotMsg.size()-1).getByteBuffer().array();
		byte[] res = new byte[b1.length+b2.length];
		System.arraycopy(b1	, 0, res, 0, b1.length);
		System.arraycopy(b2, 0, res, b1.length, b2.length);
		
		return res;
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
			byte [] subArray = Arrays.copyOfRange(bArr, index, index+ElevationPlaneDetectionPlotMsg.MSG_SIZE);
			ElevationPlaneDetectionPlotMsg aPlotMsg = new ElevationPlaneDetectionPlotMsg();
			aPlotMsg.setByteBuffer(subArray);
			addElevationPlaneDetectionPlotMsg(aPlotMsg);
			index = index+ElevationPlaneDetectionPlotMsg.MSG_SIZE;
			strBuilder.append(aPlotMsg.toString()+"\n");
		}		
	}
	
	@Override
	public String toString() {
		return "ElPlotCPI: "
				+"\n ID:"+messageId
				+"\n Class:"+messageClass
				+"\n PlotCount: "+plotCount
				+"\n"+ strBuilder.toString();
	}

	
}
