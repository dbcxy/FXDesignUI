package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import messages.utils.IByteSum;

public class AzimuthPlaneTrackMsg implements Serializable, IByteSum{
		
	private static final long serialVersionUID = 1L;
	final static int MSG_SIZE = 72;//Allocating 18*4
	
	private int messageHeader;
	private int trackName;
	private short trackQuality;
	private short trackStatus;
	private int reserved;
	private int x;
	private int y;
	private int xVelocity;
	private int yVelocity;
	
	private int xposVariance;
	private int yposVariance;
	private int xvelocityVariance;
	private int yvelocityVariance;

	private int timeStampLow;
	private int timeStampHigh;
	
	public AzimuthPlaneTrackMsg() {
		// TODO Auto-generated constructor stub
	}

	public int getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(int messageClass) {
		this.messageHeader = messageClass;
	}

	public int getTrackName() {
		return trackName;
	}

	public void setTrackName(int trackName) {
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
	
	public void setYposVariance(int yposVariance) {
		this.yposVariance = yposVariance;
	}
	
	public int getYposVariance() {
		return yposVariance;
	}
	
	public int getXvelocityVariance() {
		return xvelocityVariance;
	}

	public void setXvelocityVariance(int xvelocityVariance) {
		this.xvelocityVariance = xvelocityVariance;
	}
	
	public int getYvelocityVariance() {
		return yvelocityVariance;
	}

	public void setYvelocityVariance(int yvelocityVariance) {
		this.yvelocityVariance = yvelocityVariance;
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

		ByteBuffer buffer = ByteBuffer.allocate(MSG_SIZE);
		buffer.putInt(messageHeader);
		buffer.putInt(trackName);

		buffer.putShort(trackStatus);
		buffer.putShort(trackQuality);
		
		buffer.putInt(reserved);
		buffer.putInt(x);
		buffer.putInt(y);
		
		buffer.putInt(reserved);
		buffer.putInt(xVelocity);
		buffer.putInt(yVelocity);
		
		buffer.putInt(reserved);
		buffer.putInt(xposVariance);
		buffer.putInt(yposVariance);
		
		buffer.putInt(reserved);
		buffer.putInt(xvelocityVariance);
		buffer.putInt(yvelocityVariance);
		
		buffer.putInt(reserved);
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
		trackName = (int)bb.getInt(index);index += BYTES_PER_INT;
		
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
		
		xvelocityVariance = (int)bb.getInt(index);index += BYTES_PER_INT;
		yvelocityVariance = (int)bb.getInt(index);index += BYTES_PER_INT;		
		reserved = (int)bb.getInt(index);index += BYTES_PER_INT;
		
		timeStampLow = (int)bb.getInt(index);index += BYTES_PER_INT;
		timeStampHigh = (int)bb.getInt(index);index += BYTES_PER_INT;
		
	}
	
	@Override
	public String toString() {
		return "AzTrack: "
				+"\n Header:"+messageHeader
				+"\n Tname:"+trackName
				+"\n X: "+x
				+"\n Y :"+y;		
	}
}
