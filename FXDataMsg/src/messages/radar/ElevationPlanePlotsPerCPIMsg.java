package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import messages.utils.IByteSum;

public class ElevationPlanePlotsPerCPIMsg implements Serializable,IByteSum {

	private static final long serialVersionUID = 1L;
	final static int MSG_SIZE = 8;//Word2*4

	private int messageHeader;
	private short source;
	private short plotCount;
	private List<ElevationPlaneDetectionPlotMsg> elevationPlaneDetectionPlotMsg = new ArrayList<ElevationPlaneDetectionPlotMsg>();
	
	private StringBuilder strBuilder = new StringBuilder();
	
	public ElevationPlanePlotsPerCPIMsg() {
		
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

		ByteBuffer buffer = ByteBuffer.allocate(MSG_SIZE);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(messageHeader);
		buffer.putShort(plotCount);
		buffer.putShort(source);
		byte[] cpiBuf = buffer.array();
		
		List<Byte> plotList = new ArrayList<Byte>(Arrays.asList(ArrayUtils.toObject(cpiBuf)));
		for(ElevationPlaneDetectionPlotMsg eMsg: elevationPlaneDetectionPlotMsg){
			byte[] subBuf = eMsg.getByteBuffer().array();
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
				+"\n Header:"+messageHeader
				+"\n PlotCount: "+plotCount
				+"\n"+ strBuilder.toString();
	}

	
}
