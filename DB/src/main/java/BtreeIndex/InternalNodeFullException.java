/**
 * 
 */
package BtreeIndex;
/**
 * @author QtotheC
 *
 */
public class InternalNodeFullException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4477582053467017193L;
	public int tinyPtr;
	public PageBtree tinyNode;
	public int bigPtr;
	public PageBtree bigNode;
	
	public InternalNodeFullException(int tinyPtr, PageBtree tiny, int bigPtr, PageBtree big) {
		this.tinyPtr = tinyPtr;
		this.tinyNode=tiny;
		this.bigPtr=bigPtr;
		this.bigNode=big;
	}
}
