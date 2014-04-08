/*
 * Copyright 2004-2013 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package index;

import structure.Row;
import structure.SearchRow;

/**
 * The cursor implementation for the page b-tree index.
 */
public class PageBtreeCursor implements Cursor {
    private final PageBtreeIndex index;
    private final SearchRow last;
    private PageBTreeLeaf current;
    private int i;
    private SearchRow currentSearchRow;
    private Row currentRow;

    PageBtreeCursor(PageBtreeIndex index, SearchRow last) {
        
        this.index = index;
        this.last = last;
    }

    /**
     * Set the position of the current row.
     *
     * @param current the leaf page
     * @param i the index within the page
     */
    void setCurrent(PageBTreeLeaf current, int i) {
        this.current = current;
        this.i = i;
    }

    @Override
    public Row get() {
        if (currentRow == null && currentSearchRow != null) {
            currentRow = index.getRow(session, currentSearchRow.getKey());
        }
        return currentRow;
    }

    @Override
    public SearchRow getSearchRow() {
        return currentSearchRow;
    }

    @Override
    public boolean next() {
        if (current == null) {
            return false;
        }
        if (i >= current.getEntryCount()) {
            current.nextPage(this);
            //如果current是最顶层的leaf，那么在nextPage里会调用cursor.setCurrent(null, 0);
            //此时current变为null了
            if (current == null) {
                return false;
            }
        }
        currentSearchRow = current.getRow(i);
        currentRow = null;
        if (last != null && index.compareRows(currentSearchRow, last) > 0) {
            currentSearchRow = null;
            return false;
        }
        i++;
        return true;
    }

    @Override
    public boolean previous() {
        if (current == null) {
            return false;
        }
        if (i < 0) {
            current.previousPage(this);
            //如果current是最顶层的leaf，那么在previousPage里会调用cursor.setCurrent(null, 0);
            //此时current变为null了
            if (current == null) {
                return false;
            }
        }
        currentSearchRow = current.getRow(i);
        currentRow = null;
        i--;
        return true;
    }

}
