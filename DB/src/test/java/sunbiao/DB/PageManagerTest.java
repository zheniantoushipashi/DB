package sunbiao.DB;
/*
 * 
 * 一行 197个字符
 * 加8  205
 * 
 * 205 * 19 = 3895
 * 4046 - 3895 = 151
 * 
 * 
 * 
 */

import Store.RecordManager;
import junit.framework.TestCase;

public class PageManagerTest extends TestCase {
	        public  void testPageManager() throws Exception{String filename = "storeFile3";
	        RecordManager recordManager = new RecordManager(filename);
		    int RowNum1 = recordManager.insert("sunbiaobiao".getBytes());
		    int CurrentPageNum =  recordManager.getRowNumMap().FindPageIdByRowNum(RowNum1);
			int CurrentRecordNum =  recordManager.getRowNumMap().FindRecordIdByRowNum(RowNum1); 
			int AvailableSize = recordManager.getPageManager().getAvailableSizeByPageNum(CurrentPageNum);
			assertEquals(AvailableSize, 4069);
			assertEquals(CurrentPageNum, 2);
			assertEquals(CurrentRecordNum, 4080);
			int RowNum2 = recordManager.insert("panmiaomiaomiao".getBytes());
			CurrentPageNum =  recordManager.getRowNumMap().FindPageIdByRowNum(RowNum2);
		    CurrentRecordNum =  recordManager.getRowNumMap().FindRecordIdByRowNum(RowNum2); 
		    AvailableSize = recordManager.getPageManager().getAvailableSizeByPageNum(CurrentPageNum);
		    assertEquals(AvailableSize, 4046);
			assertEquals(CurrentPageNum, 2);
			assertEquals(CurrentRecordNum, 4072);
			int RowNum3 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
			CurrentPageNum =  recordManager.getRowNumMap().FindPageIdByRowNum(RowNum3);
		    CurrentRecordNum =  recordManager.getRowNumMap().FindRecordIdByRowNum(RowNum3); 
		    AvailableSize = recordManager.getPageManager().getAvailableSizeByPageNum(CurrentPageNum);
		    assertEquals(AvailableSize, 3841);
			assertEquals(CurrentPageNum, 2);
			assertEquals(CurrentRecordNum, 4064);
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
			
			int  RowNum20 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
		
			
		    int  RowNum21 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
			CurrentPageNum =  recordManager.getRowNumMap().FindPageIdByRowNum(RowNum21);
		    CurrentRecordNum =  recordManager.getRowNumMap().FindRecordIdByRowNum(RowNum21); 
		    assertEquals(CurrentPageNum, 2);
			assertEquals(CurrentRecordNum, 3920);
			AvailableSize = recordManager.getPageManager().getAvailableSizeByPageNum(CurrentPageNum);
			assertEquals(AvailableSize, 151);
			int RowNum22 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafjlsdjflsajflsdfsjadfljsaljdflsajflasjflsadjljaf".getBytes());
			
			CurrentPageNum =  recordManager.getRowNumMap().FindPageIdByRowNum(RowNum22);
		    CurrentRecordNum =  recordManager.getRowNumMap().FindRecordIdByRowNum(RowNum22); 
		    assertEquals(CurrentPageNum, 3);
			assertEquals(CurrentRecordNum, 4080);
			AvailableSize = recordManager.getPageManager().getAvailableSizeByPageNum(CurrentPageNum);
			assertEquals(AvailableSize, 3883);
		
			int RowNum23 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsdlfjslafj".getBytes());

			CurrentPageNum =  recordManager.getRowNumMap().FindPageIdByRowNum(RowNum23);
		    CurrentRecordNum =  recordManager.getRowNumMap().FindRecordIdByRowNum(RowNum23); 
		    assertEquals(CurrentPageNum, 3);
			assertEquals(CurrentRecordNum, 4072);
			
			int RowNum24 = recordManager.insert("panmiaomiaomiaodjflasjfdsakdfl;dsjfljslfkjslflm kajflkjslfjowjifojflskdlfjslfjljalvljaljfdlsjaflsjfldsjlfjdslfjsdfjsldjflsdjflsajfdljdflajfljsd".getBytes());

			CurrentPageNum =  recordManager.getRowNumMap().FindPageIdByRowNum(RowNum24);
		    CurrentRecordNum =  recordManager.getRowNumMap().FindRecordIdByRowNum(RowNum24); 
		    assertEquals(CurrentPageNum, 2);
			assertEquals(CurrentRecordNum, 3912);
			AvailableSize = recordManager.getPageManager().getAvailableSizeByPageNum(CurrentPageNum);
			assertEquals(AvailableSize, 0);
			
			int i = recordManager.getRowNumMap().getLastRowNum();
			assertEquals(24, recordManager.getRowNumMap().getLastRowNum());
		
	}

}
