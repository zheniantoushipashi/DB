package ValueType;
import Util.MathUtils;

public class Int extends  Value{

	
	private static final int STATIC_SIZE = 128;
	private static final Int[] STATIC_CACHE = new Int[STATIC_SIZE];
	private static final int DYNAMIC_SIZE = 256;
	private static final Int[] DYNAMIC_CACHE = new Int[DYNAMIC_SIZE];
	 /**
     * Get or create an int value for the given int.
     *
     * @param i the int
     * @return the value
     */
    public static Int get(int i) {
        if (i >= 0 && i < STATIC_SIZE) {
            return STATIC_CACHE[i];
        }
        //求hashMask，使得新值能放到0到255的下标中
        Int v = DYNAMIC_CACHE[i & (DYNAMIC_SIZE - 1)];
        if (v == null || v.value != i) {
            v = new Int(i);
            DYNAMIC_CACHE[i & (DYNAMIC_SIZE - 1)] = v;
        }
        return v;
    }
	
	private final int value;
	
	private Int(int value) {
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
	public String getString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject() {
		return value;
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public boolean equals(Object other) {
		   return other instanceof Int && value == ((Int) other).value;
	}

	@Override
	protected int compareSecure(Value o) {
		 Int v = (Int) o;
	        return MathUtils.compareInt(value, v.value);
	}

}
