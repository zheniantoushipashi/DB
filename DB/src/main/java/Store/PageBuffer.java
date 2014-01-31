package Store;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.crypto.Cipher;
import ValueType.Value;
public final class PageBuffer {
	    private  long  pageId;
        private  ByteBuffer  data;
        private  boolean  dirty = false;
        private  int  transactionCount = 0;
	    public PageBuffer() {
	        // empty
	    }

	    public PageBuffer(long pageId, ByteBuffer data) {
	        this.pageId = pageId;
	        this.data = data;
	    }
	    
	    PageBuffer(long pageId, byte[] data){
	    	
	    	this.pageId=pageId;
	    	this.data=ByteBuffer.wrap(data);
	    }
	    
	    void ensureHeapBuffer(){
	        if(data.isDirect()){
	            final byte[] bb = new byte[Storage.PAGE_SIZE];
	            data.get(bb,0,Storage.PAGE_SIZE);
	            data = ByteBuffer.wrap(bb);
	            if(data.isReadOnly()) throw new InternalError();
	        }

	    }
	    
	    /**
	     * Increments transaction count for this block, to signal that this
	     * block is in the log but not yet in the data file. The method also
	     * takes a snapshot so that the data may be modified in new transactions.
	     */
	    void incrementTransactionCount() {
	        transactionCount++;
	    }

	    /**
	     * Decrements transaction count for this block, to signal that this
	     * block has been written from the log to the data file.
	     */
	    void decrementTransactionCount() {
	        transactionCount--;
	        if (transactionCount < 0)
	            throw new Error("transaction count on page "
	                    + getPageId() + " below zero!");

	    }

	    

	    /**
	     * Calculate the number of bytes required to encode the given value.
	     *
	     * @param v the value
	     * @param handler the data handler for lobs
	     * @return the number of bytes required to store this value
	     */
	    public static int getValueLen(Value v) {
	        if (v == ValueNull.INSTANCE) {
	            return 1;
	        }
	        switch (v.getType()) {
	        case Value.BOOLEAN:
	            return 1;
	        case Value.BYTE:
	            return 2;
	        case Value.SHORT:
	            return 3;
	        case Value.INT: {
	            int x = v.getInt();
	            if (x < 0) {
	                return 1 + getVarIntLen(-x);
	            } else if (x < 16) {
	                return 1;
	            } else {
	                return 1 + getVarIntLen(x);
	            }
	        }
	        case Value.LONG: {
	            long x = v.getLong();
	            if (x < 0) {
	                return 1 + getVarLongLen(-x);
	            } else if (x < 8) {
	                return 1;
	            } else {
	                return 1 + getVarLongLen(x);
	            }
	        }
	        case Value.DOUBLE: {
	            double x = v.getDouble();
	            if (x == 1.0d) {
	                return 1;
	            }
	            long d = Double.doubleToLongBits(x);
	            if (d == ValueDouble.ZERO_BITS) {
	                return 1;
	            }
	            return 1 + getVarLongLen(Long.reverse(d));
	        }
	        case Value.FLOAT: {
	            float x = v.getFloat();
	            if (x == 1.0f) {
	                return 1;
	            }
	            int f = Float.floatToIntBits(x);
	            if (f == ValueFloat.ZERO_BITS) {
	                return 1;
	            }
	            return 1 + getVarIntLen(Integer.reverse(f));
	        }
	        case Value.STRING: {
	            String s = v.getString();
	            int len = s.length();
	            if (len < 32) {
	                return 1 + getStringWithoutLengthLen(s, len);
	            }
	            return 1 + getStringLen(s);
	        }
	        case Value.STRING_IGNORECASE:
	        case Value.STRING_FIXED:
	            return 1 + getStringLen(v.getString());
	        case Value.DECIMAL: {
	            BigDecimal x = v.getBigDecimal();
	            if (BigDecimal.ZERO.equals(x)) {
	                return 1;
	            } else if (BigDecimal.ONE.equals(x)) {
	                return 1;
	            }
	            int scale = x.scale();
	            BigInteger b = x.unscaledValue();
	            int bits = b.bitLength();
	            if (bits <= 63) {
	                if (scale == 0) {
	                    return 1 + getVarLongLen(b.longValue());
	                }
	                return 1 + getVarIntLen(scale) + getVarLongLen(b.longValue());
	            }
	            byte[] bytes = b.toByteArray();
	            return 1 + getVarIntLen(scale) + getVarIntLen(bytes.length) + bytes.length;
	        }
	        case Value.TIME:
	            if (SysProperties.STORE_LOCAL_TIME) {
	                long nanos = ((ValueTime) v).getNanos();
	                long millis = nanos / 1000000;
	                nanos -= millis * 1000000;
	                return 1 + getVarLongLen(millis) + getVarLongLen(nanos);
	            }
	            return 1 + getVarLongLen(DateTimeUtils.getTimeLocalWithoutDst(v.getTime()));
	        case Value.DATE: {
	            if (SysProperties.STORE_LOCAL_TIME) {
	                long dateValue = ((ValueDate) v).getDateValue();
	                return 1 + getVarLongLen(dateValue);
	            }
	            long x = DateTimeUtils.getTimeLocalWithoutDst(v.getDate());
	            return 1 + getVarLongLen(x / MILLIS_PER_MINUTE);
	        }
	        case Value.TIMESTAMP: {
	            if (SysProperties.STORE_LOCAL_TIME) {
	                ValueTimestamp ts = (ValueTimestamp) v;
	                long dateValue = ts.getDateValue();
	                long nanos = ts.getNanos();
	                long millis = nanos / 1000000;
	                nanos -= millis * 1000000;
	                return 1 + getVarLongLen(dateValue) + getVarLongLen(millis) + getVarLongLen(nanos);
	            }
	            Timestamp ts = v.getTimestamp();
	            return 1 + getVarLongLen(DateTimeUtils.getTimeLocalWithoutDst(ts)) + getVarIntLen(ts.getNanos());
	        }
	        case Value.JAVA_OBJECT: {
	            byte[] b = v.getBytesNoCopy();
	            return 1 + getVarIntLen(b.length) + b.length;
	        }
	        case Value.BYTES: {
	            byte[] b = v.getBytesNoCopy();
	            int len = b.length;
	            if (len < 32) {
	                return 1 + b.length;
	            }
	            return 1 + getVarIntLen(b.length) + b.length;
	        }
	        case Value.UUID:
	            return 1 + LENGTH_LONG + LENGTH_LONG;
	        case Value.BLOB:
	        case Value.CLOB: {
	            int len = 1;
	            if (v instanceof ValueLob) {
	                ValueLob lob = (ValueLob) v;
	                lob.convertToFileIfRequired(handler);
	                byte[] small = lob.getSmall();
	                if (small == null) {
	                    int t = -1;
	                    if (!lob.isLinked()) {
	                        t = -2;
	                    }
	                    len += getVarIntLen(t);
	                    len += getVarIntLen(lob.getTableId());
	                    len += getVarIntLen(lob.getObjectId());
	                    len += getVarLongLen(lob.getPrecision());
	                    len += 1;
	                    if (t == -2) {
	                        len += getStringLen(lob.getFileName());
	                    }
	                } else {
	                    len += getVarIntLen(small.length);
	                    len += small.length;
	                }
	            } else {
	                ValueLobDb lob = (ValueLobDb) v;
	                byte[] small = lob.getSmall();
	                if (small == null) {
	                    len += getVarIntLen(-3);
	                    len += getVarIntLen(lob.getTableId());
	                    len += getVarLongLen(lob.getLobId());
	                    len += getVarLongLen(lob.getPrecision());
	                } else {
	                    len += getVarIntLen(small.length);
	                    len += small.length;
	                }
	            }
	            return len;
	        }
	        case Value.ARRAY: {
	            Value[] list = ((ValueArray) v).getList();
	            int len = 1 + getVarIntLen(list.length);
	            for (Value x : list) {
	                len += getValueLen(x, handler);
	            }
	            return len;
	        }
	        case Value.RESULT_SET: {
	            int len = 1;
	            try {
	                ResultSet rs = ((ValueResultSet) v).getResultSet();
	                rs.beforeFirst();
	                ResultSetMetaData meta = rs.getMetaData();
	                int columnCount = meta.getColumnCount();
	                len += getVarIntLen(columnCount);
	                for (int i = 0; i < columnCount; i++) {
	                    len += getStringLen(meta.getColumnName(i + 1));
	                    len += getVarIntLen(meta.getColumnType(i + 1));
	                    len += getVarIntLen(meta.getPrecision(i + 1));
	                    len += getVarIntLen(meta.getScale(i + 1));
	                }
	                while (rs.next()) {
	                    len++;
	                    for (int i = 0; i < columnCount; i++) {
	                        int t = DataType.convertSQLTypeToValueType(meta.getColumnType(i + 1));
	                        Value val = DataType.readValue(null, rs, i + 1, t);
	                        len += getValueLen(val, handler);
	                    }
	                }
	                len++;
	                rs.beforeFirst();
	            } catch (SQLException e) {
	                throw DbException.convert(e);
	            }
	            return len;
	        }
	        case Value.GEOMETRY: {
	            byte[] b = v.getBytesNoCopy();
	            int len = b.length;
	            return 1 + getVarIntLen(len) + len;
	        }
	        default:
	            throw DbException.throwInternalError("type=" + v.getType());
	        }
	    }
	    

	    public void writeExternal(DataOutput out, Cipher cipherIn) throws IOException {
	        out.writeLong(pageId);
	        out.write(Utils.encrypt(cipherIn, data.array()));
	    }
	    
	    /**
	     * Returns true if the block is still dirty with respect to the
	     * transaction log.
	     */
	    boolean isInTransaction() {
	        return transactionCount != 0;
	    }

	    
	    ByteBuffer getData() {
	        return data;
	    }

	    /**
	     * Returns the page number.
	     */
	    long getPageId() {
	        return pageId;
	    }
	    
	    void setDirty() {
	        dirty = true;
	        
	        if(data.isReadOnly()){
	            // make copy if needed, so we can write into buffer
	            byte[] buf = new byte[Storage.PAGE_SIZE];
	            data.get(buf,0,Storage.PAGE_SIZE);
	            data = ByteBuffer.wrap(buf);
	        }
	    }

	    void setClean() {
	        dirty = false;
	    }

	    /**
	     * Returns true if the dirty flag is set.
	     */
	    boolean isDirty() {
	        return dirty;
	    }

	    /**
	     * Reads a byte from the indicated position
	     */
	    public byte readByte(int pos) {
	        return data.get(pos);
	    }

	    /**
	     * Writes a byte to the indicated position
	     */
	    public void writeByte(int pos, byte value) {
	        setDirty();
	        data.put(pos,value);
	    }

	    /**
	     * Reads a short from the indicated position
	     */
	    public short readShort(int pos) {
	        return data.getShort(pos);
	    }

	    /**
	     * Writes a short to the indicated position
	     */
	    public void writeShort(int pos, short value) {
	        setDirty();
	        data.putShort(pos,value);
	    }

	    /**
	     * Reads an int from the indicated position
	     */
	    public int readInt(int pos) {
	        return data.getInt(pos);
	    }

	    /**
	     * Writes an int to the indicated position
	     */
	    public void writeInt(int pos, int value) {
	        setDirty();
	        data.putInt(pos,value);
	    }

	    /**
	     * Reads a long from the indicated position
	     */
	    public long readLong(int pos) {
	        return data.getLong(pos);
	    }

	    /**
	     * Writes a long to the indicated position
	     */
	    public void writeLong(int pos, long value) {
	        setDirty();
	        data.putLong(pos,value);
	    }

	    public long readSixByteLong(int pos) {
	        long ret =                 
	                ((long) (data.get(pos + 0) & 0x7f) << 40) |
	                ((long) (data.get(pos + 1) & 0xff) << 32) |
	                ((long) (data.get(pos + 2) & 0xff) << 24) |
	                ((long) (data.get(pos + 3) & 0xff) << 16) |
	                ((long) (data.get(pos + 4) & 0xff) << 8) |
	                ((long) (data.get(pos + 5) & 0xff) << 0);
	        if((data.get(pos + 0) & 0x80) != 0)
	            return -ret;
	        else
	            return ret;

	    }

	    /**
	     * Writes a long to the indicated position
	     */
	    public void writeSixByteLong(int pos, long value) {
//	        if(value<0) throw new IllegalArgumentException();
//	    	if(value >> (6*8)!=0)
//	    		throw new IllegalArgumentException("does not fit");
	        int negativeBit = 0;
	        if(value<0){
	            value = -value;
	            negativeBit = 0x80;
	        }

	        setDirty();
	        data.put(pos + 0,(byte) ((0x7f & (value >> 40)) | negativeBit));
	        data.put(pos + 1, (byte) (0xff & (value >> 32)));
	        data.put(pos + 2, (byte) (0xff & (value >> 24)));
	        data.put(pos + 3, (byte) (0xff & (value >> 16)));
	        data.put(pos + 4, (byte) (0xff & (value >> 8)));
	        data.put(pos + 5, (byte) (0xff & (value >> 0)));

	    }


	    // overrides java.lang.Object

	    public String toString() {
	        return "PageIo("
	                + pageId + ","
	                + dirty +")";
	    }
	   
	    public  void readExternal(DataInputStream in,Cipher  cipherOut) throws IOException{
	    	pageId=in.readLong();
	    	byte[] data2 = new byte[Storage.PAGE_SIZE];
	    	in.readFully(data2);
	    	 data = ByteBuffer.wrap(data2);
	    	
	    	
	    }
	    
	    
	    public byte[] getByteArray() {
	        if ( data.hasArray())
	            return data.array();
	        byte[] d= new byte[Storage.PAGE_SIZE];
	        data.rewind();
	        data.get(d,0,Storage.PAGE_SIZE);
	        return d;
	    }

	    public void writeByteArray(byte[] buf, int srcOffset, int offset, int length) {
	        setDirty();
	        data.rewind();
	        data.position(offset);
	        data.put(buf,srcOffset,length);
	    }
	  

	    
	    

}
