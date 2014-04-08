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
 * A cursor is a helper object to iterate through an index.
 * For indexes are sorted (such as the b tree index), it can iterate
 * to the very end of the index. For other indexes that don't support
 * that (such as a hash index), only one row is returned.
 * The cursor is initially positioned before the first row, that means
 * next() must be called before accessing data.
 *
 */
public interface Cursor {

    /**
     * Get the complete current row.
     * All column are available.
     *
     * @return the complete row
     */
    Row get();

    /**
     * Get the current row.
     * Only the data for indexed columns is available in this row.
     *
     * @return the search row
     */
    SearchRow getSearchRow();

    /**
     * Skip to the next row if one is available.
     *
     * @return true if another row is available
     */
    boolean next();

    /**
     * Skip to the previous row if one is available.
     * No filtering is made here.
     *
     * @return true if another row is available
     */
    boolean previous();

}