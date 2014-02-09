package Store;

import java.io.IOException;

public class PageManager {
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
	 * 初始分配的页面大小
	 */
	static final int INITIALIZE_AVAILABLE_SIZE = 4096;
	/*
	 * 
	 * 未分配的页面号的记录位置
	 */
	static final int EMPTY_PAGE_BEGINPOINTER = 0;
	static final  int  EMPTY_PAGE_BEGINPOSItION = 8;
	static  final  int FIRST_SEARCH_PAGE_NUM = 8;
	final PageFile file;
	private PageBuffer PageManagerPage;

	PageManager(PageFile file) throws IOException {
		this.file = file;
		PageManagerPage = file.get(0);
		setEmptyPageBeginPointer(EMPTY_PAGE_BEGINPOSItION);
		PageManagerPage.ensureHeapBuffer();
	}

	public int findEnoughSpacePage(int InsertRecordSize) throws IOException {
		int i = FIRST_SEARCH_PAGE_NUM;
		while ((i < (getEmptyPageBeginPointer()))
				&& (InsertRecordSize + 8) > PageManagerPage.readInt(i)) {
			int j = PageManagerPage.readInt(i);
			i += 4;
		}
		return i < getEmptyPageBeginPointer() ? (i / 4) : allocateNewPage();
	}

	int getEmptyPageBeginPointer() {
		return this.PageManagerPage.readInt(EMPTY_PAGE_BEGINPOINTER);
	}

	void setEmptyPageBeginPointer(int Position) {
		this.PageManagerPage.writeInt(EMPTY_PAGE_BEGINPOINTER, Position);
	}

	void setPageSize(int Position, int PageSize){
		this.PageManagerPage.writeInt(Position, PageSize);
	}
	int allocateNewPage() throws IOException {
		PageManagerPage.writeInt(getEmptyPageBeginPointer(),
				INITIALIZE_AVAILABLE_SIZE);
		setEmptyPageBeginPointer(getEmptyPageBeginPointer() + 4);
		PageBuffer  pgB = file.get((getEmptyPageBeginPointer() / 4) - 1);
		pgB.writeInt(freeIndexPointer, PAGE_SIZE - 2 * bytesOfInt);
		pgB.writeInt(FreeSpaceBeginPointer, 0);
		return ( (getEmptyPageBeginPointer() / 4) - 1 );
	}
	public  int  getAvailableSizeByPageNum (int PageNum){
		return  PageManagerPage.readInt(PageNum * 4);
	}

}
