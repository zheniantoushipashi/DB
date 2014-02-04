package Store;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.crypto.Cipher;

import ValueType.Value;

public final class PageBuffer {
	private long pageId;
	private ByteBuffer data;
	private boolean dirty = false;
	private int transactionCount = 0;

	public PageBuffer() {
		// empty
	}

	public PageBuffer(long pageId, ByteBuffer data) {
		this.pageId = pageId;
		this.data = data;
	}

	PageBuffer(long pageId, byte[] data) {

		this.pageId = pageId;
		this.data = ByteBuffer.wrap(data);
	}

	void ensureHeapBuffer() {
		if (data.isDirect()) {
			final byte[] bb = new byte[Storage.PAGE_SIZE];
			data.get(bb, 0, Storage.PAGE_SIZE);
			data = ByteBuffer.wrap(bb);
			if (data.isReadOnly())
				throw new InternalError();
		}

	}

	/**
	 * Increments transaction count for this block, to signal that this block is
	 * in the log but not yet in the data file. The method also takes a snapshot
	 * so that the data may be modified in new transactions.
	 */
	void incrementTransactionCount() {
		transactionCount++;
	}

	/**
	 * Decrements transaction count for this block, to signal that this block
	 * has been written from the log to the data file.
	 */
	void decrementTransactionCount() {
		transactionCount--;
		if (transactionCount < 0)
			throw new Error("transaction count on page " + getPageId()
					+ " below zero!");

	}

	public void writeExternal(DataOutput out, Cipher cipherIn)
			throws IOException {
		out.writeLong(pageId);
		out.write(Utils.encrypt(cipherIn, data.array()));
	}

	/**
	 * Returns true if the block is still dirty with respect to the transaction
	 * log.
	 */
	boolean isInTransaction() {
		return transactionCount != 0;
	}

	ByteBuffer getData() {
		return data;
	}

	/**
	 * Returns the page number.
	 */
	long getPageId() {
		return pageId;
	}

	void setDirty() {
		dirty = true;

		if (data.isReadOnly()) {
			// make copy if needed, so we can write into buffer
			byte[] buf = new byte[Storage.PAGE_SIZE];
			data.get(buf, 0, Storage.PAGE_SIZE);
			data = ByteBuffer.wrap(buf);
		}
	}

	void setClean() {
		dirty = false;
	}

	/**
	 * Returns true if the dirty flag is set.
	 */
	boolean isDirty() {
		return dirty;
	}

	/**
	 * Reads a byte from the indicated position
	 */
	public byte readByte(int pos) {
		return data.get(pos);
	}

	/**
	 * Writes a byte to the indicated position
	 */
	public void writeByte(int pos, byte value) {
		setDirty();
		data.put(pos, value);
	}

	/**
	 * Reads a short from the indicated position
	 */
	public short readShort(int pos) {
		return data.getShort(pos);
	}

	/**
	 * Writes a short to the indicated position
	 */
	public void writeShort(int pos, short value) {
		setDirty();
		data.putShort(pos, value);
	}

	/**
	 * Reads an int from the indicated position
	 */
	public int readInt(int pos) {
		return data.getInt(pos);
	}

	/**
	 * Writes an int to the indicated position
	 */
	public void writeInt(int pos, int value) {
		setDirty();
		data.putInt(pos, value);
	}

	/**
	 * Reads a long from the indicated position
	 */
	public long readLong(int pos) {
		return data.getLong(pos);
	}

	/**
	 * Writes a long to the indicated position
	 */
	public void writeLong(int pos, long value) {
		setDirty();
		data.putLong(pos, value);
	}

	public long readSixByteLong(int pos) {
		long ret = ((long) (data.get(pos + 0) & 0x7f) << 40)
				| ((long) (data.get(pos + 1) & 0xff) << 32)
				| ((long) (data.get(pos + 2) & 0xff) << 24)
				| ((long) (data.get(pos + 3) & 0xff) << 16)
				| ((long) (data.get(pos + 4) & 0xff) << 8)
				| ((long) (data.get(pos + 5) & 0xff) << 0);
		if ((data.get(pos + 0) & 0x80) != 0)
			return -ret;
		else
			return ret;

	}

	/**
	 * Writes a long to the indicated position
	 */
	public void writeSixByteLong(int pos, long value) {
		// if(value<0) throw new IllegalArgumentException();
		// if(value >> (6*8)!=0)
		// throw new IllegalArgumentException("does not fit");
		int negativeBit = 0;
		if (value < 0) {
			value = -value;
			negativeBit = 0x80;
		}

		setDirty();
		data.put(pos + 0, (byte) ((0x7f & (value >> 40)) | negativeBit));
		data.put(pos + 1, (byte) (0xff & (value >> 32)));
		data.put(pos + 2, (byte) (0xff & (value >> 24)));
		data.put(pos + 3, (byte) (0xff & (value >> 16)));
		data.put(pos + 4, (byte) (0xff & (value >> 8)));
		data.put(pos + 5, (byte) (0xff & (value >> 0)));

	}

	// overrides java.lang.Object

	public String toString() {
		return "PageIo(" + pageId + "," + dirty + ")";
	}

	public void readExternal(DataInputStream in, Cipher cipherOut)
			throws IOException {
		pageId = in.readLong();
		byte[] data2 = new byte[Storage.PAGE_SIZE];
		in.readFully(data2);
		data = ByteBuffer.wrap(data2);

	}

	public byte[] getByteArray() {
		if (data.hasArray())
			return data.array();
		byte[] d = new byte[Storage.PAGE_SIZE];
		data.rewind();
		data.get(d, 0, Storage.PAGE_SIZE);
		return d;
	}

	public void writeByteArray(byte[] buf, int srcOffset, int offset, int length) {
		setDirty();
		data.rewind();
		data.position(offset);
		data.put(buf, srcOffset, length);
	}

	 private static int getVarIntLen(int x) {
	        if ((x & (-1 << 7)) == 0) {
	            return 1;
	        } else if ((x & (-1 << 14)) == 0) {
	            return 2;
	        } else if ((x & (-1 << 21)) == 0) {
	            return 3;
	        } else if ((x & (-1 << 28)) == 0) {
	            return 4;
	        }
	        return 5;
	    }
	
	public int getValueLen(Value v) {
		switch (v.getType()) {
		case Value.BOOLEAN:
			return 1;
		case Value.BYTE:
			return 2;
     
		case Value.INT: {
			int x = v.getInt();
			if (x < 0) {
				return 1 + getVarIntLen(-x);
			} else if (x < 16) {
				return 1;
			} else {
				return 1 + getVarIntLen(x);
			}
		}
		
		
		case Value.STRING: {
			String s = v.getString();
			int lenth = s.length();
			return legth;

		}
		
	
	      default:
	    	 return -1;
		}
	}

}
