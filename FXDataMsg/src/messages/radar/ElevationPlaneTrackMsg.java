package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import messages.utils.IByteSum;

public class ElevationPlaneTrackMsg implements Serializable,IByteSum {
	
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
	private int z;
	private int xVelocity;
	private int zVelocity;
	private int xposVariance;
	private int zposVariance;
	private int xvelocityVariance;
	private int zvelocityVariance;
	private int timeStampLow;
	private int timeStampHigh;
	
	public ElevationPlaneTrackMsg() {
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
		return z;
	}

	public void setY(int y) {
		this.z = y;
	}

	public int getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(int xVelocity) {
		this.xVelocity = xVelocity;
	}

	public int getXposVariance() {
		return xposVariance;
	}

	public void setXposVariance(int xposVariance) {
		this.xposVariance = xposVariance;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getzVelocity() {
		return zVelocity;
	}

	public void setzVelocity(int zVelocity) {
		this.zVelocity = zVelocity;
	}

	public int getZposVariance() {
		return zposVariance;
	}

	public void setZposVariance(int zposVariance) {
		this.zposVariance = zposVariance;
	}

	public int getXvelocityVariance() {
		return xvelocityVariance;
	}

	public void setXvelocityVariance(int xvelocityVariance) {
		this.xvelocityVariance = xvelocityVariance;
	}

	public int getZvelocityVariance() {
		return zvelocityVariance;
	}

	public void setZvelocityVariance(int zvelocityVariance) {
		this.zvelocityVariance = zvelocityVariance;
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
		buffer.putShort(messageId);
		buffer.putShort(messageClass);

		buffer.putShort(trackName);
		buffer.putShort(source);

		buffer.putShort(trackStatus);
		buffer.putShort(trackQuality);
		
		buffer.putInt(reserved);
		buffer.putInt(x);
		buffer.putInt(z);
		buffer.putInt(reserved);
		buffer.putInt(xVelocity);
		buffer.putInt(zVelocity);
		buffer.putInt(reserved);
		buffer.putInt(xposVariance);
		buffer.putInt(zposVariance);
		buffer.putInt(reserved);
		buffer.putInt(xvelocityVariance);
		buffer.putInt(zvelocityVariance);
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
		
		trackName = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		source = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		
		trackStatus = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		trackQuality = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		
		reserved = (int)bb.getInt(index);index += BYTES_PER_INT;
		x = (int)bb.getInt(index);index += BYTES_PER_INT;
		z = (int)bb.getInt(index);index += BYTES_PER_INT;
		reserved = (int)bb.getInt(index);index += BYTES_PER_INT;
		xVelocity = (int)bb.getInt(index);index += BYTES_PER_INT;
		zVelocity = (int)bb.getInt(index);index += BYTES_PER_INT;
		reserved = (int)bb.getInt(index);index += BYTES_PER_INT;
		xposVariance = (int)bb.getInt(index);index += BYTES_PER_INT;
		zposVariance = (int)bb.getInt(index);index += BYTES_PER_INT;
		reserved = (int)bb.getInt(index);index += BYTES_PER_INT;
		xvelocityVariance = (int)bb.getInt(index);index += BYTES_PER_INT;
		zvelocityVariance = (int)bb.getInt(index);index += BYTES_PER_INT;
		timeStampLow = (int)bb.getInt(index);index += BYTES_PER_INT;
		timeStampHigh = (int)bb.getInt(index);index += BYTES_PER_INT;
	}
	
	@Override
	public String toString() {
		return "ElTrack: "
				+"\n ID:"+messageId
				+"\n Class:"+messageClass
				+"\n X: "+x
				+"\n Z :"+z;		
	}
}

