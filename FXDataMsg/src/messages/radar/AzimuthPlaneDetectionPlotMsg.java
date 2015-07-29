package messages.radar;

import java.nio.ByteBuffer;

public class AzimuthPlaneDetectionPlotMsg {

//	final static int MSG_SIZE = 40;//Allocating
//	ByteBuffer buffer = ByteBuffer.allocate(MSG_SIZE);
//	
//	public AzimuthPlaneDetectionPlotMsg() {
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
//	public void putReserved(short val) {
//		buffer.putShort(val);
//	}
//	
//	public void putRange(int val) {
//		buffer.putInt(val);
//	}
//	
//	public void putAzimuth(int val) {
//		buffer.putInt(val);
//	}
//	
//	public void putStrength(int val) {
//	    buffer.putInt(val);
//  }
//	
//	public void putReserved(int val) {
//		buffer.putInt(val);
//	}
//	
//	public void putRangeExtension(byte val) {
//		buffer.put(val);
//	}
//	
//	public void AzimuthExtension(byte val) {
//		buffer.put(val);
//	}
//	
//	public void putReserved(byte val) {
//		buffer.put(val);
//	}
//	
//	public void putDopplerExtension(byte val) {
//		buffer.put(val);
//	}
//	
//	public void putTimeStampLow(int val) {
//		buffer.putInt(val);
//	}
//	
//	public void putTimeStampHigh(int val) {
//		buffer.putInt(val);
//	}
	
	private short messageClass;
	private short messageId;
	private short source;
	private short reserved;
	private int range;
	private int azimuth;
	private int reservedInt;
	private int strength;
	private byte rangeExtension;
	private byte azimuthExtension;
	private byte reservedByte;
	private byte dopplerExtension;
	private int timeStampLow;
	private int timeStampHigh;
	
	public AzimuthPlaneDetectionPlotMsg() {
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

	public short getReserved() {
		return reserved;
	}

	public void setReserved(short reserved) {
		this.reserved = reserved;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(int azimuth) {
		this.azimuth = azimuth;
	}

	public int getReservedInt() {
		return reservedInt;
	}

	public void setReservedInt(int reservedInt) {
		this.reservedInt = reservedInt;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public byte getRangeExtension() {
		return rangeExtension;
	}

	public void setRangeExtension(byte rangeExtension) {
		this.rangeExtension = rangeExtension;
	}

	public byte getAzimuthExtension() {
		return azimuthExtension;
	}

	public void setAzimuthExtension(byte azimuthExtension) {
		this.azimuthExtension = azimuthExtension;
	}

	public byte getReservedByte() {
		return reservedByte;
	}

	public void setReservedByte(byte reservedByte) {
		this.reservedByte = reservedByte;
	}

	public byte getDopplerExtension() {
		return dopplerExtension;
	}

	public void setDopplerExtension(byte dopplerExtension) {
		this.dopplerExtension = dopplerExtension;
	}

	public int getTimeStampLow() {
		return timeStampLow;
	}

	public void setTimeStampLow(int timeStampLow) {
		this.timeStampLow = timeStampLow;
	}

	public int getTimeStampHigh() {
		return timeStampHigh;
	}

	public void setTimeStampHigh(int timeStampHigh) {
		this.timeStampHigh = timeStampHigh;
	}
	
	
	
	
}
