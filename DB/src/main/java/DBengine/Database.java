package DBengine;

import java.io.IOException;

import Serializer.DataInputOutput;
import Serializer.Serialization;
import Serializer.Serializer;
import Store.RecordManager;

public class Database {
	RecordManager recordManager ;
	Serializer  serializer;
	public   Database(String  filename) throws Exception{
		this.recordManager = new  RecordManager(filename);
		this.serializer = new  Serialization();
	}
	
	public <A> long  insert(A obj, Serializer<A> serializer, DataInputOutput buf) throws Exception{
		buf.reset();
		long phyRowId;
		serializer.serialize(buf, obj);
		return phyRowId = recordManager.insert(buf.getBuf());
	}
	
	
	public<A>  A  fetch(long  recid, final  Serializer<A> serializer,  final DataInputOutput buf ) throws IOException, ClassNotFoundException{
		byte[]  buf1 = buf.getBuf();
		buf.read(recordManager.fetch((int)recid));
		return serializer.deserialize(buf); 
	}
	
}
