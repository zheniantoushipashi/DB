package ValueType;
import Util.MathUtils;

public class Int extends  Value{

	private final int value;
	
	private Int(int value) {
        this.value = value;
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
