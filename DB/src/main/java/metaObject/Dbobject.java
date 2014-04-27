package metaObject;

import DBengine.Database;

public interface Dbobject {
	int table  = 0;
	int index  = 1;
	int schema = 2;
	 /**
     * Get the database.
     *
     * @return the database
     */
    Database getDatabase();
    /**
     * Get the unique object id.
     *
     * @return the object id
     */
    int getId();
    /**
     * Get the name.
     *
     * @return the name
     */
    String getName();
    /**
     * Get the object type.
     *
     * @return the object type
     */
    int getType();

}
