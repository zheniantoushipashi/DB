package DBengine;
abstract public class  DbObject {

	 /**
     * The object is of the type table or view.
     */
   public static  int TABLE_OR_VIEW = 0;

    /**
     * This object is an index.
     */
    int INDEX = 1;

    /**
     * This object is a user.
     */
    int USER = 2;

    /**
     * This object is a sequence.
     */
    int SEQUENCE = 3;

    /**
     * This object is a trigger.
     */
    int TRIGGER = 4;

    /**
     * This object is a constraint (check constraint, unique constraint, or
     * referential constraint).
     */
    int CONSTRAINT = 5;

    /**
     * This object is a setting.
     */
    int SETTING = 6;

    /**
     * This object is a role.
     */
    int ROLE = 7;

    /**
     * This object is a right.
     */
    int RIGHT = 8;

    /**
     * This object is an alias for a Java function.
     */
    int FUNCTION_ALIAS = 9;

    /**
     * This object is a schema.
     */
    int SCHEMA = 10;

    /**
     * This object is a constant.
     */
    int CONSTANT = 11;

    /**
     * This object is a user data type (domain).
     */
    int USER_DATATYPE = 12;

    /**
     * This object is a comment.
     */
    int COMMENT = 13;

    /**
     * This object is a user-defined aggregate function.
     */
    int AGGREGATE = 14;

    /**
     * Get the SQL name of this object (may be quoted).
     *
     * @return the SQL name
     */
    
  
    
    

    
    
  
    /**
     * Get the object type.
     *
     * @return the object type
     */
    abstract  public  int getType();

  
  

    public void rename(String newName) {
        objectName = newName;
    }
   

    
   
    public Database getDatabase() {
        return database;
    }

    
    public int getId() {
        return id;
    }

    
    public String getName() {
        return objectName;
    }
    
    /**
     * The database.
     */
    protected Database database;



    private int id;
    protected String objectName;
    private long modificationId;
    private boolean temporary;

    /**
     * Initialize some attributes of this object.
     *
     * @param db the database
     * @param objectId the object id
     * @param name the name
     * @param traceModule the trace module name
     */
    protected void initDbObjectBase(Database db, int objectId, String name ) {
        this.database = db;
        this.id = objectId;
        this.objectName = name;
    }
}
