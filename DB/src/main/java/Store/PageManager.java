package Store;

import java.io.IOException;

public class PageManager {
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
	final PageFile file;
	private PageBuffer PageManagerPage;

	PageManager(PageFile file) throws IOException {
		this.file = file;
		PageManagerPage = file.get(0);
		PageManagerPage.ensureHeapBuffer();
	}

	public int findEnoughSpacePage(int InsertRecordSize) {
		int i = 0;
		while ((i < (getEmptyPageBeginPointer()))
				&& InsertRecordSize > PageManagerPage.readInt(i * 4)) {
			i += 4;
		}
		return i < getEmptyPageBeginPointer() ? i : allocateNewPage();
	}

	int getEmptyPageBeginPointer() {
		return this.PageManagerPage.readInt(EMPTY_PAGE_BEGINPOINTER);
	}

	void setEmptyPageBeginPointer(int Position) {
		this.PageManagerPage.writeInt(EMPTY_PAGE_BEGINPOINTER, Position);
	}

	int allocateNewPage() {
		setEmptyPageBeginPointer(getEmptyPageBeginPointer() + 4);
		PageManagerPage.writeInt(getEmptyPageBeginPointer(),
				INITIALIZE_AVAILABLE_SIZE);
		return getEmptyPageBeginPointer();
	}

}
