package MetaDictionary;

import Util.ByteUtil;

public class DictionaryCreate {
	// 创建数据字典在表空间的对象
	
	
	//把对象序列化为一个Record
	public    byte[]   SerializeMetaObject(Object  o){
        if(o instanceof  Table){
        	Table  table =  (Table)o;
    	    int  headerLen = 16;
    		int  RequiedLen = table.getId().length() + table.getName().length() + 4 + headerLen;
    		byte[]  record = new byte[RequiedLen];
    		byte[]  headerByte = new byte[16];
    		headerByte = constructHeader(table);
    		System.arraycopy(headerByte, 0, record, 0, 16);
    		System.arraycopy(table.getId().getBytes(), 0, record, 16, table.getId().getBytes().length);
    		System.arraycopy(table.getName().getBytes(), 0, record, getValueFormHeader(headerByte, 4), table.getName().length());
    		System.arraycopy(ByteUtil.int2Byte(table.getType()), 0, record, getValueFormHeader(headerByte, 8), ByteUtil.int2Byte(table.getType()).length);
    		return  record;
    	
		}
        return  null;
	}

	//反序列化一个Record为java对象
	public     Object  deserializeMetaRecord(byte[] record){
		Table table = new Table();
		int idStringLen = getValueFormHeader(record, 4) - getValueFormHeader(record, 0);
		byte[]  idString  = new byte[idStringLen];
		System.arraycopy(record, getValueFormHeader(record, 0), idString, 0, idStringLen);
		table.setId(new String(idString));
		int  nameStringLen = getValueFormHeader(record, 8) - getValueFormHeader(record, 4);
		byte[]  nameString = new byte[nameStringLen];
		System.arraycopy(record, getValueFormHeader(record, 4), nameString, 0, nameStringLen);
		table.setName(new String(nameString));
		int typeLen =   getValueFormHeader(record, 12) - getValueFormHeader(record,8);
		byte[] type = new byte[typeLen];
		System.arraycopy(record, getValueFormHeader(record, 8), type, 0, typeLen);
		table.setType(ByteUtil.byte2Int(type));
	    return  table;
	}
	
	private byte[] constructHeader(Object o) {
		if (o instanceof Table) {
			   Table  table =  (Table)o;
               byte[]  header = new  byte[16];
               System.arraycopy(Util.ByteUtil.int2Byte(16), 0, header, 0, 4);
               
               System.arraycopy(Util.ByteUtil.int2Byte(table.getId().length() + getValueFormHeader(header, 0)), 0, header, 4, 4);
               System.arraycopy(Util.ByteUtil.int2Byte(table.getName().length() + getValueFormHeader(header, 4)), 0, header, 8, 4);
               System.arraycopy(ByteUtil.int2Byte(Util.ByteUtil.int2Byte(table.getType()).length + getValueFormHeader(header, 8)), 0, header, 12, 4);       
               return  header;  
		}
		return null;
	}
	
	private  int getValueFormHeader(byte[]  header, int Pos){
		byte[]  value = new byte[4];
		System.arraycopy(header, Pos, value, 0, 4);
		return  ByteUtil.byte2Int(value);
	}

	// 创建数据字典在内存中的缓存
}
