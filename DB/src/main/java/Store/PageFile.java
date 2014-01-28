package Store;

import java.io.IOException;

import javax.crypto.Cipher;

public final class PageFile {
	/**
     * Pages currently locked for read/update ops. When released the page goes
     * to the dirty or clean list, depending on a flag.  The file header page  is
     * normally locked plus the page that is currently being read or modified.
     *
     * @see PageIo#isDirty()
     */
    private final LongHashMap<PageBuffer> inUse = new LongHashMap<PageBuffer>();

    /**
     * Pages whose state is dirty.
     */
    private final LongHashMap<PageBuffer> dirty = new LongHashMap<PageBuffer>();
    /**
     * Pages in a <em>historical</em> transaction(s) that have been written
     * onto the log but which have not yet been committed to the database.
     */
    private final LongHashMap<PageBuffer> inTxn = new LongHashMap<PageBuffer>();


    // transactions disabled?
    final boolean transactionsDisabled;

    /**
     * A array of clean data to wipe clean pages.
     */
    static final byte[] CLEAN_DATA = new byte[Storage.PAGE_SIZE];


    final Storage storage;
    private Cipher cipherOut;
    private Cipher cipherIn;
   
    PageFile(String  fileName, boolean readonly , boolean  transactionsDisabled, Cipher cipherIn, Cipher cipherOut, boolean  useRandomAccessFile, boolean lockingDisabled) throws IOException{
    	this.cipherIn = cipherIn;
        this.cipherOut = cipherOut;
        this.transactionsDisabled = transactionsDisabled;
        this.storage = new StorageDisk(fileName,readonly,lockingDisabled);
    }
    
    //get  a  page  from  a  file
    

	

}
