package Store;

import java.io.IOException;
import java.util.Iterator;

import Util.readProperty;

public class RecordManager {
	int PAGE_SIZE_SHIFT = 12;
	/**
	 * the lenght of single page.
	 * <p>
	 * !!! DO NOT MODIFY THI DIRECTLY !!!
	 */
	int PAGE_SIZE = 1 << PAGE_SIZE_SHIFT;
	/*
	 * 在一个页面中指向空闲的空间开始的位置
	 */

	public final int bytesOfInt = 1 << 2;
	public final int FreeSpaceBeginPointer = (PAGE_SIZE - bytesOfInt);
	/*
	 * 空闲记录块的开始位置
	 */

	public final int freeIndexPointer = (PAGE_SIZE - 2 * bytesOfInt);
	/*
	 * 
	 * 一个描述偏移位置和Record大小的记录块的大小 字节
	 */
	public final int offsetSizeIndex = 2 * bytesOfInt;

	private final PageSize pageSize;

	public PageSize getpageSize() {
		return pageSize;
	}

	private RowNumMap rowNumMap;

	public RowNumMap getRowNumMap() {
		return rowNumMap;
	}

	public void setRowNumMap(RowNumMap rowNumMap) {
		this.rowNumMap = rowNumMap;
	}

    PageManager filepageManager;
	
	public PageManager getFilepageManager() {
		return filepageManager;
	}

	public void setFilepageManager(PageManager filepageManager) {
		this.filepageManager = filepageManager;
	}

	readProperty readPro = new readProperty();
	public RecordManager() throws Exception {
		this.filepageManager = new PageManager(readPro.getDbFilename());
		pageSize = new PageSize(filepageManager);
		rowNumMap = new RowNumMap(filepageManager);
	}
	/*
	 * 
	 * 
	 * 插入一条记录
	 */
	public int insert(final byte[] data) throws Exception {		
		int EnoughSpacePageId = pageSize.findEnoughSpacePage(data.length);		
	//	PageBuffer pgB = (filepageManager.inUse.get(EnoughSpacePageId) == null) ? filepageManager
	//			.get(EnoughSpacePageId) : filepageManager.inUse.get(EnoughSpacePageId);
		PageBuffer  pgB = filepageManager.get(EnoughSpacePageId);
		writeHeader(pgB);
		fillRecord(pgB, data);
		int InsertRowNum = rowNumMap.RegisterMapWhenInsert(EnoughSpacePageId,
				findFreeIndex(pgB));
		pageSize.setPageSize(EnoughSpacePageId * 4, getAvailableSize(pgB));
		filepageManager.release(EnoughSpacePageId, true);
		return InsertRowNum;
	}

	void  writeHeader(PageBuffer  pgB){
		pgB.writeInt(freeIndexPointer, PAGE_SIZE - 2 * bytesOfInt);
		pgB.writeInt(FreeSpaceBeginPointer, 0);
	}
    void  fillRecord(PageBuffer pgB , byte[]  data){
    	pgB.writeByteArray(data, 0, this.findFreePosition(pgB), data.length);
		pgB.writeInt(this.findFreeIndex(pgB) - offsetSizeIndex,
				this.findFreePosition(pgB));
		pgB.writeInt(this.findFreeIndex(pgB) - offsetSizeIndex / 2, data.length);
		pgB.writeInt(FreeSpaceBeginPointer, this.findFreePosition(pgB)
				+ data.length);
		pgB.writeInt(freeIndexPointer, this.findFreeIndex(pgB)
				- offsetSizeIndex);	
    }
	public void releaseClose() throws IOException {
		Iterator<PageBuffer> iter = filepageManager.inUse.valuesIterator();
		while (iter.hasNext()) {
			filepageManager.release(iter.next().getPageId(), true);
		}
		filepageManager.close();
	}

	/*
	 * 
	 * 删除一条记录 删除记录时压缩页面
	 */
	public void delete(int RowNum) throws Exception {
		int currentPageNum = rowNumMap.FindPageIdByRowNum(RowNum);
		int currentRecordNum = rowNumMap.FindRecordIdByRowNum(RowNum);
		rowNumMap.shiftMap(RowNum);
		// todu 等以后实现压缩算法时 统一压缩
	}

	/*
	 * 
	 * 删除的时候压缩页面
	 */
	void compressPageWhenDelete() {

	}

	/*
	 * 
	 * 修改一条记录
	 */
	public void update(int RowNum, byte[] bytes) throws Exception {
		int currentPageNum = rowNumMap.FindPageIdByRowNum(RowNum);
		int currentRecordNum = rowNumMap.FindRecordIdByRowNum(RowNum);
		PageBuffer pgB = (filepageManager.inUse.get(currentPageNum) == null) ? filepageManager.get(currentPageNum)
				: filepageManager.inUse.get(currentPageNum);
		int offset = pgB.readInt(currentRecordNum);
		int length = pgB.readInt(currentRecordNum + 4);
        if(bytes.length > length){
        	int EnoughSpacePageId = pageSize.findEnoughSpacePage(bytes.length);
        	PageBuffer pgBtmp = (filepageManager.inUse.get(EnoughSpacePageId) == null) ? filepageManager
    				.get(EnoughSpacePageId) : filepageManager.inUse.get(EnoughSpacePageId);
    		fillRecord(pgBtmp, bytes);
    		rowNumMap.modifyMapPageNum(RowNum, EnoughSpacePageId);
    		rowNumMap.modifyMapRecordNum(RowNum, findFreeIndex(pgBtmp));
    		pageSize.setPageSize(EnoughSpacePageId * 4, getAvailableSize(pgBtmp));
        }
        else{
        	pgB.writeByteArray(bytes, 0, offset, bytes.length);
        	pgB.writeInt(currentRecordNum + 4, bytes.length);
        }
	}

	/*
	 * 
	 * 查询一条记录
	 */
	public byte[] fetch(int RowNum) throws IOException {
		int currentPageNum = rowNumMap.FindPageIdByRowNum(RowNum);
		int currentRecordNum = rowNumMap.FindRecordIdByRowNum(RowNum);
		return getRecordById(currentPageNum, currentRecordNum);
	}

	/*
	 * 
	 * 修改一条记录
	 */

	int findFreePosition(PageBuffer pgB) {
		int i = pgB.readInt(this.FreeSpaceBeginPointer);
		return pgB.readInt(this.FreeSpaceBeginPointer);
	}

	int findFreeIndex(PageBuffer pgB) {
		int i = pgB.readInt(this.freeIndexPointer);
		return pgB.readInt(this.freeIndexPointer);
	}

	int getAvailableSize(PageBuffer pgB) {
		int i = findFreeIndex(pgB) - this.findFreePosition(pgB);
		return findFreeIndex(pgB) - this.findFreePosition(pgB);
	}

	public byte[] getRecordById(int pageId, int RecordId) throws IOException {
		//PageBuffer pgB = (filepageManager.inUse.get(pageId) == null) ? filepageManager.get(pageId)
		//		: filepageManager.inUse.get(pageId);
		PageBuffer  pgB = filepageManager.get(pageId);
		int offset = pgB.readInt(RecordId);
		int length = pgB.readInt(RecordId + 4);
		
		byte[] data = pgB.readByteArray(new byte[length], 0, offset, length);
		filepageManager.release(pageId, true);
		return  data;
		
	}

	public void displayAllRow() throws Exception {
		for (int i = 1; i <= rowNumMap.getLastRowNum(); i++) {
			System.out.println("第" + i + "行为" + new String(fetch(i)));
		}
	}

}
