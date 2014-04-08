package index;
import java.lang.ref.SoftReference;

import org.h2.constant.SysProperties;
import org.h2.message.DbException;
import org.h2.result.Row;

import structure.SearchRow;

public class PageBTreeLeaf extends PageBtree  {

	private PageBTreeLeaf(PageBtreeIndex index, int pageId, Data data) {
        super(index, pageId, data);
        this.optimizeUpdate = index.getDatabase().getSettings().optimizeUpdate; //默认为true
    }


    /**
     * Create a new page.
     *
     * @param index the index
     * @param pageId the page id
     * @param parentPageId the parent
     * @return the page
     */
    static PageBTreeLeaf create(PageBtreeIndex index, int pageId, int parentPageId) {
        PageBTreeLeaf p = new PageBTreeLeaf(index, pageId, index.getPageStore().createData());
        p.rows = SearchRow.EMPTY_ARRAY;
        p.parentPageId = parentPageId;
        p.writeHead();
        p.start = p.data.length(); //PageBtreeLeaf页头部结束的位置，即写完entryCount项后的位置
        return p;
    }
    
    private void writeHead() {
        data.reset();
        data.writeByte((byte) (Page.TYPE_BTREE_LEAF | (onlyPosition ? 0 : Page.FLAG_LAST)));
        data.writeShortInt(0);
        data.writeInt(parentPageId);
        data.writeVarInt(index.getId());
        data.writeShortInt(entryCount); //最开始为0
    }
    
    @Override
    void find(PageBtreeCursor cursor, SearchRow first, boolean bigger) {
        int i = find(first, bigger, false, false); //这里返回的i怎么会>entryCount
        if (i > entryCount) { //什么情况下才会使得i > entryCount? 
            if (parentPageId == PageBtree.ROOT) {
                return;
            }
            PageBTreeNode next = (PageBTreeNode) index.getPage(parentPageId);
            next.find(cursor, first, bigger);
            return;
        }
        cursor.setCurrent(this, i);
    }
    
  //假设一个块128字节，写索引记录的顺序是从块尾开始，offset从块尾开始
    private int addRow(SearchRow row, boolean tryOnly) {
        int rowLength = index.getRowSize(data, row, onlyPosition);
        int pageSize = index.getPageStore().getPageSize();
        int last = entryCount == 0 ? pageSize : offsets[entryCount - 1];
        //确保page剩余空间能够保存offset
        if (last - rowLength < start + OFFSET_LENGTH) {
            if (tryOnly && entryCount > 1) {
                int x = find(row, false, true, true);
                if (entryCount < 5) {
                    // required, otherwise the index doesn't work correctly
                    return entryCount / 2;
                }
                // split near the insertion point to better fill pages
                // split in half would be:
                // return entryCount / 2;
                int third = entryCount / 3;
                return x < third ? third : x >= 2 * third ? 2 * third : x;
            }
            //当索引字段值的大小超过pageSize时只存位置不存字段值
            readAllRows();
            writtenData = false;
            onlyPosition = true;
            // change the offsets (now storing only positions)
            int o = pageSize;
            for (int i = 0; i < entryCount; i++) {
                o -= index.getRowSize(data, getRow(i), true);
                offsets[i] = o;
            }
            last = entryCount == 0 ? pageSize : offsets[entryCount - 1];
            rowLength = index.getRowSize(data, row, true);
            if (SysProperties.CHECK && last - rowLength < start + OFFSET_LENGTH) {
                throw DbException.throwInternalError();
            }
        }
        index.getPageStore().logUndo(this, data);
        if (!optimizeUpdate) {
            readAllRows();
        }
        changeCount = index.getPageStore().getChangeCount();
        written = false;
        int x;
        if (entryCount == 0) {
            x = 0;
        } else {
            x = find(row, false, true, true);
        }
        start += OFFSET_LENGTH;
        //x所在元素的值，是它前面的元素值-rowLength
        int offset = (x == 0 ? pageSize : offsets[x - 1]) - rowLength;
        //索引或内存中已有记录的情况(第一次运行完JDBC客户端程序后不删表和索引，第2次运行JDBC客户端程序添加记录就能测试)此种情况
        if (optimizeUpdate && writtenData) {
            if (entryCount > 0) {
                byte[] d = data.getBytes();
                int dataStart = offsets[entryCount - 1]; //offsets总是降序的，所以offsets[entryCount - 1]是data的最小下标
                int dataEnd = offset;
                //将data数组中dataStart下标开始的dataEnd - dataStart + rowLength个元素往左移到dataStart - rowLength开始处
                //(即: 把数据移到dataStart位置的左边，因为dataStart前的位置还没写数据，所以整体往前挪rowLength, 右边挪出的空位置
                //用来放新的row，这个row的位置就是offset)
                System.arraycopy(d, dataStart, d, dataStart - rowLength, dataEnd - dataStart + rowLength);
            }
            index.writeRow(data, offset, row, onlyPosition);
        }
        //offsets这个数组内的元素是由大到小的，所以在rows中的记录最先的反而写在文件的后面
        offsets = insert(offsets, entryCount, x, offset);
        //为什么要entryCount + 1呢，因前经过前面一行代码后，offsets的实际长度已经增加1了，但是entryCount在后面才加1
        add(offsets, x + 1, entryCount + 1, -rowLength); //x是中间位置时，从x之后的offset要减去rowLength
        rows = insert(rows, entryCount, x, row);
        entryCount++;
        index.getPageStore().update(this);
        return -1;
    }

    
    
}
