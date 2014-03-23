package structure;

import java.util.ArrayList;
import java.util.HashMap;

import org.h2.constraint.Constraint;
import org.h2.message.Trace;
import org.h2.result.Row;
import org.h2.schema.Schema;
import org.h2.schema.Sequence;
import org.h2.schema.TriggerObject;
import org.h2.table.Column;
import org.h2.table.TableView;

public class Table {
	
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
    
    private  final HashMap<String, Column> columnMap;
    private  final boolean persistIndexes;
    private  final boolean persistData;
    private  ArrayList<TriggerObject> triggers;
    private  ArrayList<Constraint>  constraints;
    private  ArrayList<Sequence>   sequences;
    private  ArrayList<TableView>  views;
    
    
    public  Table(int id, String name, boolean persistIndexes, boolean persistData) {
    
    }
}
