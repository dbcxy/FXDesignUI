package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import messages.utils.IByteSum;

public class ElevationPlaneBeamPositionMsg implements Serializable,IByteSum{

	private static final long serialVersionUID = 1L;
	final static int MSG_SIZE = 16;//4*4
	
	private short messageClass;
	private short messageId;
	private short source;
	private short elBeamPosNo;
	private short scanNo;
	private short reserved;
	private int reservedInt;
	
	public ElevationPlaneBeamPositionMsg() {
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

	public short getElBeamPosNo() {
		return elBeamPosNo;
	}

	public void setElBeamPosNo(short elBeamPosNo) {
		this.elBeamPosNo = elBeamPosNo;
	}

	public short getScanNo() {
		return scanNo;
	}

	public void setScanNo(short scanNo) {
		this.scanNo = scanNo;
	}

	public short getReserved() {
		return reserved;
	}

	public void setReserved(short reserved) {
		this.reserved = reserved;
	}

	public int getReservedInt() {
		return reservedInt;
	}

	public void setReservedInt(int reservedInt) {
		this.reservedInt = reservedInt;
	}
	
	/*
	 * Byte buffer to Tx
	 */
	public ByteBuffer getByteBuffer() {

		ByteBuffer buffer = ByteBuffer.allocate(MSG_SIZE);
		buffer.putShort(messageId);
		buffer.putShort(messageClass);
		
		buffer.putShort(elBeamPosNo);
		buffer.putShort(source);
		
		buffer.putShort(reserved);
		buffer.putShort(scanNo);
		
		buffer.putInt(reservedInt);
		
		return buffer;
	}
		
	/*
	 * Byte array to Rx and decode object	
	 */
	public void setByteBuffer(byte[] bArr) {
		ByteBuffer bb = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
		
		int index = 0;
		messageId = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		messageClass = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		elBeamPosNo = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		source = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		reserved = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		scanNo = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		
		reservedInt = (int)bb.getInt(index);index += BYTES_PER_INT;		
	}
	
	@Override
	public String toString() {
		return "ElBeamPos: "
				+"\n ID:"+messageId
				+"\n Class:"+messageClass
				+"\n ElBeamPosNo: "+elBeamPosNo
				+"\n ScanNo :"+scanNo;		
	}

}
