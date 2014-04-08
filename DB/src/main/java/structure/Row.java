package structure;
import Store.PageBuffer;
import ValueType.Value;
public class Row implements  SearchRow {
    private  long  key;
    private  Value  searchColumn;
    public Value getSearchColumn() {
		return searchColumn;
	}
	public void setSearchColumn(Value searchColumn) {
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
    public  int  getColumnCount(){
    	return data.length;
    }
    
    public boolean isEmpty() {
        return data == null;
    }
    
    public boolean isDeleted() {
        return deleted;
    }

    public Value[] getValueList() {
        return data;
    }
	public Value getValue(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	public void setValue(int index, Value v) {
		// TODO Auto-generated method stub
		
	}
	public void setKeyAndVersion(SearchRow old) {
		// TODO Auto-generated method stub
		
	}
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}
	public int getMemory() {
		// TODO Auto-generated method stub
		return 0;
	}
    
    
}
