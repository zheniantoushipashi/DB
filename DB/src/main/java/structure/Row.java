package structure;
import Store.PageBuffer;
import ValueType.Value;
//代表数据库表格的的一行

public class Row {
    private  long  key;
    private  String  searchColumn;
    public String getSearchColumn() {
		return searchColumn;
	}

	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
	}

	private  final  Value[]  data;
    private  boolean  deleted;
    public   Row(Value[] data){
    	this.data=data;
    }
    
    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }
    
    /**
     * Get the number of bytes required for the data.
     *
     * @param dummy the template buffer
     * @return the number of bytes
     */
    public int getByteCount(PageBuffer dummy) {
        int size = 0;
        for (Value v : data) {
            size += dummy.getValueLen(v);
        }
        return size;
    }
    
}
