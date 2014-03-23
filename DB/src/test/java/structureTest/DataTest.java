package structureTest;

import structure.Table;
import DBengine.Database;
import Serializer.DataInputOutput;
import junit.framework.TestCase;

public class DataTest extends TestCase {

	public  void  testData() throws Exception{
		    Data data = new Data(10);
		    String filename = "storeFile5";
			Database database = new Database(filename);
			DataInputOutput inbuf = new DataInputOutput(100);
			DataInputOutput outbuf = new DataInputOutput(100);
			@SuppressWarnings("unchecked")
			long recid = database.insert(data, database.serializer, inbuf);
			@SuppressWarnings("unchecked")
			Data data1 = (Data) database.fetch(recid, database.serializer, outbuf);
			assertEquals(data ,data1);
		
	}
}
