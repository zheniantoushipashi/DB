package index;
import java.nio.ByteBuffer;
import structure.Row;
import structure.SearchRow;
import  Store.*;
//this is  a  page  contains  indexs
public abstract class PageBtree extends PageBuffer {

	 /**
     * The index.
     */
    protected final PageBtreeIndex index;
	 /**
     * This is a root page.
     */
    static  final  int  ROOT = 0;
    /**
     * The page number of the parent.
     */
    protected  int  parentPageId;
    /**
     * The row offsets.
     */
    protected int []  offsets;
    /**
     * The number of entries.
     */
    protected int entryCount; //对于PageBtreeNode就是分隔点(rows)的个数(也等于子节点个数-1)，对于PageBtreeLeaf就代表行数

    /**
     * The index data
     */
    protected Row[] rows;
    
    /**
     * The start of the data area.
     */
    protected int start;
    

    /**
     * Whether the data page is up-to-date.
     */
    protected boolean written;
    
    /**
     * Find the first row.
     *
     * @param cursor the cursor
     * @param first the row to find
     * @param bigger if the row should be bigger
     */
    abstract void find(PageBtreeCursor cursor, SearchRow first, boolean bigger);
    /**
     * Find an entry.
     *
     * @param compare the row
     * @param bigger if looking for a larger row
     * @param add if the row should be added (check for duplicate keys)
     * @param compareKeys compare the row keys as well
     * @return the index of the found row
     */
    //折半查找
    //返回的下标会用于org.h2.store.Page的insert方法中，使得insert到rows时就按照建立索引时指定的排序方式排好了，
    //比如CREATE index IF NOT EXISTS idx_name ON IndexTestTable(name desc)，因为为name指定了desc，所以rows数组是降序的
    
    //此方法的返回值是: 0<=x<=entryCount
   
    int find(SearchRow compare, boolean bigger, boolean add, boolean compareKeys) { //只有addRow和remove时compareKeys为true，find时compareKeys为false
        if (compare == null) {
            return 0;
        }
        int l = 0, r = entryCount;
        int comp = 1;
        while (l < r) {
            int i = (l + r) >>> 1; //除以2，从中间元素开始
            SearchRow row = getRow(i);
            //如果索引字段是降序的，比如CREATE index IF NOT EXISTS idx_name ON IndexTestTable(name desc)
            //那么此时会按降序比较，如果row<compare，那么得到的结果是row>compare，此时rows数组中的元素是降序排列的。
            comp = index.compareRows(row, compare);
            if (comp == 0) {
            	//增加新记录时，如果是唯一索引，当两条记录相等时，
            	//再根据不同数据库的兼容模式来判断记录中的null字段情况
            	//1. uniqueIndexSingleNull 返回false, 抛出DuplicateKeyException
            	//2. uniqueIndexSingleNullExceptAllColumnsAreNull
            	//   如果要比较的记录包含非null字段，返回false, 抛出DuplicateKeyException，否则允许通过
            	//3. 其他情况: 如果要比较的记录含null字段，返回true，否则返回false, 抛出DuplicateKeyException
                if (add && index.indexType.isUnique()) {
                    if (!index.containsNullAndAllowMultipleNull(compare)) {
                        throw index.getDuplicateKeyException(compare.toString());
                    }
                }
                if (compareKeys) {
                    comp = index.compareKeys(row, compare);
                    if (comp == 0) {
                        return i;
                    }
                }
            }
            //第i个元素大于要比较的元素时，往左移动，
            //或者相等时，如果bigger为false，说明要找更小的，往左移动
            if (comp > 0 || (!bigger && comp == 0)) {
                r = i;
            } else {
                l = i + 1;
            }
        }
        return l;
    }

    /**
     * Get the row at this position.
     *
     * @param at the index
     * @return the row
     */
    SearchRow getRow(int at) {
        SearchRow row = rows[at];
        if (row == null) {
            row = index.readRow(data, offsets[at], onlyPosition, true);
            memoryChange();
            rows[at] = row;
        } else if (!index.hasData(row)) {
            row = index.readRow(row.getKey());
            memoryChange();
            rows[at] = row;
        }
        return row;
    }
    /**
     * Add a row if possible. If it is possible this method returns -1, otherwise
     * the split point. It is always possible to add one row.
     *
     * @param row the row to add
     * @return the split point of this page, or -1 if no split is required
     */
    abstract int addRowTry(SearchRow row);

	
}
