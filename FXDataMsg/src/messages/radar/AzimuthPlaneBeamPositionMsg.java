package messages.radar;

import java.nio.ByteBuffer;

public class AzimuthPlaneBeamPositionMsg {
	
//	final static int MSG_SIZE = 16;//4*4
//	ByteBuffer buffer = ByteBuffer.allocate(MSG_SIZE);
//	
//	public AzimuthPlaneBeamPositionMsg() {
//
//	}	
//	
//	public ByteBuffer getByteBuffer() {
//		return buffer;
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
//	public void putAzBeamPosNo(short val) {
//		buffer.putShort(val);
//	}
//	
//	public void putScanNo(short val) {
//		buffer.putShort(val);
//	}
//	
//	public void putReserved(short val) {
//		buffer.putShort(val);
//	}
//	
//	public void putReserver(int val) {
//		buffer.putInt(val);
//	}
	
	private short messageClass;
	private short messageId;
	private short source;
	private short azBeamPosNo;
	private short scanNo;
	private short reserved;
	private int reservedInt;
	
	public AzimuthPlaneBeamPositionMsg() {
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

	public short getAzBeamPosNo() {
		return azBeamPosNo;
	}

	public void setAzBeamPosNo(short azBeamPosNo) {
		this.azBeamPosNo = azBeamPosNo;
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
	
	

}
