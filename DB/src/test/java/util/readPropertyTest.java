package util;

import java.io.IOException;

import Util.readProperty;
import junit.framework.TestCase;

public class readPropertyTest extends TestCase {
	public  void  testReadProperty() throws IOException{
	   readProperty  readPro = new readProperty();
	   System.out.println(readPro.getDbFilename());
	   assertEquals(readPro.getDbFilename(), "DB");
	}
}
