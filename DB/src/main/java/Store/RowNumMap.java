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
    static final  int MAPSTARTPOINT = 4;
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
	
	int  getLastRowNum() throws Exception{
		return (getEmptySpacePointer() - 4)/MAP_SIZE;
	}
	public void  RegisterMapWhenInsert(int recordId) throws Exception{
	    RowNumMapPage.writeInt(getEmptySpacePointer(), getLastRowNum() + 1);
	    RowNumMapPage.writeInt(getEmptySpacePointer() + 4, recordId);
	}
    public int	FindRecordIdByRowNum(int RowNum){
        return  RowNumMapPage.readInt(MAPSTARTPOINT+((RowNum - 1) * 8) + 4);
    }
	
}
