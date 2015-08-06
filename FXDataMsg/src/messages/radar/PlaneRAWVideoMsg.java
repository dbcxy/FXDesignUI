package messages.radar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import messages.utils.IByteSum;

public class PlaneRAWVideoMsg implements Serializable, IByteSum{

	private static final long serialVersionUID = 1L;
	private int msgLen;
	private int messageHeader;
	private int reservedInt;
	private short messageCounter;
	private short reservedShort;
	private int timeStampLow;
	private int timeStampHigh;
	
	private int internalData0;
	private int internalData1;
	private int internalData2;
	private int internalData3;
	private int internalData4;
	
	private int[] reservedIntArr = new int[20];
	private short noRangeCells;
	private byte azBPN;
	private byte elBPN;
	private List<Byte> RC = new ArrayList<Byte>();
	
	public PlaneRAWVideoMsg() {

	}
	
	public int getMsgLen() {
		return msgLen;
	}

	public void setMsgLen(int msgLen) {
		this.msgLen = msgLen;
	}

	public int getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(int messageClass) {
		this.messageHeader = messageClass;
	}

	public int getReservedInt() {
		return reservedInt;
	}

	public void setReservedInt(int reservedInt) {
		this.reservedInt = reservedInt;
	}

	public short getMessageCounter() {
		return messageCounter;
	}

	public void setMessageCounter(short messageCounter) {
		this.messageCounter = messageCounter;
	}

	public short getReservedShort() {
		return reservedShort;
	}

	public void setReservedShort(short reservedShort) {
		this.reservedShort = reservedShort;
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

	public int[] getReservedIntArr() {
		return reservedIntArr;
	}

	public void setReservedIntArr(int[] reservedIntArr) {
		this.reservedIntArr = reservedIntArr;
	}

	public short getNoRangeCells() {
		return noRangeCells;
	}

	public void setNoRangeCells(short noRangeCells) {
		this.noRangeCells = noRangeCells;
	}

	public int getAzBPN() {
		return (azBPN & 0xFF);
	}

	public void setAzBPN(byte azBPN) {
		this.azBPN = azBPN;
	}

	public int getElBPN() {
		return (elBPN & 0xFF);
	}

	public void setElBPN(byte elBPN) {
		this.elBPN = elBPN;
	}
	
	public Byte getRangeCellList(int index) {
		return RC.get(index);
	}

	public void setRangeCellList(
			List<Byte> rc) {
		this.RC = rc;
	}
	
	public void addRangeCell(Byte rc) {
		this.RC.add(rc);
	}
	
	/*
	 * Byte Buffer for Tx
	 */
	public ByteBuffer getByteBuffer() {

		ByteBuffer buffer = ByteBuffer.allocate(msgLen);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(msgLen);
		buffer.putInt(messageHeader);
		buffer.putInt(reservedInt);
		buffer.putShort(reservedShort);
		buffer.putShort(messageCounter);
		buffer.putInt(timeStampHigh);
		buffer.putInt(timeStampLow);
		
		buffer.putInt(internalData0);
		buffer.putInt(internalData1);
		buffer.putInt(internalData2);
		buffer.putInt(internalData3);
		buffer.putInt(internalData4);
		
		for(int i=0;i<reservedIntArr.length;i++)
			buffer.putInt(reservedIntArr[i]);
		
		buffer.putShort(noRangeCells);
		buffer.put(azBPN);
		buffer.put(elBPN);

		for(int j=0;j<RC.size();j++)
			buffer.put((byte)RC.get(j));
		
		return buffer;		
	}

	/*
	 * Byte array to Rx and decode object	
	 */
	public void setByteBuffer(byte[] bArr) {
		ByteBuffer bb = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
		
		int index = 0;
		msgLen = (int)bb.getInt(index);index += BYTES_PER_INT;
		messageHeader = (int)bb.getInt(index);index += BYTES_PER_INT;	
		reservedInt = (int)bb.getInt(index);index += BYTES_PER_INT;
		
		reservedShort = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		messageCounter = (short)bb.getShort(index);index += BYTES_PER_SHORT;

		timeStampHigh = (int)bb.getInt(index);index += BYTES_PER_INT;
		timeStampLow = (int)bb.getInt(index);index += BYTES_PER_INT;
		
		internalData0 = (int)bb.getInt(index);index += BYTES_PER_INT;
		internalData1 = (int)bb.getInt(index);index += BYTES_PER_INT;
		internalData2 = (int)bb.getInt(index);index += BYTES_PER_INT;
		internalData3 = (int)bb.getInt(index);index += BYTES_PER_INT;
		internalData4 = (int)bb.getInt(index);index += BYTES_PER_INT;
		
		for(int i=0;i<reservedIntArr.length;i++) {
			reservedIntArr[i] = (int)bb.getInt(index);
			index += BYTES_PER_INT;
		}
		
		noRangeCells = (short)bb.getShort(index);index += BYTES_PER_SHORT;
		azBPN = (byte)bb.get(index);index += BYTES;
		elBPN = (byte)bb.get(index);index += BYTES;
		
		for(int j=0;j<noRangeCells;j++) {// Doc says BIG ENDIAN
			RC.add((byte)bb.get(index));
			index += BYTES;
		}			
	}
	
	@Override
	public String toString() {
		return "RAWVideo: "
				+"\n Len:"+msgLen
				+"\n Header:"+messageHeader
				+"\n NoRangeCells: "+noRangeCells
				+"\n azBPN :"+azBPN
				+"\n elBPN :"+elBPN;		
	}
	
}
