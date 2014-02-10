package Store;

import java.io.IOException;
import java.util.Iterator;

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

	private final PageManager pageManager;

	public PageManager getPageManager() {
		return pageManager;
	}

	private RowNumMap rowNumMap;

	public RowNumMap getRowNumMap() {
		return rowNumMap;
	}

	public void setRowNumMap(RowNumMap rowNumMap) {
		this.rowNumMap = rowNumMap;
	}

	PageFile file;

	public RecordManager() throws Exception {
		pageManager = new PageManager(file);
		rowNumMap = new RowNumMap(file);
	}

	public RecordManager(String filename) throws Exception {
		this.file = new PageFile(filename);
		pageManager = new PageManager(file);
		rowNumMap = new RowNumMap(file);
	}

	/*
	 * 
	 * 
	 * 插入一条记录
	 */
	public int insert(final byte[] data) throws Exception {
		int EnoughSpacePageId = pageManager.findEnoughSpacePage(data.length);
		PageBuffer pgB = (file.inUse.get(EnoughSpacePageId) == null) ? file
				.get(EnoughSpacePageId) : file.inUse.get(EnoughSpacePageId);
		fillRecord(pgB, data);
		int InsertRowNum = rowNumMap.RegisterMapWhenInsert(EnoughSpacePageId,
				findFreeIndex(pgB));
		pageManager.setPageSize(EnoughSpacePageId * 4, getAvailableSize(pgB));
		return InsertRowNum;

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
		Iterator<PageBuffer> iter = file.inUse.valuesIterator();
		while (iter.hasNext()) {
			file.release(iter.next().getPageId(), true);
		}
		file.close();
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
		PageBuffer pgB = (file.inUse.get(currentPageNum) == null) ? file.get(currentPageNum)
				: file.inUse.get(currentPageNum);
		int offset = pgB.readInt(currentRecordNum);
		int length = pgB.readInt(currentRecordNum + 4);
        if(bytes.length > length){
        	int EnoughSpacePageId = pageManager.findEnoughSpacePage(bytes.length);
        	PageBuffer pgBtmp = (file.inUse.get(EnoughSpacePageId) == null) ? file
    				.get(EnoughSpacePageId) : file.inUse.get(EnoughSpacePageId);
    		fillRecord(pgBtmp, bytes);
    		rowNumMap.modifyMapPageNum(RowNum, EnoughSpacePageId);
    		rowNumMap.modifyMapRecordNum(RowNum, findFreeIndex(pgBtmp));
    		pageManager.setPageSize(EnoughSpacePageId * 4, getAvailableSize(pgBtmp));
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
		PageBuffer pgB = (file.inUse.get(pageId) == null) ? file.get(pageId)
				: file.inUse.get(pageId);
		int offset = pgB.readInt(RecordId);
		int length = pgB.readInt(RecordId + 4);
		return pgB.readByteArray(new byte[length], 0, offset, length);
	}

	public void displayAllRow() throws Exception {
		for (int i = 1; i <= rowNumMap.getLastRowNum(); i++) {
			System.out.println("第" + i + "行为" + new String(fetch(i)));
		}
	}

}
