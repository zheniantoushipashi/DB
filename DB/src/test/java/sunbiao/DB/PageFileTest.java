package sunbiao.DB;

import Store.PageBuffer;
import Store.PageFile;
import junit.framework.TestCase;

public class PageFileTest extends TestCase {

	  /**
     * Test addition of record 0
     */
    public  void   testAddCharacter() throws Exception{
    	String  filename="storeFile1";
    	PageFile  file = new PageFile(filename);
    	PageBuffer  data = file.get(0);
    	data.writeByte(14, (byte)'b');
    	file.release(0, true);
    	file.close();
    	PageFile  file1 = new PageFile(filename);
        PageBuffer	  data1 = file1.get(0);
        System.out.println((char)data1.readByte(14));
    
    	assertEquals((byte)'b', data1.readByte(14));
    	file1.release(0, false);
    	file1.close();
    }
      
      
      
}
