package MetaDictionaryTest;

import MetaDictionary.DictionaryCreate;
import MetaDictionary.Table;
import junit.framework.TestCase;

public class DictionaryCreateTest extends TestCase {
	public  void  testSerializeMetaObject() throws Exception{
		Table tableTest = new  Table();
		tableTest.setId("12323sdfljlsf");
		tableTest.setName("peoplesfdsafasldfj;asjf");
		tableTest.setType(101);
		DictionaryCreate  dictionaryCreate =  new DictionaryCreate();
		Table  table2 = (Table)dictionaryCreate.deserializeMetaRecord(dictionaryCreate.SerializeMetaObject(tableTest));
		assertEquals(tableTest.getId(), table2.getId());
		assertEquals(tableTest.getName(), table2.getName());
		assertEquals(tableTest.getType(), table2.getType());
	}

}
