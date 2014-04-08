package BtreeIndex;

import java.util.Arrays;

import edu.ku.eecs.db.APlusTree.KeyExistsException;
import edu.ku.eecs.db.APlusTree.LeafNodeFullException;
/**
 * @author 孙彪彪
 *
 */
public class PageBtreeLeaf extends PageBtree{
	private int siblingPtr;
	
    public  PageBtreeLeaf(PageTable pages, int order){
    	super(pages, order);
    	keys = new int [treeOrder];
    	Arrays.fill(keys, -1);
    	pointers = new int[treeOrder];
		Arrays.fill(pointers, -1);
		siblingPtr = -1;
    }
   
    @Override
    public  int search(int key){
    	for(int i = 0; i < keys.length; i++){
    		if(key == keys[i]) return  pointers[i];
    	}
    	return -1;
    }
    
    public int insertionPoint(int key) throws KeyExistsException {
		for (int i=0; i<keys.length; i++ ) {
			if (keys[i] >= key || keys[i] == -1) {
				if (keys[i] == key) throw new KeyExistsException();
				return i;
			}
		}
		return numElements();
	}
    
    @Override
	public void insert(int key, int value) throws Exception {
		if (numElements() >= keys.length) {
			// no more room for insertion
			throw new LeafNodeFullException();
		}
		else {
			int insertIndex = numElements();
			for (int i=0; i<numElements(); i++) {
				if (keys[i] >= key || keys[i] == -1) {
					if (keys[i] == key) throw new KeyExistsException();
					insertIndex = i;
					break;
				}
			}
			for (int i=numElements()-1; i >= insertIndex; i--) { // shift values down to make room for insertion
				keys[i+1] = keys[i];
				pointers[i+1] = pointers[i];
			}
			keys[insertIndex] = key;
			pointers[insertIndex] = value;
		}
	}
}
