package ValueType;
public class MyInt extends Value {

	private final int value;

	private static final int STATIC_SIZE = 128;
	// must be a power of 2
	private static final int DYNAMIC_SIZE = 256;
	private static final MyInt[] STATIC_CACHE = new MyInt[STATIC_SIZE];
	private static final MyInt[] DYNAMIC_CACHE = new MyInt[DYNAMIC_SIZE];

	private MyInt(int value) {

		this.value = value;
	}

	@Override
	public int getInt() {
		return value;
	}

	@Override
	public int getType() {
		return Value.INT;
	}

	@Override
	public int getSignum() {
		return Integer.signum(value);
	}

	/**
	 * Get or create an int value for the given int.
	 * 
	 * @param i
	 *            the int
	 * @return the value
	 */
	public static MyInt get(int i) {
		if (i >= 0 && i < STATIC_SIZE) {
			return STATIC_CACHE[i];
		}
		// 求hashMask，使得新值能放到0到255的下标中
		MyInt v = DYNAMIC_CACHE[i & (DYNAMIC_SIZE - 1)];
		if (v == null || v.value != i) {
			v = new MyInt(i);
			DYNAMIC_CACHE[i & (DYNAMIC_SIZE - 1)] = v;
		}
		return v;
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
