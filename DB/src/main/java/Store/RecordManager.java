package Store;

import java.io.IOException;

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
	
	private  final PageManager  pageManager;
	String filename = "storeFile3";
	PageFile file = new PageFile(filename);
	public RecordManager() throws IOException {
	//	this.pgB.writeInt(freeIndexPointer, PAGE_SIZE - 2 * bytesOfInt);
	//	this.pgB.writeInt(FreeSpaceBeginPointer, 0);
		pageManager = new PageManager(file);	
	}
	public int insert(final byte[] data) throws IOException {
		PageBuffer  pgB = file.get(pageManager.findEnoughSpacePage(data.length));
		pgB.writeByteArray(data, 0, this.findFreePosition(pgB), data.length);
		pgB.writeInt(this.findFreeIndex(pgB) - offsetSizeIndex,
				this.findFreePosition(pgB));
		pgB.writeInt(this.findFreeIndex(pgB) - offsetSizeIndex / 2, data.length);
		pgB.writeInt(FreeSpaceBeginPointer, this.findFreePosition(pgB)
				+ data.length);
		pgB.writeInt(freeIndexPointer, this.findFreeIndex(pgB)
				- offsetSizeIndex);
		return this.findFreeIndex(pgB);
	}

	int findFreePosition(PageBuffer pgB) {
		int i = pgB.readInt(this.FreeSpaceBeginPointer);
		return pgB.readInt(this.FreeSpaceBeginPointer);
	}

	int findFreeIndex(PageBuffer pgB) {
		int i = pgB.readInt(this.freeIndexPointer);
		return pgB.readInt(this.freeIndexPointer);
	}

	int getAvailableSize(PageBuffer pgB) {
		int i = PAGE_SIZE - this.findFreePosition(pgB);
		return PAGE_SIZE - this.findFreePosition(pgB);
	}

	public byte[] getRecordById(int RecordId, PageBuffer pgB) {
		int offset = pgB.readInt(RecordId);
		int length = pgB.readInt(RecordId + 4);
		return pgB.readByteArray(new byte[length], 0, offset, length);
	}

}
