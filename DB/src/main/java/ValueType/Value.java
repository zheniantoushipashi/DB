package ValueType;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
public  abstract class Value {
	
	 /**
     * The data type is unknown at this time.
     */
    public static final int UNKNOWN = -1;
	 /**
     * The value type for NULL.
     */
    public static final int NULL = 0;

    /**
     * The value type for BOOLEAN values.
     */
    public static final int BOOLEAN = 1;

    /**
     * The value type for BYTE values.
     */
    public static final int BYTE = 2;

    /**
     * The value type for SHORT values.
     */
    public static final int SHORT = 3;

    /**
     * The value type for INT values.
     */
    public static final int INT = 4;

    /**
     * The value type for LONG values.
     */
    public static final int LONG = 5;

    /**
     * The value type for DECIMAL values.
     */
    public static final int DECIMAL = 6;

    /**
     * The value type for DOUBLE values.
     */
    public static final int DOUBLE = 7;

    /**
     * The value type for FLOAT values.
     */
    public static final int FLOAT = 8;

    /**
     * The value type for TIME values.
     */
    public static final int TIME = 9;

    /**
     * The value type for DATE values.
     */
    public static final int DATE = 10;

    /**
     * The value type for TIMESTAMP values.
     */
    public static final int TIMESTAMP = 11;

    /**
     * The value type for BYTES values.
     */
    public static final int BYTES = 12;

    /**
     * The value type for STRING values.
     */
    public static final int STRING = 13;

    /**
     * The value type for case insensitive STRING values.
     */
    public static final int STRING_IGNORECASE = 14;

    /**
     * The value type for BLOB values.
     */
    public static final int BLOB = 15;

    /**
     * The value type for CLOB values.
     */
    public static final int CLOB = 16;

    /**
     * The value type for ARRAY values.
     */
    public static final int ARRAY = 17;

    /**
     * The value type for RESULT_SET values.
     */
    public static final int RESULT_SET = 18;
    /**
     * The value type for JAVA_OBJECT values.
     */
    public static final int JAVA_OBJECT = 19;

    /**
     * The value type for UUID values.
     */
    public static final int UUID = 20;

    /**
     * The value type for string values with a fixed size.
     */
    public static final int STRING_FIXED = 21;

    /**
     * The value type for string values with a fixed size.
     */
    public static final int GEOMETRY = 22;

    /**
     * The number of value types.
     */
    public static final int TYPE_COUNT = GEOMETRY + 1;
    
    public  abstract  int  getType();
    
    protected abstract int compareSecure(Value v);
    
    /**
     * Get the value as a string.
     *
     * @return the string
     */
    public abstract String getString();

    /**
     * Get the value as an object.
     *
     * @return the object
     */
    public abstract Object getObject();
    
    public abstract int hashCode();
    
    
    public abstract boolean equals(Object other);

    
    static int getOrder(int type) {
    	//当两个值需要发生转换时，order数字小的要转到数字大的，比如a是int，b是long，int是23，long是24，那么在做运算时a要转成long
        switch(type) {
        case UNKNOWN:
            return 1;
        case NULL:
            return 2;
        case STRING:
            return 10;
        case CLOB:
            return 11;
        case STRING_FIXED:
            return 12;
        case STRING_IGNORECASE:
            return 13;
        case BOOLEAN:
            return 20;
        case BYTE:
            return 21;
        case SHORT:
            return 22;
        case INT:
            return 23;
        case LONG:
            return 24;
        case DECIMAL:
            return 25;
        case FLOAT:
            return 26;
        case DOUBLE:
            return 27;
        case TIME:
            return 30;
        case DATE:
            return 31;
        case TIMESTAMP:
            return 32;
        case BYTES:
            return 40;
        case BLOB:
            return 41;
        case UUID:
            return 42;
        case JAVA_OBJECT:
            return 43;
        case GEOMETRY:
            return 44;
        case ARRAY:
            return 50;
        case RESULT_SET:
            return 51;
        default:
            return -1;
        }
    }
    
    public static int getHigherOrder(int t1, int t2) {
        if (t1 == t2) {
            return t1;
        }
        int o1 = getOrder(t1);
        int o2 = getOrder(t2);
        return o1 > o2 ? t1 : t2;
    }

	public int getInt() {
		// TODO Auto-generated method stub
		return 0;
	}
	

    public Boolean getBoolean() {
        return ((ValueBoolean) convertTo(Value.BOOLEAN)).getBoolean();
    }

    public Date getDate() {
        return ((ValueDate) convertTo(Value.DATE)).getDate();
    }

    public Time getTime() {
        return ((ValueTime) convertTo(Value.TIME)).getTime();
    }

    public Timestamp getTimestamp() {
        return ((ValueTimestamp) convertTo(Value.TIMESTAMP)).getTimestamp();
    }

    public byte[] getBytes() {
        return ((ValueBytes) convertTo(Value.BYTES)).getBytes();
    }

    public byte[] getBytesNoCopy() {
        return ((ValueBytes) convertTo(Value.BYTES)).getBytesNoCopy();
    }

    public byte getByte() {
        return ((ValueByte) convertTo(Value.BYTE)).getByte();
    }

    public short getShort() {
        return ((ValueShort) convertTo(Value.SHORT)).getShort();
    }

    public BigDecimal getBigDecimal() {
        return ((ValueDecimal) convertTo(Value.DECIMAL)).getBigDecimal();
    }

    public double getDouble() {
        return ((ValueDouble) convertTo(Value.DOUBLE)).getDouble();
    }

    public float getFloat() {
        return ((ValueFloat) convertTo(Value.FLOAT)).getFloat();
    }

    public int getInt() {
        return ((ValueInt) convertTo(Value.INT)).getInt();
    }

    public long getLong() {
        return ((ValueLong) convertTo(Value.LONG)).getLong();
    }

    
    private static byte convertToByte(long x) {
      
        return (byte) x;
    }

    private static short convertToShort(long x) {
      
        return (short) x;
    }

    private static int convertToInt(long x) {
      
        return (int) x;
    }

    private static long convertToLong(double x) {
       
        return Math.round(x);
    }

    private static long convertToLong(BigDecimal x) {
      
        return x.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
    }

	
	
	 /**
     * Compare a value to the specified type.
     *
     * @param targetType the type of the returned value
     * @return the converted value
     */
	 public Value convertTo(int targetType) {
		 
     switch (targetType){
	 case INT: {
         switch (getType()) {
         case BOOLEAN:
             return Int.get(getBoolean().booleanValue() ? 1 : 0);
         case BYTE:
             return Int.get(getByte());
         case SHORT:
             return Int.get(getShort());
         case LONG:
             return Int.get(convertToInt(getLong()));
         case DECIMAL:
             return Int.get(convertToInt(convertToLong(getBigDecimal())));
         case DOUBLE:
             return Int.get(convertToInt(convertToLong(getDouble())));
         case FLOAT:
             return Int.get(convertToInt(convertToLong(getFloat())));
         case BYTES:
             return Int.get((int) Long.parseLong(getString(), 16));
         }
         break;
      
	    }
      }
	 
	 }

   
    
    

}
