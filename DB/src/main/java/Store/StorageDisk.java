package Store;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class StorageDisk implements  Storage{
	 private ArrayList<RandomAccessFile> rafs = new ArrayList<RandomAccessFile>();
	    private ArrayList<RandomAccessFile> rafsTranslation = new ArrayList<RandomAccessFile>();

	    private String fileName;

	    private long lastPageNumber = Long.MIN_VALUE;
	    private boolean readonly;
	    private boolean lockingDisabled;
	  private  ArrayList<RandomAccessFile>  rafs=new ArrayList<RandomAccessFile>(); 
	
	

	public void write(long pageNumber, ByteBuffer data) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public ByteBuffer read(long pageNumber) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void forceClose() throws IOException {
		// TODO Auto-generated method stub
		
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

	public void sync() throws IOException {
		// TODO Auto-generated method stub
		
	}

	public DataOutputStream openTransactionLog() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteAllFiles() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
