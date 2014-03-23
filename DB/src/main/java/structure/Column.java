package structure;

import java.sql.ResultSetMetaData;
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
    private Sequence  sequence;
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

    public Column getClone() {
        Column newColumn = new Column(name, type, precision, scale, displaySize);
        newColumn.copy(this);
        return newColumn;
    }
    
    public void copy(Column source) {
    	//16个字段，还有7个字段未copy，分别是: type、table、columnId、autoIncrement、start、increment、resolver
        checkConstraint = source.checkConstraint;
        checkConstraintSQL = source.checkConstraintSQL;
        displaySize = source.displaySize;
        name = source.name;
        precision = source.precision;
        scale = source.scale;
        // table is not set
        // columnId is not set
        nullable = source.nullable;
        defaultExpression = source.defaultExpression;
        originalSQL = source.originalSQL;
        // autoIncrement, start, increment is not set
        convertNullToDefault = source.convertNullToDefault;
        sequence = source.sequence;
        comment = source.comment;
        computeTableFilter = source.computeTableFilter;
        isComputed = source.isComputed;
        selectivity = source.selectivity;
        primaryKey = source.primaryKey;
    }
    
    
    public  Value convert(ValueType.Value v){
    	return  v.convertTo(type);
    	
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

    public String getSQL() {
        return Parser.quoteIdentifier(name);
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

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public Sequence getSequence() {
        return sequence;
    }
       

}
