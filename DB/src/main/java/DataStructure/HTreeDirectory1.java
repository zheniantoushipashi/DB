package DataStructure;

import java.io.IOException;

public class HTreeDirectory1 <K, V> {

	static final int MAX_CHILDREN = 256;

	static final int BIT_SIZE = 8; // log2(256) = 8
	static final int MAX_DEPTH = 3; // 4 levels
	private long[][] _children;
	private byte _depth;
	private long _recid;
	long size;

	protected final HTree<K, V> tree;
	
	public HTreeDirectory1(HTree<K, V> tree) {
        this.tree = tree;
    }
	
	  HTreeDirectory1(HTree<K, V> tree, byte depth) {
	        this.tree = tree;
	        _depth = depth;
	        _children = new long[32][];
	    }

	  
	  /**
	     * Get the record identifier used to load this hashtable.
	     */
	    long getRecid() {
	        return _recid;
	    }
	    boolean  isEmpty(){
	    	
	    	for(int i = 0; i < _children.length; i++){
	    		long []  sub =  _children[i];
	    		if(sub != null){
	    			for( int j = 0; j < 8; j++){
	    				if(sub[j] != 0){
	    					return false;
	    				}
	    			}
	    		}
	    	}
	    	return true;
	    }
   
	    V get(K key)
	            throws IOException {
	        int hash = hashCode(key);
	        long child_recid = getRecid(hash);
	        if (child_recid == 0) {
	            // not bucket/node --> not found
	            return null;
	        } else {
	            Object node = tree.db.fetch(child_recid, tree.SERIALIZER);
	            // System.out.println("HashDirectory.get() child is : "+node);

	            if (node instanceof HTreeDirectory) {
	                // recurse into next directory level
	                HTreeDirectory<K, V> dir = (HTreeDirectory<K, V>) node;
	                dir.setPersistenceContext(child_recid);
	                return dir.get(key);
	            } else {
	                // node is a bucket
	                HTreeBucket<K, V> bucket = (HTreeBucket) node;
	                return bucket.getValue(key);
	            }
	        }
	    }
	    
	    V  get (K key) {
	    	int  hash = hashCode(key);
	    	long  child_recid = getRecid(hash);
	    	if(child_recid == 0){
	    		return  null;
	    	}
	    	
	    	else{
	    		System.out.println(child_recid);
	    		return  child_recid;
	    		
	    	}
	    	
	    }
	    /**
	     * Calculates the hashcode of a key, based on the current directory
	     * depth.
	     */
	   
	    private  int hashCode(Object key ){
	    	
	    	int hashMask = hashMash();
	    	int  hash = key.hashCode();
	    	hash = hash & hashMask;
	    	hash = hash >>>((MAX_DEPTH - _depth) * BIT_SIZE);
	    	hash = hash % MAX_CHILDREN;
	    	return hash;
	    }
	    
	    
	    int hashMash(){
	    	int bits = MAX_CHILDREN - 1;
	    	int hashMask = bits << ((MAX_DEPTH - _depth) * BIT_SIZE);
	    	return hashMask;
	    }
	    
	    private  long  getRecid(int hash){
	    	long[] sub = _children[hash >>> 3];
	    	return  sub == null? 0 :sub[hash % 8];
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
}
