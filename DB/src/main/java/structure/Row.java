package structure;
//代表数据库表格的的一行

public class Row {
    private  long  key;
    private  String  searchColumn;
    private  final  Value[]  data;
    private  boolean  deleted;
    public   Row(Value[] data){
    	
    	this.data=data;
    }
    
    
	
	
}
