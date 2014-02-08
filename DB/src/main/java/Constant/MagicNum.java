package Constant;
/*
 * 参照一篇博客自己实现博客中的原理  http://blog.csdn.net/cjfeii/article/details/8884658
 * 
 */
public class MagicNum {

	/*
	 * 在一个页面中指向空闲的空间开始的位置
	 */
	public final short FreeSpaceBeginPointer = 0;

	/*
	 * 空闲记录块的开始位置
	 */

	public final short freeIndexPointer = 1;
	/*
	 * 
	 * 一个描述偏移位置和Record大小的记录块的大小 字节
	 */
	public final short offsetSizeIndex = 2;

}
