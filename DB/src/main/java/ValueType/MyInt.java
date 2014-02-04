package ValueType;

public class MyInt  extends Value {

	private  final  int value;
	
	private  MyInt(int  value){
		
		this.value=value;
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
