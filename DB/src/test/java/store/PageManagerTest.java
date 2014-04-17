package store;

import Store.PageBuffer;
import Store.PageManager;
import junit.framework.TestCase;
public class PageManagerTest extends TestCase {
    public  void   testAddCharacter() throws Exception{
       String  filename="storeFile";
    	PageManager  file = new PageManager(filename);
    	PageBuffer  data = file.get(1);
    	//data.writeByte(14, (byte)'b');
    	data.writeString("asdfslfjldsjflsjflsdj");
    	file.release(1, true);
    	file.close();
    	
    	PageManager  file1 = new PageManager(filename);
        PageBuffer	data1 = file1.get(1);
        System.out.println(data1.readString(data1.getData()));
        System.out.println(data1.readString(data1.getData()).length());
        // assertEquals("aa", data1.readString(data1.getData()));
        //assertEquals((byte) 'b', data.readByte(14));
    	file1.release(1, false);
    	file1.close();
    }
}
