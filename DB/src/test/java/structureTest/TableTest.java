package structureTest;
import java.util.ArrayList;
import DBengine.Database;
import Serializer.DataInputOutput;
import structure.Column;
import structure.Table;
import junit.framework.TestCase;
public class TableTest extends TestCase {
    public void testCreatTable() throws Exception{
    	ArrayList<Column> columns = new ArrayList<Column>();
    	Column  column1 = new Column("name", 1);
    	Column  column2 = new Column("age", 2);
    	columns.add(column1);
    	columns.add(column2);
	    String   testTable = "people";
	    Table  people = new	Table(testTable,columns);
	    String filename = "storeFile5";
		Database database = new Database(filename);
		DataInputOutput inbuf = new DataInputOutput(200);
		DataInputOutput outbuf = new DataInputOutput(200);
		@SuppressWarnings("unchecked")
		long recid = database.insert(people, database.serializer, inbuf);
		@SuppressWarnings("unchecked")
		Table people1 = (Table) database.fetch(recid, database.serializer, outbuf);
		assertEquals(people,people1);
	}
}
