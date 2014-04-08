/*
 * Copyright 2004-2013 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package index;

import structure.Column;
import structure.Row;
import structure.SearchRow;
import structure.Table;

/**
 * An index. Indexes are used to speed up searching data.
 */
//此类的提供的查找方法并没有直接提供模糊查询之类的功能，只有范围查询，
//更高级的功能都是通过范围查询获得记录后再在Select那一层通过where条件一一比较和过滤
public interface Index  {

    /**
     * Get the message to show in a EXPLAIN statement.
     *
     * @return the plan
     */
    String getPlanSQL(); //用于EXPLAIN语句，是"模式名.索引名"，跟getCreateSQL()不一样，getCreateSQL()是完整的"CREATE INDEX"

    /**
     * Add a row to the index.
     *
     * @param session the session to use
     * @param row the row to add
     */
    void add(Row row);

    /**
     * Remove a row from the index.
     *
     * @param session the session
     * @param row the row
     */
    void remove(Row row); //删除单行

    /**
     * Find a row or a list of rows and create a cursor to iterate over the result.
     *
     * @param session the session
     * @param first the first row, or null for no limit
     * @param last the last row, or null for no limit
     * @return the cursor to iterate over the results
     */
    Cursor find(SearchRow first, SearchRow last);

  
    /**
     * Remove the index.
     *
     * @param session the session
     */
    void remove(); //删除行并释放page

    /**
     * Remove all rows from the index.
     *
     * @param session the session
     */
    void truncate(); //不释放page，只删除行

    /**
     * Check if the index can directly look up the lowest or highest value of a
     * column.
     *
     * @return true if it can
     */
    boolean canGetFirstOrLast();

    /**
     * Check if the index can get the next higher value.
     *
     * @return true if it can
     */
    boolean canFindNext(); //只有PageBtreeIndex返回true

    /**
     * Find a row or a list of rows that is larger and create a cursor to
     * iterate over the result.
     *
     * @param session the session
     * @param higherThan the lower limit (excluding)
     * @param last the last row, or null for no limit
     * @return the cursor
     */
    //MVSecondaryIndex还不支持这个方法，所以在Select类中还无法支持DistinctQuery优化
    //比如这样的SQL: select distinct name from test
    //就算为name字段建立了索引也不行
    Cursor findNext(SearchRow higherThan, SearchRow last); //只有org.h2.index.PageBtreeIndex实现了

    /**
     * Find the first (or last) value of this index. The cursor returned is
     * positioned on the correct row, or on null if no row has been found.
     *
     * @param session the session
     * @param first true if the first (lowest for ascending indexes) or last
     *            value should be returned
     * @return a cursor (never null)
     */
    Cursor findFirstOrLast(boolean first); //用于快束min、max聚合查询

    /**
     * Check if the index needs to be rebuilt.
     * This method is called after opening an index.
     *
     * @return true if a rebuild is required.
     */
    boolean needRebuild(); //构建索引时调用

    /**
     * Get the row count of this table, for the given session.
     *
     * @param session the session
     * @return the row count
     */
    long getRowCount();

    /**
     * Get the approximated row count for this table.
     *
     * @return the approximated row count
     */
    long getRowCountApproximation();

    /**
     * Get the used disk space for this index.
     *
     * @return the estimated number of bytes
     */
    long getDiskSpaceUsed();

    /**
     * Compare two rows.
     *
     * @param rowData the first row
     * @param compare the second row
     * @return 0 if both rows are equal, -1 if the first row is smaller, otherwise 1
     */
    int compareRows(SearchRow rowData, SearchRow compare); //只比较索引字段，并不一定是所有字段

    /**
     * Get the index of a column in the list of index columns
     *
     * @param col the column
     * @return the index (0 meaning first column)
     */
    //并不是返回列id，而是索引字段列表中的位置，
    //PageDataIndex、MVPrimaryIndex直接返回-1，因为这两个类严格说不是索引，而是存放主表的原始记录的
    int getColumnIndex(Column col);

    
    /**
     * Get the indexed columns.
     *
     * @return the columns
     */
    Column[] getColumns();

   
    /**
     * Get the table on which this index is based.
     *
     * @return the table
     */
    Table getTable();

    /**
     * Commit the operation for a row. This is only important for multi-version
     * indexes. The method is only called if multi-version is enabled.
     *
     * @param operation the operation type
     * @param row the row
     */
    //如果使用了multiVersion，在提交时删除row
    void commit(int operation, Row row); //PageDataIndex、ScanIndex、MultiVersionIndex有实现，其他子类什么都没做

    /**
     * Get the row with the given key.
     *
     * @param session the session
     * @param key the unique key
     * @return the row
     */
    //MVSecondaryIndex类没实现，而是在org.h2.mvstore.db.MVSecondaryIndex.MVStoreCursor.get()中调用MVPrimaryIndex的
    Row getRow(long key); //按key获取主表的完整记录，PageDataIndex、MVPrimaryIndex才有效

    /**
     * Does this index support lookup by row id?
     *
     * @return true if it does
     */
    //按_ROWID_排序时，直接使用MVPrimaryIndex、PageDataIndex
    boolean isRowIdIndex(); //只有org.h2.mvstore.db.MVPrimaryIndex和org.h2.index.PageDataIndex返回true

    /**
     * Can this index iterate over all rows?
     *
     * @return true if it can
     */
    //只看到org.h2.store.PageStore.compact(int)在用
    boolean canScan(); //HashIndex、NonUniqueHashIndex、FunctionIndex返回false

    /**
     * Enable or disable the 'sorted insert' optimizations (rows are inserted in
     * ascending or descending order) if applicable for this index
     * implementation.
     *
     * @param sortedInsertMode the new value
     */
    //只在org.h2.index.PageDataLeaf中用到，
    //如insert into IndexTestTable(id, name, address) SORTED values(...)
    //见org.h2.index.PageDataLeaf.addRowTry(Row)，insert加了SORTED后，获取切换点的方式不一样。
    void setSortedInsertMode(boolean sortedInsertMode); //PageIndex有覆盖此方法

}
