package BtreeIndex;

import java.util.Arrays;
/**
 * @author 孙彪彪
 * B+ 树的内节点
 */
public class PageBtreeNode  extends PageBtree{
	public PageBtreeNode(PageTable pages, int order)
	{
		super(pages, order);
		keys = new int[treeOrder-1];
		Arrays.fill(keys, -1);
		pointers = new int[treeOrder];
		Arrays.fill(pointers, -1);
	}

	@Override
	public int search(int key) throws Exception {
		for (int i=0; i<keys.length;i++) {
			if (key <= keys[i] || keys[i] == -1) {
				return getNode(pointers[i]).search(key);
			}
		}
		// Hasn't been found at other pointers. Search last pointer.
		return getNode(pointers[treeOrder - 1]).search(key);
	}
	
	public int insertionPoint(int key) throws KeyExistsException {
		for (int i=0; i<keys.length; i++ ) {
			if (keys[i] >= key || keys[i] == -1) {
				if (keys[i] == key) throw new KeyExistsException();
				return i;
			}
		}
		return numElements()-1;
	}
	
	
	/**
	 * 
	 */
	@Override
	public void insert(int key, int value) throws Exception {
		int insertIndex = insertionPoint(key);
		PageBtree  target  = getNode(pointers[insertIndex]);
		int targetPtr = pointers[insertIndex];
		try {
			target.insert(key, value);
			Page p = pages.getIndexedPage(targetPtr);
			p.contents = Arrays.copyOf(target.toBytes(), p.contents.length);
		}
		catch (LeafNodeFullException e) {
			// need to split node
			PageBtreeLeaf tinyLeaf = new PageBtreeLeaf(pages, treeOrder);
			PageBtreeLeaf bigLeaf = new PageBtreeLeaf(pages, treeOrder);
			
			int nodeTransitionIndex = (int) Math.ceil(treeOrder/2);
			int iterator = 0;
			for (int i=0; i<=treeOrder; i++) {
				if (i == ((PageBtreeLeaf)target).insertionPoint(key)) {
					if (i <= nodeTransitionIndex) {
						tinyLeaf.keys()[i] = key;
						tinyLeaf.pointers()[i] = value;
					}
					else {
						bigLeaf.keys()[i-nodeTransitionIndex-1] = key;
						bigLeaf.pointers()[i-nodeTransitionIndex-1] = value;
					}
				}
				else {
					if (i <= nodeTransitionIndex) {
						tinyLeaf.keys()[i] = target.keys()[iterator];
						tinyLeaf.pointers()[i] = target.pointers()[iterator];
					}
					else {
						bigLeaf.keys()[i-nodeTransitionIndex-1] = target.keys()[iterator];
						bigLeaf.pointers()[i-nodeTransitionIndex-1] = target.pointers()[iterator];
					}
					iterator++;
				}
			}
			int tinyPage = pages.getNewPage(); Page tinyPg = pages.getIndexedPage(tinyPage);
			int bigPage = pages.getNewPage(); Page bigPg = pages.getIndexedPage(bigPage);
			tinyLeaf.siblingPtr(bigPage);
			tinyPg.contents = Arrays.copyOf(tinyLeaf.toBytes(), tinyPg.contents.length);
			bigPg.contents = Arrays.copyOf(bigLeaf.toBytes(), bigPg.contents.length);
			
			// push up the new pointers
			if (isFull()) {
				// no more room in this internal node. Need to propagate split up.
				throw new InternalNodeFullException(
						tinyPage, tinyLeaf, bigPage, bigLeaf
						);
			}
			else {
				int lastPointerIndex = numElements()-1;
				if (insertIndex < lastPointerIndex) { // shift last pointer down
					pointers[lastPointerIndex+1] = pointers[lastPointerIndex];
				}
				for (int i=lastPointerIndex-1; i >= insertIndex; i--) { // shift values down to make room for insertion
					keys[i+1] = keys[i];
					pointers[i+1] = pointers[i];
				}
				keys[insertIndex] = tinyLeaf.keys()[tinyLeaf.numElements()-1];
				pointers[insertIndex] = tinyPage;
				pointers[insertIndex+1] = bigPage;
				pages.deletePage(targetPtr); // delete the now orphaned node
			}
		}
		catch (InternalNodeFullException e) {
			// TODO handle internal node full
			// split the internal node
			PageBtreeNode leftNode = new PageBtreeNode(pages, treeOrder);
			PageBtreeNode rightNode = new PageBtreeNode(pages, treeOrder);
			int pushedUpKey = e.tinyNode.keys()[e.tinyNode.numElements()-1]; // biggest element of left side split node
			int pushupInsertIndex = target.numElements()-1;
			for (int i=0; i<target.keys().length; i++) {
				if (target.keys()[i] >= pushedUpKey || target.keys()[i] == -1) {
					pushupInsertIndex = i;
					break;
				}
			}
			int nodeTransitionIndex = (int) Math.ceil(treeOrder/2); // the index after which keys are put in rightNode
			int iterator = 0;
			for (int i=0; i<treeOrder; i++) {
				if (i == pushupInsertIndex) {
					if (i <= nodeTransitionIndex) {
						leftNode.keys()[i] = pushedUpKey;
					}
					else {
						rightNode.keys()[i-nodeTransitionIndex-1] = pushedUpKey;
					}
				}
				else {
					if (i <= nodeTransitionIndex) {
						leftNode.keys()[i] = target.keys()[iterator];
					}
					else {
						rightNode.keys()[i-nodeTransitionIndex-1] = target.keys()[iterator];
					}
					iterator++;
				}
			}
			iterator = 0;
			for (int i=0; i<treeOrder+1; i++) { // reconnect pointers
				if (i == pushupInsertIndex) {
					if (i <= nodeTransitionIndex) {
						leftNode.pointers()[i] = e.tinyPtr;
					}
					else {
						rightNode.pointers()[i-nodeTransitionIndex-1] = e.tinyPtr;
					}
				}
				else if (i == pushupInsertIndex+1) {
					if (i <= nodeTransitionIndex) {
						leftNode.pointers()[i] = e.bigPtr;
					}
					else {
						rightNode.pointers()[i-nodeTransitionIndex-1] = e.bigPtr;
					}
					// TODO: destroy page for current target.pointers()[iterator] value?
					iterator++;
				}
				else {
					if (i <= nodeTransitionIndex) {
						leftNode.pointers()[i] = target.pointers()[iterator];
					}
					else {
						rightNode.pointers()[i-nodeTransitionIndex-1] = target.pointers()[iterator];
					}
					iterator++;
				}
			}
			int leftPage = pages.getNewPage(); Page leftPg = pages.getIndexedPage(leftPage);
			int rightPage = pages.getNewPage(); Page rightPg = pages.getIndexedPage(rightPage);
			leftPg.contents = Arrays.copyOf(leftNode.toBytes(), leftPg.contents.length);
			rightPg.contents = Arrays.copyOf(rightNode.toBytes(), rightPg.contents.length);
			
			// push up the new pointers
			if (isFull()) {
				// no more room in this internal node. Need to propagate split up.
				throw new InternalNodeFullException(leftPage, leftNode, rightPage, rightNode);
			}
			else {
				int lastPointerIndex = numElements()-1;
				if (insertIndex < lastPointerIndex) { // shift last pointer down
					pointers[lastPointerIndex+1] = pointers[lastPointerIndex];
				}
				for (int i=lastPointerIndex-1; i >= insertIndex; i--) { // shift values down to make room for insertion
					keys[i+1] = keys[i];
					pointers[i+1] = pointers[i];
				}
				keys[insertIndex] = leftNode.keys()[leftNode.numElements()-1]; // push up largest value in left node
				leftNode.keys()[leftNode.numElements()-1] = -1; // TODO delete this key after it's been pushed up, or leave it?
				//													Right now not being deleted, because leftNode written before this change.
				pointers[insertIndex] = leftPage;
				pointers[insertIndex+1] = rightPage;
				pages.deletePage(targetPtr); // delete the now orphaned node
			}
		}
	}

}
