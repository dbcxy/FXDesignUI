package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class AzimuthPlaneTrackMsg implements Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	final static int MSG_SIZE = 72;//Allocating
//	ByteBuffer buffer = ByteBuffer.allocate(MSG_SIZE);
//
//	public AzimuthPlaneTrackMsg() {
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
//	public void putTrackName(short val) {
//		buffer.putShort(val);
//	}
//	
//	public void putTrackQuality(short val) {
//		buffer.putShort(val);
//	}
//	
//	public void putTrackStatus(short val) {
//		buffer.putShort(val);
//	}
//	
//	public void putReserved(int val) {
//		buffer.putInt(val);
//	}
//	
//	public void putX(int val) {
//		buffer.putInt(val);
//	}
//	
//	public void putY(int val) {
//		buffer.putInt(val);
//	}
//	
//	public void putXvelocity(int val) {
//		buffer.putInt(val);
//	}
//	
//	public void putYvelocity(int val) {
//		buffer.putInt(val);
//	}
//	
//	public void putXPosVariance(int val) {
//		buffer.putInt(val);
//	}
//	
//	public void putYPosVariance(int val) {
//		buffer.putInt(val);
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
		
}
