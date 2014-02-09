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
    private  final  int PageOffset = 4;
    private  final  int RecordOffset = 6;
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
	public int  RegisterMapWhenInsert(int pageId, int recordId) throws Exception{
	    RowNumMapPage.writeInt(getEmptySpacePointer(), getLastRowNum() + 1);
	    RowNumMapPage.writeShort(getEmptySpacePointer() + PageOffset, (short)pageId);
	    RowNumMapPage.writeShort(getEmptySpacePointer() + RecordOffset, (short)recordId);
	    setEmptySpacePointer(getEmptySpacePointer() + MAP_SIZE);
	    return RowNumMapPage.readInt(getEmptySpacePointer() - MAP_SIZE);
	}
    public int	FindPageIdByRowNum(int RowNum){
        return  RowNumMapPage.readShort((RowNum - 1) * MAP_SIZE + MAPSTARTPOINT + PageOffset);
    }
    
    public int	FindRecordIdByRowNum(int RowNum){
    	return  RowNumMapPage.readShort((RowNum - 1) * MAP_SIZE + MAPSTARTPOINT + RecordOffset);
    }
	public  void shiftMap(int  toDeleteRow) throws Exception{
		for(int i = toDeleteRow ;i < getEmptySpacePointer() - MAP_SIZE;i += 8){
			RowNumMapPage.writeInt(((i - 1) * MAP_SIZE + MAPSTARTPOINT + PageOffset), RowNumMapPage.readInt((i + 1 -1) * MAP_SIZE + MAPSTARTPOINT + PageOffset));	
		}
	}
	
}
