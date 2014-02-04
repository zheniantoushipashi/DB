package ValueType;
public abstract class Value {

	public static final int NULL = 0;

	/**
	 * The value type for BOOLEAN values.
	 */
	public static final int BOOLEAN = 1;

	/**
	 * The value type for BYTE values.
	 */
	public static final int BYTE = 2;

	public static final int INT = 4;

	public static final int DATE = 10;
	/**
	 * The value type for BYTES values.
	 */
	public static final int BYTES = 12;

	/**
	 * The value type for STRING values.
	 */
	public static final int STRING = 13;

	public abstract int getType();

	public abstract java.lang.String getString();

	public int getSignum() {
		return 0;
	}

	/**
	 * Get the value as an object.
	 * 
	 * @return the object
	 */
	public abstract Object getObject();

	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object other);

	static int getOrder(int type) {
		switch (type) {
		case NULL:
			return 2;
		case STRING:
			return 10;
		case BOOLEAN:
			return 20;
		case BYTE:
			return 21;
		case DATE:
			return 31;

		case BYTES:
			return 40;
		default:
			return -1;
		}
	}

	public static int getHigherOrder(int t1, int t2) {
		if (t1 == t2) {
			return t1;
		}
		int o1 = getOrder(t1);
		int o2 = getOrder(t2);
		return o1 > o2 ? t1 : t2;
	}

	public int getInt() {
		return ((MyInt) convertType(Value.INT)).getInt();
	}

	public byte[] getBytesNoCopy() {
		return ((MyBytes) convertType(Value.BYTES)).getBytesNoCopy();
	}

	public byte[] getBytes() {
		return ((MyBytes) convertType(Value.BYTES)).getBytes();
	}

	public byte getByte() {
		return ((MyByte) convertType(Value.BYTE)).getByte();
	}

	public Boolean getBoolean() {
		return ((MyBoolean) convertType(Value.BOOLEAN)).getBoolean();
	}

	public Value convertType(int TargetType) {
		if (getType() == TargetType) {
			return this;
		}
		switch (TargetType) {
		case BOOLEAN: {
			switch (getType()) {
			case INT:
			case BYTE:
				return MyBoolean.get(getSignum() != 0);
			}
		}
		case BYTE: {
			switch (getType()) {
			case BOOLEAN:
				return MyByte.get(getBoolean().booleanValue() ? (byte) 1
						: (byte) 0);
			case INT:
				return MyByte.get(convertToByte(getInt()));
			}

		}
		case INT: {
            switch (getType()) {
            case BOOLEAN:
                return MyInt.get(getBoolean().booleanValue() ? 1 : 0);
            case BYTE:
                return MyInt.get(getByte());
            case BYTES:
                return MyInt.get((int) Long.parseLong(getString(), 16));
            }
            break;
        }  
		}
		return null;

	}

	private static byte convertToByte(long x) {
		if (x > Byte.MAX_VALUE || x < Byte.MIN_VALUE) {

		}
		return (byte) x;
	}

}
