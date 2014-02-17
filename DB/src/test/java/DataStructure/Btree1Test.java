package DataStructure;

import junit.framework.TestCase;

public class Btree1Test extends TestCase {

	public void testInsert() throws Exception{
		BTree1<String, String> btree1 = new BTree1<String, String>(3);
		String key1 = "sunbiaobiao";
		String value1 = "孙彪彪是一个好人";
		String key2 = "panmiaomiao";
		String value2 = "潘苗苗是一个好人呢";
		btree1.insert(key1, value1);
		btree1.insert(key2, value2);
	    assertEquals("孙彪彪是一个好人", btree1.search("sunbiaobiao"));
	    btree1.output();
	    btree1.delete(key1);
	    btree1.output();
	    
	}

}
