package sunbiao.DB;

import Store.RecordManager;
import junit.framework.TestCase;

public class RowNumMapTest extends TestCase {

  public void    testRowNum() throws Exception{
	    String filename = "storeFile3";
	    RecordManager recordManager = new RecordManager(filename);
	    int RowNum1 = recordManager.insert("sunbiaobiao".getBytes());
	    int CurrentPageNum =  recordManager.getRowNumMap().FindPageIdByRowNum(RowNum1);
		int CurrentRecordNum =  recordManager.getRowNumMap().FindRecordIdByRowNum(RowNum1); 
		assertEquals(CurrentPageNum, 2);
		assertEquals(CurrentRecordNum, 4080);
		int RowNum2 = recordManager.insert("panmiaomiaomiao".getBytes());
		CurrentPageNum =  recordManager.getRowNumMap().FindPageIdByRowNum(RowNum2);
	    CurrentRecordNum =  recordManager.getRowNumMap().FindRecordIdByRowNum(RowNum2); 
		assertEquals(CurrentPageNum, 2);
		assertEquals(CurrentRecordNum, 4072);
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
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		
		int  RowNum19 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		CurrentPageNum =  recordManager.getRowNumMap().FindPageIdByRowNum(RowNum19);
	    CurrentRecordNum =  recordManager.getRowNumMap().FindRecordIdByRowNum(RowNum19); 
	    assertEquals(CurrentPageNum, 2);
		assertEquals(CurrentRecordNum, 3936);
		int  RowNum20 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
	    int  RowNum21 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		CurrentPageNum =  recordManager.getRowNumMap().FindPageIdByRowNum(RowNum21);
	    CurrentRecordNum =  recordManager.getRowNumMap().FindRecordIdByRowNum(RowNum21); 
	    assertEquals(CurrentPageNum, 2);
		assertEquals(CurrentRecordNum, 3920);
		int RowNum22 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
	
		CurrentPageNum =  recordManager.getRowNumMap().FindPageIdByRowNum(RowNum22);
	    CurrentRecordNum =  recordManager.getRowNumMap().FindRecordIdByRowNum(RowNum22); 
	    assertEquals(CurrentPageNum, 3);
		assertEquals(CurrentRecordNum, 4080);
		int i = recordManager.getRowNumMap().getLastRowNum();
		assertEquals(22, recordManager.getRowNumMap().getLastRowNum());
	  
  }
}
