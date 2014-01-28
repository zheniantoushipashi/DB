package Store;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.List;

import Store.StorageDiskMapped;

public class StorageDisk implements  Storage{
	  private  ArrayList<RandomAccessFile>  rafs=new ArrayList<RandomAccessFile>();
	  private  ArrayList<RandomAccessFile> rafsTranslation=new ArrayList<RandomAccessFile>();
	  private  String  fileName;
	  private  long   lastPageNumber =  Long.MIN_VALUE;
	  private  boolean  readonly;
	  private  boolean  lockingDisabled;
	  
	  public StorageDisk(String fileName,boolean readonly, boolean lockingDisabled) throws IOException {
	        this.fileName = fileName;
	        this.readonly = readonly;
	        this.lockingDisabled = lockingDisabled;
	        //make sure first file can be opened
	        //lock it
	        try {
	            if(!readonly && !lockingDisabled)
	                getRaf(0).getChannel().tryLock();
	        } catch (IOException e) {
	            throw new IOException("Could not lock DB file: " + fileName, e);
	        } catch (OverlappingFileLockException e) {
	            throw new IOException("Could not lock DB file: " + fileName, e);
	        }

	    }
	 
	  RandomAccessFile getRaf(long pageNumber) throws IOException {

	        int fileNumber = (int) (Math.abs(pageNumber)/PAGES_PER_FILE );

	        List<RandomAccessFile> c = pageNumber>=0 ? rafs : rafsTranslation;

	        //increase capacity of array lists if needed
	        for (int i = c.size(); i <= fileNumber; i++) {
	            c.add(null);
	        }

	        RandomAccessFile ret = c.get(fileNumber);
	        if (ret == null) {
	            String name = StorageDiskMapped.makeFileName(fileName, pageNumber, fileNumber);
	            ret = new RandomAccessFile(name, readonly?"r":"rw");
	            c.set(fileNumber, ret);
	        }
	        return ret;

	    }

	 
	public void write(long pageNumber, ByteBuffer data) throws IOException {
		if(data.capacity() != PAGE_SIZE) throw new IllegalArgumentException();
		long  offset = pageNumber * PAGE_SIZE;
		RandomAccessFile file =getRaf(pageNumber);
		file.seek(Math.abs(offset % (PAGES_PER_FILE* PAGE_SIZE)));
		file.write(data.array());
	    lastPageNumber = pageNumber;
		
	}

	 public ByteBuffer read(long pageNumber) throws IOException {
	        
	        long offset = pageNumber * PAGE_SIZE;
	        ByteBuffer buffer = ByteBuffer.allocate(PAGE_SIZE);
	        
	        RandomAccessFile file = getRaf(pageNumber);
//	        if (lastPageNumber + 1 != pageNumber) //TODO cache position again, so seek is not necessary
	            file.seek(Math.abs(offset % (PAGES_PER_FILE* PAGE_SIZE)));
	        int remaining = buffer.limit();
	        int pos = 0;
	        while (remaining > 0) {
	            int read = file.read(buffer.array(), pos, remaining);
	            if (read == -1) {
	                System.arraycopy(PageFile.CLEAN_DATA, 0, buffer.array(), pos, remaining);
	                break;
	            }
	            remaining -= read;
	            pos += read;
	        }
	        lastPageNumber = pageNumber;
	        return buffer;
	    }
	 
	 

	public boolean isReadonly() {
		// TODO Auto-generated method stub
		return false;
	}

	public DataInputStream readTransactionLog() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteTransactionLog() {
		// TODO Auto-generated method stub
		
	}
	
	 /**
     * Synchronizes the file.
     */
    public void sync() throws IOException {
        for (RandomAccessFile file : rafs)
            if (file != null)
                file.getFD().sync();
        for (RandomAccessFile file : rafsTranslation)
            if (file != null)
                file.getFD().sync();
    }

    public void forceClose() throws IOException {
        for (RandomAccessFile f : rafs) {
            if (f != null)
                f.close();
        }
        rafs = null;
        for (RandomAccessFile f : rafsTranslation) {
            if (f != null)
                f.close();
        }
        rafsTranslation = null;
    }

	

	public DataOutputStream openTransactionLog() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteAllFiles() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
