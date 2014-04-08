package BtreeIndex;

import java.util.Arrays;
public class PageBtreeIndex {
	/**
	 * 
	 */
	private int rootPage;
	/**
	 * 
	 */
	private PageTable pages;
	/**
	 * 
	 */
	private int treeOrder;

	/**
	 * 
	 * @throws Exception
	 */
	
	public  PageBtreeIndex() throws Exception{
		pages = new PageTable(100);
		treeOrder = 3;
		PageBtreeLeaf root = new PageBtreeLeaf(pages, treeOrder);
		byte[] flatRoot = root.toBytes();
		rootPage = pages.getNewPage();
		BtreeIndex.Page  rootPg = pages.getIndexedPage(rootPage);
		rootPg.contents  = Arrays.copyOf(flatRoot, rootPg.contents.length);
	}

}
