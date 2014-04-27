package metaObject;

import index.Index;

import java.util.HashMap;

import DBengine.Database;

public class schema  extends Dbobjectimpl{
     private  final  HashMap<String, Table> table;
     private  final  HashMap<String, Index> indexes;
	public int getType() {
		return Dbobject.schema;
	}
	
	
    public  schema(Database database, int id, String schemaName){
    	initDbObject(database, id, schemaName);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
