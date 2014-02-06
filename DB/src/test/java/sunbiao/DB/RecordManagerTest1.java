package sunbiao.DB;

import Store.PageBuffer;
import Store.PageFile;
import Store.RecordManager;
import junit.framework.TestCase;

public class RecordManagerTest1 extends TestCase {

	public void testInsert() throws Exception {
		String filename = "storeFile1";
		PageFile file = new PageFile(filename);
		PageBuffer data = file.get(1);
		RecordManager recordManager = new RecordManager(data);
		int one = recordManager.insert("sunbiaobiao".getBytes(),
				"sunbiaobiao".getBytes().length);
		System.out.println(one);
		int two = recordManager.insert("panmiaomaiomiao".getBytes(),
				"panmiaomiao".getBytes().length);
		System.out.println(two);
		file.release(1, true);
		file.close();
		PageFile  file1 = new PageFile(filename);
        PageBuffer	data1 = file1.get(1);
       	file1.release(1, false);
    	file1.close();
	}
}
