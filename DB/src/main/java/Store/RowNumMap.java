package Store;

import java.io.IOException;

public class RowNumMap {
	final PageFile file;
	private PageBuffer RowNumMapPage;
	/*
	 * 
	 * 指向将要写入Map对的位置
	 */
	static final int EMPTY_SPACE_POINTER = 0;
	static  final  int  lastRowNum = 4;
	/*
	 * 
	 * 一个映射对的大小8个字节
	 */
	static  final  int MAP_SIZE = 8;
	RowNumMap(PageFile file) throws IOException {
		this.file = file;
		RowNumMapPage = file.get(1);
		RowNumMapPage.ensureHeapBuffer();
	}
	int  getEmptySpacePointer() throws Exception{
		return RowNumMapPage.readInt(EMPTY_SPACE_POINTER);
	}
	void setEmptySpacePointer(int Position) throws Exception{
		RowNumMapPage.writeInt(EMPTY_SPACE_POINTER, Position);
	}
	int  getLastRowNum(){
		return RowNumMapPage.readInt(lastRowNum);
	}
	void setLastRowNum(int lastRowNum){
		RowNumMapPage.writeInt(lastRowNum, lastRowNum);
	}
	public void  RegisterMapWhenInsert(int recordId) throws Exception{
	    RowNumMapPage.writeInt(getEmptySpacePointer(), getLastRowNum() + 1);
	    RowNumMapPage.writeInt(getEmptySpacePointer() + 4, recordId);
	    setLastRowNum(getLastRowNum() + 1);
	}
    public  int	FindMap(int RowNum){
    	int i = 0;
    	while(i < )
    }
	
}
