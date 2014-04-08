package index;
import org.h2.index.PageBtree;
import org.h2.index.PageBtreeNode;
import org.h2.result.Row;
import org.h2.result.SearchRow;
import org.h2.store.Data;
import org.h2.table.Column;
import org.h2.value.Value;

import  structure.*;

public class PageBtreeIndex  extends PageIndex{
	
	 private  Cursor  find(SearchRow first, boolean  bigger, SearchRow  last){
		 PageBtree  root = getPage(rootPageId);
		 PageBtreeCursor cursor = new PageBtreeCursor(this, last);
		 root.find(cursor, first, bigger);
		 return  cursor;
	 }
	 
	 /**
	     * Read the given page.
	     *
	     * @param id the page id
	     * @return the page
	     */ 
	    PageBtree  getPage(int  id){
	    	//todo   根据id  获得一个页面。
	    	if(p == null){
	    		 PageBTreeLeaf empty = PageBTreeLeaf.create(this, id, PageBtree.ROOT);
	    		 return  empty;
	    	}
	    	return (PageBtree) p;	    	
	    }
	  
	    public  void  add(Row row){
	    	SearchRow  newRow = getSearchRow(row);
	    	addRow(newRow);
	    }
	    
	    /**
	     * Create a search row for this row.
	     *
	     * @param row the row
	     * @return the search row
	     */
	    private SearchRow getSearchRow(Row row) {
	        SearchRow r = table.getTemplateSimpleRow(columns.length == 1);
	        r.setKeyAndVersion(row);
	        for (Column c : columns) {
	            int idx = c.getColumnId();
	            r.setValue(idx, row.getValue(idx));
	        }
	        return r;
	    }
	    
	    private  void  addRow(SearchRow newRow){
	    	while(true){
	    		PageBtree root = getPage(rootPageId);
	    		int splitPoint = root 
	    	}
	    }
	    private void addRow(SearchRow newRow) {
	        while (true) {
	            PageBtree root = getPage(rootPageId);
	            int splitPoint = root.addRowTry(newRow);
	            if (splitPoint == -1) {
	                break;
	            }
	           
//	            System.out.println("-----------切割前----------");
//	            System.out.println(root);
//	            
	            //下面的代码是处理切割的情况
	            //一开始因为root是一个PageBtreeLeaf，所以是对PageBtreeLeaf的切割
	            //从第二次开始，都是对PageBtreeNode的切割
	            //对于PageBtreeLeaf，如果索引是升序的，那么切割后的两个节点，小于等于切割点的在左边结点，大于切割点的在右边结点，
	            //如果索引是降序的，那么切割后的两个节点，大于等于切割点的在左边结点，小于切割点的在右边结点，
	            if (trace.isDebugEnabled()) {
	                trace.debug("split {0}", splitPoint);
	            }
	            SearchRow pivot = root.getRow(splitPoint - 1);
	            store.logUndo(root, root.data);
	            PageBtree page1 = root;
	            PageBtree page2 = root.split(splitPoint);
	            store.logUndo(page2, null);
	            int id = store.allocatePage();
	            page1.setPageId(id); //左结点要改变pageId，右结点在root.split(splitPoint)内部已经有新pageId了。
	            
	            //在这里进行切割操作的因为是顶层结点，所以左右子结点的parentPageId必然是rootPageId, 这两个左右子结点在B-tree的第二层，
	            //当顶层结点继续切割时，那么这两些的两个左右子结点会继续往下沉，所以他们的parentPageId就不是rootPageId了，
	            //而是新的在B-tree的第二层的结点，所以在root.split(splitPoint)内部会重新remapChildren右节点中对应的原始子节点的
	            //而 page1.setPageId(id)会重新remapChildren左节点中对应的原始子节点
	            page1.setParentPageId(rootPageId);
	            page2.setParentPageId(rootPageId);
	            PageBtreeNode newRoot = PageBtreeNode.create(this, rootPageId, PageBtree.ROOT);
	            store.logUndo(newRoot, null);
	            newRoot.init(page1, pivot, page2);
	            store.update(page1);
	            store.update(page2);
				store.update(newRoot);
				root = newRoot; //这行代码没用

				//			System.out.println("-----------按" + pivot + "切割----------");
				//			System.out.println("-----------Root切割成两个子页面----------");
				//			System.out.println(page1);
				//			System.out.println(page2);
				//
				//			System.out.println("-----------切割后----------");
				//			System.out.println(root);
			}
			invalidateRowCount();
			rowCount++;

			//		System.out.println();
			//		System.out.println(getPage(rootPageId));
			//		System.out.println("---------------------");
	    }

	    
	    /**
	     * Get the size of a row (only the part that is stored in the index).
	     *
	     * @param dummy a dummy data page to calculate the size
	     * @param row the row
	     * @param onlyPosition whether only the position of the row is stored
	     * @return the number of bytes
	     */
	    int getRowSize(structureTest.Data dummy, SearchRow row, boolean onlyPosition) {
	        int rowsize = Data.getVarLongLen(row.getKey());
	        if (!onlyPosition) {
	            for (Column col : columns) {
	                Value v = row.getValue(col.getColumnId());
	                rowsize += dummy.getValueLen(v);
	            }
	        }
	        return rowsize;
	    }

}
