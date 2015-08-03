package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import messages.utils.IByteSum;

public class ElevationPlaneDetectionPlotMsg implements Serializable,IByteSum{

	private static final long serialVersionUID = 1L;
	public final static int MSG_SIZE = 40;//Allocating
	
	private short messageClass;
	private short messageId;
	private short source;
	private short reserved;
	private int range;
	private int elevation;
	private int reservedInt;
	private int strength;
	private byte rangeExtension;
	private byte elevationExtension;
	private byte reservedByte;
	private byte dopplerExtension;
	private int timeStampLow;
	private int timeStampHigh;
	
	public ElevationPlaneDetectionPlotMsg() {
		
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

	public int getElevation() {
		return elevation;
	}

	public void setElevation(int elevation) {
		this.elevation = elevation;
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

	public byte getElevationExtension() {
		return elevationExtension;
	}

	public void setElevationExtension(byte elevationExtension) {
		this.elevationExtension = elevationExtension;
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
		buffer.putShort(messageId);
		buffer.putShort(messageClass);
		
		buffer.putShort(reserved);
		buffer.putShort(source);
		
		buffer.putInt(range);
		buffer.putInt(reservedInt);
		buffer.putInt(elevation);
		buffer.putInt(strength);
		
		buffer.put(dopplerExtension);
		buffer.put(elevationExtension);
		buffer.put(reservedByte);
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
		messageId = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		messageClass = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		reserved = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		source = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		
		range = (int)bb.getInt(index);index += BYTES_PER_INT;		
		reservedInt = (int)bb.getInt(index);index += BYTES_PER_INT;
		elevation = (short)bb.getInt(index);index += BYTES_PER_INT;
		strength = (int)bb.getInt(index);index += BYTES_PER_INT;

		dopplerExtension = (byte)bb.get(index);index += BYTES;
		elevationExtension = (byte)bb.get(index);index += BYTES;
		reservedByte = (byte)bb.get(index);index += BYTES;
		rangeExtension = (byte)bb.get(index);index += BYTES;
		
		reservedInt = (int)bb.getInt(index);index += BYTES_PER_INT;
		timeStampLow = (int)bb.getInt(index);index += BYTES_PER_INT;
		timeStampHigh = (int)bb.getInt(index);index += BYTES_PER_INT;
	}
	
	@Override
	public String toString() {
		return "AzPlot: "
				+"\n ID:"+messageId
				+"\n Class:"+messageClass
				+"\n Range: "+range
				+"\n El :"+elevation;		
	}
	
}
