package BtreeIndex;

import java.util.Arrays;
/**
 * @author 孙彪彪
 *
 */
public class PageBtreeLeaf extends PageBtree{
	private int siblingPtr;
	
    public  PageBtreeLeaf(PageTable pages, int order){
    	super(pages, order);
    	keys = new int [treeOrder];
    	Arrays.fill(keys, -1);
    	pointers = new int[treeOrder];
		Arrays.fill(pointers, -1);
		siblingPtr = -1;
    }
}
