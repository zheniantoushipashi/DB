package ValueType;

public class MyBytes extends Value {

	public static final byte[] EMPTY_BYTES = {};
	private byte[] value;

	private MyBytes(byte[] v) {

		this.value = v;
	}

	@Override
	public byte[] getBytesNoCopy() {
		return value;
	}

	@Override
	public byte[] getBytes() {
		return this.cloneByteArray(getBytesNoCopy());
	}

	/**
	 * Create a new byte array and copy all the data. If the size of the byte
	 * array is zero, the same array is returned.
	 * 
	 * @param b
	 *            the byte array (may not be null)
	 * @return a new byte array
	 */
	public static byte[] cloneByteArray(byte[] b) {
		if (b == null) {
			return null;
		}
		int len = b.length;
		if (len == 0) {
			return EMPTY_BYTES;
		}
		byte[] copy = new byte[len];
		System.arraycopy(b, 0, copy, 0, len);
		return copy;
	}

	@Override
	public int getType() {
		return Value.BYTES;
	}

	@Override
	public String getString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

}
