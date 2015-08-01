package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import messages.utils.IByteSum;

public class AzimuthPlaneTrackMsg implements Serializable, IByteSum{
		
	private static final long serialVersionUID = 1L;
	final static int MSG_SIZE = 72;//Allocating
	ByteBuffer buffer = ByteBuffer.allocate(MSG_SIZE);
	
	private short messageClass;
	private short messageId;
	private short source;
	private short trackName;
	private short trackQuality;
	private short trackStatus;
	private int reserved;
	private int x;
	private int y;
	private int xVelocity;
	private int yVelocity;
	private int xposVariance;
	private int yposVariance;
	private int timeStampLow;
	private int timeStampHigh;
	
	public AzimuthPlaneTrackMsg() {
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

	public short getTrackName() {
		return trackName;
	}

	public void setTrackName(short trackName) {
		this.trackName = trackName;
	}

	public short getTrackQuality() {
		return trackQuality;
	}

	public void setTrackQuality(short trackQuality) {
		this.trackQuality = trackQuality;
	}

	public short getTrackStatus() {
		return trackStatus;
	}

	public void setTrackStatus(short trackStatus) {
		this.trackStatus = trackStatus;
	}

	public int getReserved() {
		return reserved;
	}

	public void setReserved(int reserved) {
		this.reserved = reserved;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(int xVelocity) {
		this.xVelocity = xVelocity;
	}

	public int getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(int yVelocity) {
		this.yVelocity = yVelocity;
	}

	public int getXposVariance() {
		return xposVariance;
	}

	public void setXposVariance(int xposVariance) {
		this.xposVariance = xposVariance;
	}

	public int getYposVariance() {
		return yposVariance;
	}

	public void setYposVariance(int yposVariance) {
		this.yposVariance = yposVariance;
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
	 * Byte Buffer for Tx
	 */
	public ByteBuffer getByteBuffer() {
		putMessageId(messageId);
		putMessageClass(messageClass);

		putTrackName(trackName);
		putSource(source);

		putTrackStatus(trackStatus);
		putTrackQuality(trackQuality);
		
		putReserved(reserved);
		putX(x);
		putY(y);
		putReserved(reserved);
		putXvelocity(xVelocity);
		putYvelocity(yVelocity);
		putReserved(reserved);
		putXPosVariance(xposVariance);
		putYPosVariance(yposVariance);
		putReserved(reserved);
		putTimeStampLow(timeStampLow);
		putTimeStampHigh(timeStampHigh);
		
		return buffer;
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
	
	private void putTrackName(short val) {
		buffer.putShort(val);
	}
	
	private void putTrackQuality(short val) {
		buffer.putShort(val);
	}
	
	private void putTrackStatus(short val) {
		buffer.putShort(val);
	}
	
	private void putReserved(int val) {
		buffer.putInt(val);
	}
	
	private void putX(int val) {
		buffer.putInt(val);
	}
	
	private void putY(int val) {
		buffer.putInt(val);
	}
	
	private void putXvelocity(int val) {
		buffer.putInt(val);
	}
	
	private void putYvelocity(int val) {
		buffer.putInt(val);
	}
	
	private void putXPosVariance(int val) {
		buffer.putInt(val);
	}
	
	private void putYPosVariance(int val) {
		buffer.putInt(val);
	}
	
	private void putTimeStampLow(int val) {
		buffer.putInt(val);
	}
	
	private void putTimeStampHigh(int val) {
		buffer.putInt(val);
	}

	/*
	 * Byte array to Rx and decode object	
	 */
	public void setByteBuffer(byte[] bArr) {
		ByteBuffer bb = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
		
		int index = 0;
		messageId = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		messageClass = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		
		trackName = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		source = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		
		trackStatus = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		trackQuality = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		
		reserved = (int)bb.getInt(index);index += BYTES_PER_INT;
		x = (int)bb.getInt(index);index += BYTES_PER_INT;
		y = (int)bb.getInt(index);index += BYTES_PER_INT;
		reserved = (int)bb.getInt(index);index += BYTES_PER_INT;
		xVelocity = (int)bb.getInt(index);index += BYTES_PER_INT;
		yVelocity = (int)bb.getInt(index);index += BYTES_PER_INT;
		reserved = (int)bb.getInt(index);index += BYTES_PER_INT;
		xposVariance = (int)bb.getInt(index);index += BYTES_PER_INT;
		yposVariance = (int)bb.getInt(index);index += BYTES_PER_INT;
		reserved = (int)bb.getInt(index);index += BYTES_PER_INT;
		timeStampLow = (int)bb.getInt(index);index += BYTES_PER_INT;
		timeStampHigh = (int)bb.getInt(index);index += BYTES_PER_INT;
		
	}
}
