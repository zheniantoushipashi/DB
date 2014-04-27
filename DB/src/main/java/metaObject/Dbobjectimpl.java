package metaObject;

import DBengine.Database;
public  abstract class Dbobjectimpl implements Dbobject{

	 /**
     * The database.
     */
    protected Database database;
    private  int  id;
    private String  name;
    
 
    protected  void  initDbObject(Database db, int objectId, String name){
    	this.database = db;
    	this.id = objectId;
    	this.name = name;
    }
    
  

	public Database getDatabase() {
		return  database;
	}

	public String getName() {
		return name;
	}

	

    public int getId() {
        return id;
    }
}
