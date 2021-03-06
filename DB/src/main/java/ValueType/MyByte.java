package ValueType;
public class MyByte extends Value {

	@Override
	public int getType() {
		return Value.BYTE;
	}

	private final byte value;

	private MyByte(byte value) {
		this.value = value;
	}

	@Override
	public byte getByte() {
		return value;
	}

	public static MyByte get(byte i) {
		return new MyByte(i);
	}

	@Override
	public String getString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSignum() {
		return Integer.signum(value);
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
