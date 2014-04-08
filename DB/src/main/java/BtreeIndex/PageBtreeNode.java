package BtreeIndex;

import java.util.Arrays;
/**
 * @author 孙彪彪
 * B+ 树的内节点
 */
public class PageBtreeNode  extends PageBtree{
	public PageBtreeNode(PageTable pages, int order)
	{
		super(pages, order);
		keys = new int[treeOrder-1];
		Arrays.fill(keys, -1);
		pointers = new int[treeOrder];
		Arrays.fill(pointers, -1);
	}

}
