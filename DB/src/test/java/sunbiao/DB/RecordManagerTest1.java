package sunbiao.DB;

import Store.PageBuffer;
import Store.PageFile;
import Store.RecordManager;
import junit.framework.TestCase;

public class RecordManagerTest1 extends TestCase {
/*
	public void testInsertMethord() throws Exception {
		String filename = "storeFile2";
		PageFile file = new PageFile(filename);
		PageBuffer data = file.get(1);
		RecordManager recordManager = new RecordManager(data);
		int one = recordManager.insert("sunbiaobiao".getBytes(),
				"sunbiaobiao".getBytes().length);
		System.out.println(one);
		int two = recordManager.insert("panmiaomiaomiao".getBytes(),
				"panmiaomiaomiao".getBytes().length);
		System.out.println(two);
		file.release(1, true);
		file.close();

	}
	
*/	

	public void testGetRecordById() throws Exception {
		String filename = "storeFile3";
		PageFile file = new PageFile(filename);
		PageBuffer data = file.get(1);
		/*
		RecordManager recordManager = new RecordManager(data);
		int RecordoneId = recordManager.insert("sunbiaobiao".getBytes(),
				"sunbiaobiao".getBytes().length);
		int RecordtwoId = recordManager.insert("panmiaomiaomiao".getBytes(),
				"panmiaomiaomiao".getBytes().length);
		assertEquals(new String(recordManager.getRecordById(RecordtwoId)), "panmiaomiaomiao");
	    file.release(1, true);
		file.close();
		*/
	}

}
