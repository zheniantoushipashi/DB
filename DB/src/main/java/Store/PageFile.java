package Store;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import javax.crypto.Cipher;
public final class PageFile {
	final PageTransactionManager txnMgr;
	/**
	 * Pages currently locked for read/update ops. When released the page goes
	 * to the dirty or clean list, depending on a flag. The file header page is
	 * normally locked plus the page that is currently being read or modified.
	 * 
	 * @see PageIo#isDirty()
	 */
	public final LongHashMap<PageBuffer> inUse = new LongHashMap<PageBuffer>();

	/**
	 * Pages whose state is dirty.
	 */
	private final LongHashMap<PageBuffer> dirty = new LongHashMap<PageBuffer>();
	/**
	 * Pages in a <em>historical</em> transaction(s) that have been written onto
	 * the log but which have not yet been committed to the database.
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

	PageFile(String fileName, boolean readonly, boolean transactionsDisabled,
			Cipher cipherIn, Cipher cipherOut, boolean useRandomAccessFile,
			boolean lockingDisabled) throws IOException {
		this.cipherIn = cipherIn;
		this.cipherOut = cipherOut;
		this.transactionsDisabled = transactionsDisabled;
		this.storage = new StorageDisk(fileName, readonly, lockingDisabled);
		if (!readonly && !transactionsDisabled) {
			txnMgr = new PageTransactionManager(this, storage, cipherIn,
					cipherOut);
		} else {
			txnMgr = null;
		}
	}

	public PageFile(String filename) throws IOException {
		this(filename, false, false, null, null, false, false);
	}

	// get a page from a file
	public PageBuffer get(long pageId) throws IOException {
		PageBuffer node = inTxn.get(pageId);
		if (node != null) {
			inTxn.remove(pageId);
			inUse.put(pageId, node);
			return node;
		}
		node = dirty.get(pageId);
		if (node != null) {
			dirty.remove(pageId);
			inUse.put(pageId, node);
			return node;
		}
		if (inUse.get(pageId) != null) {
			throw new Error("同一个页面get两次 " + pageId);
		}

		// read node from file
		if (cipherOut == null) {
			node = new PageBuffer(pageId, storage.read(pageId));
		}

		inUse.put(pageId, node);
		node.setClean();
		return node;

	}

	/**
	 * Releases a page.
	 * 
	 * @param pageId
	 *            The record number to release.
	 * @param isDirty
	 *            If true, the page was modified since the get().
	 */
	public void release(final long pageId, final boolean isDirty)
			throws IOException {

		final PageBuffer page = inUse.remove(pageId);
		if (!page.isDirty() && isDirty)
			page.setDirty();

		if (page.isDirty()) {
			dirty.put(pageId, page);
		} else if (!transactionsDisabled && page.isInTransaction()) {
			inTxn.put(pageId, page);
		}
	}

	/**
	 * Releases a page.
	 * 
	 * @param page
	 *            The page to release.
	 */
	public void release(final PageBuffer page) throws IOException {
		final long key = page.getPageId();
		inUse.remove(key);
		if (page.isDirty()) {
			// System.out.println( "Dirty: " + key + page );
			dirty.put(key, page);
		} else if (!transactionsDisabled && page.isInTransaction()) {
			inTxn.put(key, page);
		}
	}

	void freePage(final long pageId, final boolean isDirty) throws IOException {
		final PageBuffer page = inUse.remove(pageId);
		if (!page.isDirty() && isDirty)
			page.setDirty();

		if (page.isDirty()) {
			dirty.put(pageId, page);
		}

	}

	void discard(PageBuffer page) {
		long key = page.getPageId();
		inUse.remove(key);
	}

	/**
	 * Commits the current transaction by flushing all dirty buffers to disk.
	 */
	void commit() throws IOException {

		// sort pages by IDs
		long[] pageIds = new long[dirty.size()];
		int c = 0;
		for (Iterator<PageBuffer> i = dirty.valuesIterator(); i.hasNext();) {
			pageIds[c] = i.next().getPageId();
			c++;
		}
		Arrays.sort(pageIds);

		for (long pageId : pageIds) {
			PageBuffer node = dirty.get(pageId);

			// System.out.println("node " + node + " map size now " +
			// dirty.size());
			if (transactionsDisabled) {
				if (cipherIn != null)
					storage.write(
							node.getPageId(),
							ByteBuffer.wrap(Utils.encrypt(cipherIn,
									node.getData())));
				else
					storage.write(node.getPageId(), node.getData());
				node.setClean();
			} else {
				txnMgr.add(node);
				inTxn.put(node.getPageId(), node);
			}
		}
		dirty.clear();
		if (!transactionsDisabled) {
			txnMgr.commit();
		}
	}

	/**
	 * Rollback the current transaction by discarding all dirty buffers
	 */
	void rollback() throws IOException {
		// debugging...
		if (!inUse.isEmpty()) {
			showList(inUse.valuesIterator());
			throw new Error("in use list not empty at rollback time ("
					+ inUse.size() + ")");
		}
		// System.out.println("rollback...");
		dirty.clear();

		txnMgr.synchronizeLogFromDisk();

		if (!inTxn.isEmpty()) {
			showList(inTxn.valuesIterator());
			throw new Error("in txn list not empty at rollback time ("
					+ inTxn.size() + ")");
		}
		;
	}

	/**
	 * Commits and closes file.
	 */
	public void close() throws IOException {
		if (!dirty.isEmpty()) {
			commit();
		}

		if (!transactionsDisabled && txnMgr != null) {
			txnMgr.shutdown();
		}

		if (!inTxn.isEmpty()) {
			showList(inTxn.valuesIterator());
			throw new Error("In transaction not empty");
		}

		// these actually ain't that bad in a production release
		if (!dirty.isEmpty()) {
			System.out.println("ERROR: dirty pages at close time");
			showList(dirty.valuesIterator());
			throw new Error("Dirty pages at close time");
		}
		if (!inUse.isEmpty()) {
			System.out.println("ERROR: inUse pages at close time");
			showList(inUse.valuesIterator());
			throw new Error("inUse pages  at close time");
		}

		storage.sync();
		storage.forceClose();
	}

	/**
	 * Force closing the file and underlying transaction manager. Used for
	 * testing purposed only.
	 */
	void forceClose() throws IOException {
		if (!transactionsDisabled) {
			txnMgr.forceClose();
		}
		storage.forceClose();
	}

	/**
	 * Prints contents of a list
	 */
	private void showList(Iterator<PageBuffer> i) {
		int cnt = 0;
		while (i.hasNext()) {
			System.out.println("elem " + cnt + ": " + i.next());
			cnt++;
		}
	}

	/**
	 * Synchs a node to disk. This is called by the transaction manager's
	 * synchronization code.
	 */
	void synch(PageBuffer node) throws IOException {
		ByteBuffer data = node.getData();
		if (data != null) {
			if (cipherIn != null)
				storage.write(node.getPageId(),
						ByteBuffer.wrap(Utils.encrypt(cipherIn, data)));
			else
				storage.write(node.getPageId(), data);
		}
	}

	/**
	 * Releases a node from the transaction list, if it was sitting there.
	 */
	void releaseFromTransaction(PageBuffer node) throws IOException {
		inTxn.remove(node.getPageId());
	}

	/**
	 * Synchronizes the file.
	 */
	void sync() throws IOException {
		storage.sync();
	}

	public int getDirtyPageCount() {
		return dirty.size();
	}

	public void deleteAllFiles() throws IOException {
		storage.deleteAllFiles();
	}

}
