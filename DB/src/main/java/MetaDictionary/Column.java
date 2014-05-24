package MetaDictionary;

public class Column {
	private  int Table_ID;
	private   int   pos;
	private     String  name;
	
	// 该列的数据类型
	private    int  dataType;
	
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public int getTable_ID() {
		return Table_ID;
	}
	public void setTable_ID(int table_ID) {
		Table_ID = table_ID;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
