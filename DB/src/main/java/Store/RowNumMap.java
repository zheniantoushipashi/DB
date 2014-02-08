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
	RowNumMap(PageFile file) throws Exception {
		this.file = file;
		RowNumMapPage = file.get(1);
		RowNumMapPage.ensureHeapBuffer();
		setEmptySpacePointer(MAPSTARTPOINT);
	}
	int  getEmptySpacePointer() throws Exception{
		return RowNumMapPage.readInt(EMPTY_SPACE_POINTER);
	}
	void setEmptySpacePointer(int Position) throws Exception{
		RowNumMapPage.writeInt(EMPTY_SPACE_POINTER, Position);
	}
	
	public  int  getLastRowNum() throws Exception{
		return (getEmptySpacePointer() - 4)/MAP_SIZE;
	}
	public void  RegisterMapWhenInsert(int pageId, int recordId) throws Exception{
	    RowNumMapPage.writeInt(getEmptySpacePointer(), getLastRowNum() + 1);
	    RowNumMapPage.writeShort(getEmptySpacePointer() + 4, (short)pageId);
	    RowNumMapPage.writeShort(getEmptySpacePointer() + 6, (short)recordId);
	    setEmptySpacePointer(getEmptySpacePointer() + MAP_SIZE);
	}
    public int	FindRecordIdByRowNum(int RowNum){
        return  RowNumMapPage.readInt(MAPSTARTPOINT+((RowNum - 1) * 8) + 4);
    }
	
	
}
