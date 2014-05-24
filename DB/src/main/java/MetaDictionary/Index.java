package MetaDictionary;

public class Index {

	private  String  name;
	public String getName() {
		return name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTable_ID() {
		return Table_ID;
	}
	public void setTable_ID(int table_ID) {
		Table_ID = table_ID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setName(String name) {
		this.name = name;
	}
	private   int    id;
	private    int   Table_ID;
	private   int  type;
	
}
