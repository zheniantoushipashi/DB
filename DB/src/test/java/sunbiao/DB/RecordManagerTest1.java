package sunbiao.DB;

import Store.PageBuffer;
import Store.PageFile;
import Store.RecordManager;
import junit.framework.TestCase;

public class RecordManagerTest1 extends TestCase {

	public void testInsert() throws Exception {

		String filename = "storeFile";
		PageFile file = new PageFile(filename);
		PageBuffer data = file.get(1);
		RecordManager recordManager = new RecordManager(data);
		int one = recordManager.insert("sunbiaobiao".getBytes(),
				"sunbiaobiao".getBytes().length);
		System.out.println(one);
		int two = recordManager.insert("panmiaomaio".getBytes(),
				"panmiaomiao".getBytes().length);
		file.release(1, true);
		file.close();
		
		PageFile  file1 = new PageFile(filename);
        PageBuffer	data1 = file1.get(1);
        System.out.println(data1.readString(data1.getData()));
        System.out.println(data1.readString(data1.getData()).length());
        // assertEquals("aa", data1.readString(data1.getData()));
        //assertEquals((byte) 'b', data.readByte(14));
    	file1.release(1, false);
    	file1.close();

	}
}
