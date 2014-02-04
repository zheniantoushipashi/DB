package ValueType;

public class MyString  extends  Value {

	private  final String  value;
	@Override
	public int getType() {
		return  Value.STRING;
	}
	
	private  MyString(String  value){
		this.value=value;
	}
	
	@Override
	public String getString() {
		return value;
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
