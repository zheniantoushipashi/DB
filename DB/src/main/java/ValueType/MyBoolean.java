package ValueType;

public class MyBoolean extends Value {

	private final Boolean value;

	private MyBoolean(boolean value) {
		this.value = Boolean.valueOf(value);
	}
	@Override
    public Boolean getBoolean() {
        return value;
    }

	@Override
	public int getType() {
		return Value.BOOLEAN;
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
