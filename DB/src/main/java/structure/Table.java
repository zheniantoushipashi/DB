package structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import ValueType.Value;
import DBengine.DbObject;
public class Table extends DbObject implements Serializable {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * The table type that means this table is a regular persistent table.
     */
    public static final int TYPE_CACHED = 0;

    /**
     * The table type that means this table is a regular persistent table.
     */
    public static final int TYPE_MEMORY = 1;

    /**
     * The table type name for linked tables.
     */
    public static final String TABLE_LINK = "TABLE LINK";

    /**
     * The table type name for system tables.
     */
    public static final String SYSTEM_TABLE = "SYSTEM TABLE";

    /**
     * The table type name for regular data tables.
     */
    public static final String TABLE = "TABLE";

    /**
     * The table type name for views.
     */
    public static final String VIEW = "VIEW";

    /**
     * The table type name for external table engines.
     */
    public static final String EXTERNAL_TABLE_ENGINE = "EXTERNAL";

    /**
     * The columns of this table.
     */
    protected Column[] columns;

    /**
     * The compare mode used for this table.
     */
   
    protected boolean  isHidden;
    
    private  final HashMap<String, Column> columnMap = new HashMap<String, Column>();
    private Column rowIdColumn;
    
    
    public  Table(String name, ArrayList<Column> columns) {
      this.objectName = name;
      Column[] cols = new Column[columns.size()];
      columns.toArray(cols);
      setColumns(cols);
    }
    
    
    public boolean canDrop() {
        return true;
    }
    
    
    public Column[] getColumns() {
        return columns;
    }
    
    
    public Column getRowIdColumn() { //ROWID伪列，columnId是-1
        if (rowIdColumn == null) {
            rowIdColumn = new Column(Column.ROWID, Value.INT);
            rowIdColumn.setTable(this, -1);
        }
        return rowIdColumn;
    }
    
    
    protected void setColumns(Column[] columns) {
        this.columns = columns;
        if (columnMap.size() > 0) {
            columnMap.clear();
        }
        for (int i = 0; i < columns.length; i++) {
            Column col = columns[i];
            int dataType = col.getType();
          
            col.setTable(this, i);
            String columnName = col.getName();
           
            columnMap.put(columnName, col);
        }
    }
    
    /**
     * Rename a column of this table.
     *
     * @param column the column to rename
     * @param newName the new column name
     */
    public void renameColumn(Column column, String newName) {
        for (Column c : columns) {
            if (c == column) {
                continue;
            }
          
        }
        columnMap.remove(column.getName());
        column.rename(newName);
        columnMap.put(newName, column);
    }
    
    @Override
    public int getType() {
        return DbObject.TABLE_OR_VIEW;
    }
    
    
    /**
     * Get the column at the given index.
     *
     * @param index the column index (0, 1,...)
     * @return the column
     */
    public Column getColumn(int index) {
        return columns[index];
    }

    

    /**
     * Get the column with the given name.
     *
     * @param columnName the column name
     * @return the column
     * @throws DbException if the column was not found
     */
    public Column getColumn(String columnName) {
        Column column = columnMap.get(columnName);
      
        return column;
    }
    
    public boolean equals(Object o){
    	
    	if(this == o){
    		return  true;
    	}
    	if(o == null || getClass() != o.getClass()){
    		return false;
    	}
    	Table e = (Table)o;
    	for(int i = 0; i < this.columns.length; i++){
    		if(this.columns[i].equals(e.columns[i]))
    			return false;
    	}
    	
    	return  true;
    }
    
   
	
    
    
   
}
