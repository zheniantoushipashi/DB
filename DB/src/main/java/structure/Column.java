package structure;

import java.sql.ResultSetMetaData;

import ValueType.Value;
public class Column {
    public  static  final  String  ROWID = "_ROWID_";
    public  static  final int NOT_NULLABLE = 1;
    public  static  final  int NULLABLE = 2;
    
    
    private  final  int type;
    private  long  precision;
    private  int  scale;
    private  Table  table;
    private int displaySize;
    private  String name;
    private  int columnId;
    private  boolean  nullable = true;
    private   boolean  autoIncrement;
    
    private long  increment;
    private  boolean  primaryKey;
    
    public  Column(String name, int type){
    	this(name, type, -1, -1, -1);
    }
    
    public Column(String name, int type, long precision, int scale, int displaySize){
    	this.name = name;
        this.type = type;
        
        this.precision = precision;
        this.scale = scale;
        this.displaySize = displaySize; 
    }
    @Override 
    public boolean  equals(Object o){
    	if(o == this){
    		return true;
    	}else if(!(o instanceof Column)){
    		return  false;
    	}
    	Column other = (Column)o;
    	if(table == null || other.table == null || name == null || other.name == null){
    		return  false;
    	}
    	if(table != other.table){
    		return false;
    	}
    	return name.equals(other.name);
    }
    @Override
    public int hashCode() {
        if (table == null || name == null) {
            return 0;
        }
        return table.getId() ^ name.hashCode();
    }

    
    public  Value convert(ValueType.Value v){
    	return  v.convertType(type);
    	
    }
    
    public void setTable(Table table, int columnId) {
        this.table = table;
        this.columnId = columnId;
    }
    
    public Table getTable() {
        return table;
    }
    
    public int getColumnId() {
        return columnId;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public long getPrecision() {
        return precision;
    }

    public void setPrecision(long p) {
        precision = p;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public int getScale() {
        return scale;
    }

    public void setNullable(boolean b) {
        nullable = b;
    }
    
    
    public void rename(String newName) {
        this.name = newName;
    }


}
