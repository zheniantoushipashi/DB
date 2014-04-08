package index;

import structure.SearchRow;

public class PageBTreeNode extends PageBtree {
	 private int[] childPageIds;
	 
	 @Override
	    void find(PageBtreeCursor cursor, SearchRow first, boolean bigger) {
	        int i = find(first, bigger, false, false);
	        if (i > entryCount) {
	            if (parentPageId == PageBtree.ROOT) {
	                return;
	            }
	            PageBTreeNode next = (PageBTreeNode) index.getPage(parentPageId);
	            next.find(cursor, first, bigger);
	            return;
	        }
	        PageBtree page = index.getPage(childPageIds[i]);
	        page.find(cursor, first, bigger);
	    }

}
