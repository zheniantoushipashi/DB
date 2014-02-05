package Store;

import Constant.MagicNum;

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
	
	public  final  int  bytesOfInt = 1 << 2;
	public final int FreeSpaceBeginPointer = (PAGE_SIZE- bytesOfInt -1);

	/*
	 * 空闲记录块的开始位置
	 */

	public final int freeIndexPointer = (PAGE_SIZE - 2 * bytesOfInt - 1);
	/*
	 * 
	 * 一个描述偏移位置和Record大小的记录块的大小 字节
	 */
	
	public final int offsetSizeIndex = 2*bytesOfInt;

	private final PageBuffer pgB;

    public RecordManager(PageBuffer pgB) {
		this.pgB = pgB;
		this.pgB.writeInt(freeIndexPointer, PAGE_SIZE - 2 * bytesOfInt - 1);
		this.pgB.writeInt(FreeSpaceBeginPointer, 0);
		
	}

	public int insert(final byte[] data, final int length) {
          if((length + offsetSizeIndex) > this.getAvailableSize()){
        	  return -1;
          }  
          this.pgB.writeByteArray(data, 0, this.findFreePosition(), length); 
          this.pgB.writeInt(this.findFreeIndex() - offsetSizeIndex, this.findFreePosition());
          this.pgB.writeInt(this.findFreeIndex() - offsetSizeIndex/2, length);
          this.pgB.writeInt(FreeSpaceBeginPointer, this.findFreePosition() + length);
          this.pgB.writeInt(offsetSizeIndex, this.findFreeIndex() - offsetSizeIndex);
          return  this.findFreeIndex();
	} 
	int findFreePosition() {
		return this.pgB.readInt(this.FreeSpaceBeginPointer);
	}

	int findFreeIndex() {
	     int  i = this.pgB.readInt(this.freeIndexPointer);
		return this.pgB.readInt(this.freeIndexPointer);
	}
	int  getAvailableSize(){
		return  PAGE_SIZE-this.findFreePosition();
	}
	

}
