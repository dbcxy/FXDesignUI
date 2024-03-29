package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import messages.utils.IByteSum;

public class AzimuthPlaneDetectionPlotMsg implements Serializable,IByteSum{

	private static final long serialVersionUID = 1L;
	public final static int MSG_SIZE = 40;//Allocating
	
	private int messageHeader;
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
	
	
	/*
	 * Byte buffer to Tx
	 */
	public ByteBuffer getByteBuffer() {

		ByteBuffer buffer = ByteBuffer.allocate(MSG_SIZE);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(messageHeader);
		
		buffer.putShort(reserved);
		buffer.putShort(source);
		
		buffer.putInt(range);
		buffer.putInt(azimuth);
		buffer.putInt(reservedInt);
		buffer.putInt(strength);
		
		buffer.put(dopplerExtension);
		buffer.put(reservedByte);
		buffer.put(azimuthExtension);
		buffer.put(rangeExtension);
		
		buffer.putInt(reservedInt);
		buffer.putInt(timeStampLow);
		buffer.putInt(timeStampHigh);
		
		return buffer;
	}
		
	/*
	 * Byte array to Rx and decode object	
	 */
	public void setByteBuffer(byte[] bArr) {
		ByteBuffer bb = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
		
		int index = 0;
		messageHeader = (int)bb.getInt(index);index += BYTES_PER_INT;	
		reserved = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		source = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		
		range = (int)bb.getInt(index);index += BYTES_PER_INT;
		azimuth = (short)bb.getInt(index);index += BYTES_PER_INT;
		reservedInt = (int)bb.getInt(index);index += BYTES_PER_INT;
		strength = (int)bb.getInt(index);index += BYTES_PER_INT;

		dopplerExtension = (byte)bb.get(index);index += BYTES;
		reservedByte = (byte)bb.get(index);index += BYTES;
		azimuthExtension = (byte)bb.get(index);index += BYTES;
		rangeExtension = (byte)bb.get(index);index += BYTES;
		
		reservedInt = (int)bb.getInt(index);index += BYTES_PER_INT;
		timeStampLow = (int)bb.getInt(index);index += BYTES_PER_INT;
		timeStampHigh = (int)bb.getInt(index);index += BYTES_PER_INT;
		
	}
	
	@Override
	public String toString() {
		return "AzPlot: "
				+"\n Header:"+messageHeader
				+"\n Range: "+range
				+"\n Az :"+azimuth;		
	}
}
