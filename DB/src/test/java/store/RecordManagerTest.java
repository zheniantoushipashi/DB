package store;
import Store.RecordManager;
import junit.framework.TestCase;

public class RecordManagerTest extends TestCase {
	   
	public void testGetRecordById() throws Exception {
		RecordManager recordManager = new RecordManager();
		
	//	int rowOne = recordManager.insert("sunbiaobiao".getBytes());
		assertEquals(new String(recordManager.fetch(1)),"sunbiaobiao" );
		
		
	 //   int  rowTwo = recordManager.insert("panmiaomiaomiao".getBytes());
	    assertEquals(new String(recordManager.fetch(2)),"panmiaomiaomiao" );
	    /*
	    int rowThree = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		int RowNum15 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		
		int RowNum16 = recordManager.insert("aaaaa".getBytes());
		
		assertEquals(new String(recordManager.fetch(16)),"aaaaa" );
		
		int RowNum17 = recordManager.insert("you are a little dog".getBytes());
		*/
		assertEquals(new String(recordManager.fetch(17)),"you are a little dog" );
		assertEquals(new String(recordManager.fetch(15)),"panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf");
	
		//recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		//int RowNum19 = recordManager.insert("19panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		
		
	
	
		
		
		//recordManager.commitAll();
		
		
	}
	
	/*

	public  void testDisplayAllRow() throws Exception{
	    RecordManager recordManager = new RecordManager();
		recordManager.insert("sunbiaobiao".getBytes());
		recordManager.insert("panmiaomiaomiao".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		int RowNum10 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		int RowNum15 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		int RowNum16 = recordManager.insert("aaaaa".getBytes());
		int RowNum17 = recordManager.insert("you are a little dog ".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		int RowNum19 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.delete(RowNum16);
		assertEquals(new String(recordManager.fetch(RowNum16)),"you are a little dog " );
		
		recordManager.delete(1);
		assertEquals(new String(recordManager.fetch(15)),"you are a little dog " );
		assertEquals(new String(recordManager.fetch(1)),"panmiaomiaomiao" );
		
		recordManager.releaseClose();
	}
	
	public  void  testUpdate() throws Exception{
	    RecordManager recordManager = new RecordManager();
		recordManager.insert("sunbiaobiao".getBytes());
		recordManager.insert("panmiaomiaomiao".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		int RowNum10 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		int RowNum15 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		int RowNum16 = recordManager.insert("aaaaa".getBytes());
		int RowNum17 = recordManager.insert("you are a little dog ".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		int RowNum19 = recordManager.insert("cccccc".getBytes());
		recordManager.update(RowNum16, "bbbb".getBytes());
		assertEquals(new String(recordManager.fetch(RowNum16)),"bbbb" );
		recordManager.update(RowNum19, "我把第十九行修改为现在的这一句话".getBytes());
		assertEquals(new String(recordManager.fetch(RowNum19)),"我把第十九行修改为现在的这一句话" );
		recordManager.update(1, "sunbiaobiao我把第1行修改为现在的这一句话".getBytes());
		assertEquals(new String(recordManager.fetch(1)),"sunbiaobiao我把第1行修改为现在的这一句话" );
		recordManager.displayAllRow();	
	}

    */
}
