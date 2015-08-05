package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import messages.utils.IByteSum;

public class AzimuthPlanePlotsPerCPIMsg implements Serializable,IByteSum {

	private static final long serialVersionUID = 1L;
	final static int MSG_SIZE = 8;//Word2*4

	private int messageHeader;
	private short source;
	private short plotCount;
	private List<AzimuthPlaneDetectionPlotMsg> azimuthPlaneDetectionPlotMsg = new ArrayList<AzimuthPlaneDetectionPlotMsg>();
	
	private StringBuilder strBuilder = new StringBuilder();
	
	public AzimuthPlanePlotsPerCPIMsg() {
		
	}

	public int getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(int messageClass) {
		this.messageHeader = messageClass;
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
		
		ByteBuffer buffer = ByteBuffer.allocate(MSG_SIZE);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(messageHeader);
		buffer.putShort(plotCount);
		buffer.putShort(source);
		byte[] cpiBuf = buffer.array();
		
		List<Byte> plotList = new ArrayList<Byte>(Arrays.asList(ArrayUtils.toObject(cpiBuf)));
		for(AzimuthPlaneDetectionPlotMsg aMsg: azimuthPlaneDetectionPlotMsg){
			byte[] subBuf = aMsg.getByteBuffer().array();
			List<Byte> subList = new ArrayList<Byte>(Arrays.asList(ArrayUtils.toObject(subBuf)));
			plotList.addAll(subList);
		}
		Byte[] out = new Byte[plotList.size()];
		plotList.toArray(out);
		byte[] res = new byte[plotList.size()]; 
		res = ArrayUtils.toPrimitive(out);
		
		return res;
	}
	
	/*
	 * Byte array to Rx and decode object	
	 */
	public void setByteBuffer(byte[] bArr) {
		ByteBuffer bb = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
		
		int index = 0;
		messageHeader = (int)bb.getInt(index);index += BYTES_PER_INT;	
		plotCount = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		source = (short)bb.getShort(index);index += BYTES_PER_SHORT;

		for(int i=0;i<plotCount;i++) {
			byte [] subArray = Arrays.copyOfRange(bArr, index, index+AzimuthPlaneDetectionPlotMsg.MSG_SIZE);
			AzimuthPlaneDetectionPlotMsg aPlotMsg = new AzimuthPlaneDetectionPlotMsg();
			aPlotMsg.setByteBuffer(subArray);
			addAzimuthPlaneDetectionPlotMsg(aPlotMsg);
			index = index+AzimuthPlaneDetectionPlotMsg.MSG_SIZE;
			strBuilder.append(aPlotMsg.toString()+"\n");
		}		
	}
	
	@Override
	public String toString() {
		return "AzPlotCPI: "
				+"\n Header:"+messageHeader
				+"\n PlotCount: "+plotCount
				+"\n"+ strBuilder.toString();
	}
	
}
