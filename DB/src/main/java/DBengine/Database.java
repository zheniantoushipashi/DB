package DBengine;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import Serializer.DataInputOutput;
import Serializer.LongPacker;
import Serializer.ObjectClassInfo;
import Serializer.SerialClassInfo1;
import Serializer.Serialization;
import Serializer.Serializer;
import Store.RecordManager;

public class Database {
	RecordManager recordManager ;
	public Serializer  serializer;
	ArrayList<ObjectClassInfo> registered = new ArrayList<ObjectClassInfo>();
	public   Database(String  filename) throws Exception{
		this.recordManager = new  RecordManager();
		this.serializer = new  Serialization(registered);
	}
	
	 /** we need to set reference to this DB instance, so serializer needs to be here*/
	/*
    final Serializer<Serialization> defaultSerializationSerializer = new Serializer<Serialization>(){

        public void serialize(DataOutput out, Serialization obj) throws IOException {
            LongPacker.packLong(out,obj.serialClassInfoRecid);
            SerialClassInfo1.serializer.serialize(out,obj.registered);
        }

        public Serialization deserialize(DataInput in) throws IOException, ClassNotFoundException {
            final long recid = LongPacker.unpackLong(in);
            final ArrayList<SerialClassInfo.ClassInfo> classes = SerialClassInfo.serializer.deserialize(in);
            return new Serialization(DBAbstract.this,recid,classes);
        }
    };
	*/
	public <A> long  insert(A obj, Serializer<A> serializer, DataInputOutput buf) throws Exception{
		buf.reset();
		long phyRowId;
		serializer.serialize(buf, obj);
		return phyRowId = recordManager.insert(buf.getBuf());
	}
	
	
	public<A>  A  fetch(long  recid, final  Serializer<A> serializer,  final DataInputOutput buf ) throws IOException, ClassNotFoundException{
		buf.read(recordManager.fetch((int)recid));
		return serializer.deserialize(buf); 
	}
	
	public<A>  void  update(long recid, final A obj, final  Serializer<A> serializer, final DataInputOutput buf) throws Exception{
	           buf.reset();
	           serializer.serialize(buf, obj);
	           recordManager.update((int)recid, buf.getBuf());
	           
	}
	
}
