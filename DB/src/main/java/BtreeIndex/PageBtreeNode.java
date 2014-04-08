package BtreeIndex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
	
	@Override
	public boolean isLeaf() {
		return false;
	}
	
	@Override
	protected byte[] flatten() {
		int keySize = 9;
		int ptrSize = 4;
		ByteBuffer buff = ByteBuffer.allocate(keySize * (treeOrder - 1) + ptrSize * treeOrder);
		buff.order(ByteOrder.nativeOrder());
		buff.putInt(pointers[0]);
		buff.position(buff.position()+(ptrSize-4));
		for (int i=0; i<keys.length; i++) {
			buff.putInt(keys[i]);
			buff.position(buff.position()+(keySize-4));
			buff.putInt(pointers[i+1]);
			buff.position(buff.position()+(ptrSize-4));
		}
		return buff.array();
	}

	@Override
	public void unflatten(byte[] array) {
		int keySize = 9;
		int ptrSize = 4;
		ByteBuffer buff = ByteBuffer.wrap(array);
		buff.order(ByteOrder.nativeOrder());
		pointers[0] = buff.getInt();
		buff.position(buff.position()+(ptrSize-4));
		for (int i=0; i<keys.length; i++) {
			keys[i] = buff.getInt();
			buff.position(buff.position()+(keySize-4));
			pointers[i+1] = buff.getInt();
			buff.position(buff.position()+(ptrSize-4));
		}
	}
	
	
	public int deletionPointer(int key) {
		for (int i=0; i<keys.length; i++ ) {
			if (keys[i] >= key || keys[i] == -1) {
				return i;
			}
		}
		return numElements()-1;
	}

	@Override
	public int numElements() {
		int counter =0;
		for (int i=0; i<pointers.length; i++) {
			if (pointers[i] != -1) counter++;
			else break;
		}
		return counter;
	}


	/**
	 * 
	 */
	@Override
	public void delete(int key) throws Exception {
		int deletionIndex = deletionPointer(key);
		PageBtree target = getNode(pointers[deletionIndex]);
		int targetPtr = pointers[deletionIndex];
		try {
			target.delete(key);
			Page p = pages.getIndexedPage(targetPtr);
			p.contents = Arrays.copyOf(target.toBytes(), p.contents.length);
		}
		catch (LeafUnderflowException e) {
			boolean underflowFixed = false;
			PageBtreeLeaf leftNode = null;
			int leftIndex = deletionIndex - 1;
			PageBtreeLeaf rightNode = null;
			int rightIndex = deletionIndex + 1;
			if (deletionIndex > 0) { // underflowed node is not the first child
				leftNode = (PageBtreeLeaf)getNode(pointers[leftIndex]); // only leaves throw LeafUnderflowException
			}
			if (deletionIndex < numElements() - 1) { // underflowed node is not the last child
				rightNode = (PageBtreeLeaf)getNode(pointers[rightIndex]); // only leaves throw LeafUnderflowException
			}
			// try borrowing from siblings
			// try left side sibling
			if (leftNode != null && leftNode.numElements() > Math.ceil(treeOrder/2.0)) {
				// left node has enough to share
				// move entry from left node to target
				int leftNodeSpareIndex = leftNode.numElements()-1;
				target.insert(leftNode.keys()[leftNodeSpareIndex], leftNode.pointers()[leftNodeSpareIndex]);
				leftNode.delete(leftNode.keys()[leftNodeSpareIndex]);
				// push up new key
				int pushedKey = leftNode.keys()[leftNode.numElements()-1];
				keys[deletionIndex-1] = pushedKey;
				// save changes
				Page leftP = pages.getIndexedPage(pointers[leftIndex]);
				Page p = pages.getIndexedPage(targetPtr);
				p.contents = Arrays.copyOf(target.toBytes(), p.contents.length);
				leftP.contents = Arrays.copyOf(leftNode.toBytes(), leftP.contents.length);
				underflowFixed = true;
			}
			if (rightNode != null && !underflowFixed && rightNode.numElements() > Math.ceil(treeOrder/2.0)) {
				// right node has enough to share
				// move entry from right node to target
				target.insert(rightNode.keys()[0], rightNode.pointers()[0]);
				rightNode.delete(rightNode.keys()[0]);
				// push up new key
				int pushedKey = target.keys()[target.numElements()-1];
				keys[deletionIndex] = pushedKey; // TODO: see if this is the correct one
				// save changes
				Page rightP = pages.getIndexedPage(pointers[rightIndex]);
				Page p = pages.getIndexedPage(targetPtr);
				p.contents = Arrays.copyOf(target.toBytes(), p.contents.length);
				rightP.contents = Arrays.copyOf(rightNode.toBytes(), rightP.contents.length);
				underflowFixed = true;
			}
			if (!underflowFixed) { // siblings did not have enough to donate. Merge.
				// no rule on which two nodes to merge, so choose left one if it exists, otherwise right
				if (leftNode != null) {
					// merge with left node
					int beginIndex = leftNode.numElements();
					for (int i=0; i<target.numElements(); i++) {
						leftNode.keys()[beginIndex+i] = target.keys()[i];
						leftNode.pointers()[beginIndex+i] = target.pointers()[i];
					}
					// save merged node
					Page p = pages.getIndexedPage(pointers[leftIndex]);
					p.contents = Arrays.copyOf(leftNode.toBytes(), p.contents.length);
					// delete key and pointer from parent, shift elements left to maintain continuity
					for (int i=deletionIndex-1; i < keys.length; i++) {
						if (i+1 == keys.length) { // delete last key/pointer pair
							keys[i] = -1;
							pointers[i+1] = -1;
							break;
						}
						keys[i] = keys[i+1];
						pointers[i+1] = pointers[i+2];
						keys[i+1] = -1;
						pointers[i+2] = -1;
					}
					pages.deletePage(targetPtr);
					if (numElements() < Math.ceil(treeOrder/2.0)) {
						// this internal node is now underflowed. Throw exception.
						if (!isRoot() || (numElements() < 2 && isRoot())) { // if this is the root, only underflow when fewer than 2 pointers exist
							throw new InternalUnderflowException();
						}
					}
				}
				else if (rightNode != null) {
					// merge with right node
					int beginIndex = target.numElements();
					for (int i=0; i<rightNode.numElements(); i++) {
						target.keys()[beginIndex+i] = rightNode.keys()[i];
						target.pointers()[beginIndex+i] = rightNode.pointers()[i];
					}
					// save merged node
					Page p = pages.getIndexedPage(targetPtr);
					p.contents = Arrays.copyOf(target.toBytes(), p.contents.length);
					// delete key and pointer from parent, shift elements left to maintain continuity
					int deadPointer = pointers[deletionIndex+1];
					for (int i=deletionIndex; i < keys.length; i++) {
						if (i+1 == keys.length) { // delete last key/pointer pair
							keys[i] = -1;
							pointers[i+1] = -1;
							break;
						}
						keys[i] = keys[i+1];
						pointers[i+1] = pointers[i+2];
						keys[i+1] = -1;
						pointers[i+2] = -1;
					}
					pages.deletePage(deadPointer);
					if (numElements() < Math.ceil(treeOrder/2.0)) {
						// this internal node is now underflowed. Throw exception.
						if (!isRoot() || (numElements() < 2 && isRoot())) { // if this is the root, only underflow when fewer than 2 pointers exist
							throw new InternalUnderflowException();
						}
					}
				}
				else {
					throw new Exception("The root might do this, but I'll find out in debugging.");
				}
			}
		}
		catch (InternalUnderflowException e) {
			boolean underflowFixed = false;
			PageBtreeNode leftNode = null;
			int leftIndex = deletionIndex - 1;
			PageBtreeNode rightNode = null;
			int rightIndex = deletionIndex + 1;
			if (deletionIndex > 0) { // underflowed node is not the first child
				leftNode = (PageBtreeNode)getNode(pointers[leftIndex]); // only internal nodes throw InternalUnderflowException
			}
			if (deletionIndex < numElements() - 1) { // underflowed node is not the last child
				rightNode = (PageBtreeNode)getNode(pointers[rightIndex]); // only internal nodes throw InternalUnderflowException
			}
			// try borrowing from siblings
			// try left side sibling
			if (leftNode != null && leftNode.numElements() > Math.ceil(treeOrder/2.0)) {
				// left node has enough to share
				// move entry from left node to target
				int leftNodeSpareKeyIndex = leftNode.numElements()-2; // index of the key on the left node that we will borrow
				int leftNodeSparePtrIndex = leftNode.numElements()-1; // index of the pointer on the left node that we will borrow
				PageBtreeNode node = (PageBtreeNode)target;
				int lastPointerIndex = node.numElements()-1;
				for (int i=lastPointerIndex; i > 0; i--) { // shift values down to make room for insertion
					node.keys()[i] = node.keys()[i-1];
					node.pointers()[i+1] = node.pointers()[i];
				} 
				node.pointers()[1] = node.pointers()[0]; // shift first pointer down
				node.pointers()[0] = leftNode.pointers()[leftNodeSparePtrIndex]; // attach borrowed pointer
				node.keys()[0] = keys[deletionIndex-1]; // push down new key value
				keys[deletionIndex-1] = leftNode.keys()[leftNodeSpareKeyIndex]; // push up new key
				leftNode.keys()[leftNodeSpareKeyIndex] = -1; // remove borrowed key from left node
				leftNode.pointers()[leftNodeSparePtrIndex] = -1; // remove borrowed pointer from left node
				// save changes
				Page leftP = pages.getIndexedPage(pointers[leftIndex]);
				Page p = pages.getIndexedPage(targetPtr);
				p.contents = Arrays.copyOf(node.toBytes(), p.contents.length);
				leftP.contents = Arrays.copyOf(leftNode.toBytes(), leftP.contents.length);
				underflowFixed = true;
			}
			else if (rightNode != null && !underflowFixed && rightNode.numElements() > Math.ceil(treeOrder/2.0)) {
				// right node has enough to share
				// move entry from left node to target
				PageBtreeNode node = (PageBtreeNode)target;
				int insertIndex = target.numElements();
				node.keys()[insertIndex-1] = keys[deletionIndex]; // push down new key value
				node.pointers()[insertIndex] = rightNode.pointers()[0]; // attach borrowed pointer
				keys[deletionIndex] = rightNode.keys()[0]; // push up new key
				int rightNodeLength = rightNode.numElements();
				rightNode.pointers()[0] = rightNode.pointers()[1]; // remove borrowed key and pointer
				for (int i=0; i<rightNodeLength-2; i++) { // 			from right node
					rightNode.keys()[i] = rightNode.keys()[i+1];
					rightNode.keys()[i+1] = -1;
					rightNode.pointers()[i+1] = rightNode.pointers()[i+2];
					rightNode.pointers()[i+2] = -1;
				}
				// save changes
				Page rightP = pages.getIndexedPage(pointers[rightIndex]);
				Page p = pages.getIndexedPage(targetPtr);
				p.contents = Arrays.copyOf(target.toBytes(), p.contents.length);
				rightP.contents = Arrays.copyOf(rightNode.toBytes(), rightP.contents.length);
				underflowFixed = true;
			}
			else { // siblings did not have enough to share. Merge.
				// TODO got to merge
				if (leftNode != null) {
					// merge with left node
					int beginIndex = leftNode.numElements();
					leftNode.pointers()[beginIndex] = target.pointers()[0];
					for (int i=1; i<target.numElements(); i++) { // transfer keys and pointers to the left node
						leftNode.keys()[beginIndex+i-1] = target.keys()[i-1];
						leftNode.pointers()[beginIndex+i] = target.pointers()[i];
					}
					// push down new key
					leftNode.keys()[beginIndex-1] = keys[deletionIndex-1];
					// save merged node
					Page p = pages.getIndexedPage(pointers[leftIndex]);
					p.contents = Arrays.copyOf(leftNode.toBytes(), p.contents.length);
					// delete key and pointer from parent, shift elements left to maintain continuity
					for (int i=deletionIndex-1; i < keys.length; i++) {
						if (i+1==keys.length) {
							keys[i] = -1;
							pointers[i+1] = -1;
						}
						else {
							keys[i] = keys[i+1];
							pointers[i+1] = pointers[i+2];
							keys[i+1] = -1;
							pointers[i+2] = -1;
						}
					}
					pages.deletePage(targetPtr);
					if (numElements() < Math.ceil(treeOrder/2.0)) {
						// this internal node is now underflowed. Throw exception.
						if (!isRoot() || (numElements() < 2 && isRoot())) { // if this is the root, only underflow when fewer than 2 pointers exist
							throw new InternalUnderflowException();
						}
					}
				}
				else if (rightNode != null) {
					// merge with right node
					int beginIndex = target.numElements();
					target.pointers()[beginIndex] = rightNode.pointers()[0];
					for (int i=1; i<rightNode.numElements(); i++) { // transfer keys and pointers to the left node
						target.keys()[beginIndex+i-1] = rightNode.keys()[i-1];
						target.pointers()[beginIndex+i] = rightNode.pointers()[i];
					}
					// push down new key
					target.keys()[beginIndex-1] = keys[deletionIndex];
					// save merged node
					Page p = pages.getIndexedPage(targetPtr);
					p.contents = Arrays.copyOf(target.toBytes(), p.contents.length);
					int deadPointer = pointers[deletionIndex+1];
					// delete key and pointer from parent, shift elements left to maintain continuity
					for (int i=deletionIndex+1; i < keys.length; i++) {
						keys[i-1] = keys[i];
						pointers[i] = pointers[i+1];
						keys[i] = -1;
						pointers[i+1] = -1;
					}
					pages.deletePage(deadPointer);
					if (numElements() < Math.ceil(treeOrder/2.0)) {
						// this internal node is now underflowed. Throw exception.
						if (!isRoot() || (numElements() < 2 && isRoot())) { // if this is the root, only underflow when fewer than 2 pointers exist
							throw new InternalUnderflowException();
						}
					}
				}
				else {
					throw new Exception("The root might do this, but I'll find out in debugging.");
				}
			}
		}
	}
}
