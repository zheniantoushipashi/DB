package Serializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Serializer.Serialization.FastArrayList;

public class SerialClassInfo1 {
	public  SerialClassInfo1(){
		
	}
	
	public SerialClassInfo1(
			ArrayList<ObjectClassInfo> registered) {
		this.registered = registered;
	}

	static final Serializer<ArrayList<ObjectClassInfo>> serializer = new Serializer<ArrayList<ObjectClassInfo>>() {
		public void serialize(DataOutput out, ArrayList<ObjectClassInfo> obj)
				throws IOException {
			LongPacker.packInt(out, obj.size());
			for (ObjectClassInfo oci : obj) {
				out.writeUTF(oci.getName());
				out.writeBoolean(oci.isEnum);
				out.writeBoolean(oci.isExternalizable);
				if (oci.isExternalizable)
					continue; // no fields
				LongPacker.packInt(out, oci.fields.size());
				for (FieldInfo fi : oci.fields) {
					out.writeUTF(fi.getName());
					out.writeBoolean(fi.isPrimitive());
					out.writeUTF(fi.getType());

				}

			}
		}

		public ArrayList<ObjectClassInfo> deserialize(DataInput in)
				throws IOException, ClassNotFoundException {
			int size = LongPacker.unpackInt(in);
			ArrayList<ObjectClassInfo> ret = new ArrayList<ObjectClassInfo>(
					size);
			for (int i = 0; i < size; i++) {
				String className = in.readUTF();
				boolean isEnum = in.readBoolean();
				boolean isExternalizable = in.readBoolean();
				int fieldsNum = isExternalizable ? 0 : LongPacker.unpackInt(in);
				FieldInfo[] fields = new FieldInfo[fieldsNum];
				for (int j = 0; j < fieldsNum; j++) {
					fields[j] = new FieldInfo(in.readUTF(), in.readBoolean(),
							in.readUTF(), Class.forName(className));
				}
				ret.add(new ObjectClassInfo(className, fields, isEnum,
						isExternalizable));
			}
			return ret;
		}

	};

	private ObjectStreamField[] getFields(Class clazz) {
		ObjectStreamField[] fields = null;
		ObjectClassInfo classInfo = null;
		Integer classId = classToclassId.get(clazz);
		if (classId != null) {
			classInfo = registered.get(classId);
			fields = classInfo.getObjectStreamFields();
		}
		if (fields == null) {
			ObjectStreamClass streamClass = ObjectStreamClass.lookup(clazz);
			FastArrayList<ObjectStreamField> fieldsList = new FastArrayList<ObjectStreamField>();
			while (streamClass != null) {
				for (ObjectStreamField f : streamClass.getFields()) {
					fieldsList.add(f);
				}
				clazz = clazz.getSuperclass();
				streamClass = ObjectStreamClass.lookup(clazz);
			}
			fields = new ObjectStreamField[fieldsList.size()];
			for (int i = 0; i < fields.length; i++) {
				fields[i] = fieldsList.get(i);
			}
			if (classInfo != null)
				classInfo.setObjectStreamFields(fields);
		}
		return fields;
	}

	ArrayList<ObjectClassInfo> registered;
	Map<Class, Integer> classToclassId = new HashMap<Class, Integer>();
	Map<Integer, Class> classIdToclass = new HashMap<Integer, Class>();

	public void registerClass(Class clazz) throws IOException {
		if (clazz != Object.class) {
			assertClassSerializable(clazz);
		}
		if (containsClass(clazz)) {
			return;
		}
		ObjectStreamField[] streamFields = getFields(clazz);
		FieldInfo[] fields = new FieldInfo[streamFields.length];
		for (int i = 0; i < fields.length; i++) {
			ObjectStreamField sf = streamFields[i];
			fields[i] = new FieldInfo(sf, clazz);
		}
		ObjectClassInfo i = new ObjectClassInfo(clazz.getName(), fields,
				clazz.isEnum(), Externalizable.class.isAssignableFrom(clazz));
		classToclassId.put(clazz, registered.size());
		classIdToclass.put(registered.size(), clazz);
		registered.add(i);

	}

	private void assertClassSerializable(Class clazz)
			throws NotSerializableException, InvalidClassException {
		if (containsClass(clazz))
			return;

		if (!Serializable.class.isAssignableFrom(clazz))
			throw new NotSerializableException(clazz.getName());
	}

	public boolean containsClass(Class clazz) {
		return (classToclassId.get(clazz) != null);
	}

	public int getClassId(Class clazz) {
		Integer classId = classToclassId.get(clazz);
		if (classId != null) {
			return classId;
		}
		throw new Error("Class is not registered: " + clazz);
	}

	public Object getFieldValue(String fieldName, Object object) {
		try {
			registerClass(object.getClass());
		} catch (IOException e) {
			e.printStackTrace();
		}
		ObjectClassInfo classInfo = registered.get(classToclassId.get(object
				.getClass()));
		return getFieldValue(classInfo.getField(fieldName), object);
	}

	public Object getFieldValue(FieldInfo fieldInfo, Object object) {

		Object fieldAccessor = fieldInfo.getter;
		try {
			if (fieldAccessor instanceof Method) {
				Method m = (Method) fieldAccessor;
				return m.invoke(object);
			} else {
				Field f = (Field) fieldAccessor;
				return f.get(object);
			}
		} catch (Exception e) {

		}

		throw new NoSuchFieldError(object.getClass() + "."
				+ fieldInfo.getName());
	}

	public void setFieldValue(String fieldName, Object object, Object value) {
		try {
			registerClass(object.getClass());
		} catch (IOException e) {
			e.printStackTrace();
		}
		ObjectClassInfo classInfo = registered.get(classToclassId.get(object
				.getClass()));
		setFieldValue(classInfo.getField(fieldName), object, value);
	}

	public void setFieldValue(FieldInfo fieldInfo, Object object, Object value) {

		Object fieldAccessor = fieldInfo.setter;
		try {
			if (fieldAccessor instanceof Method) {
				Method m = (Method) fieldAccessor;
				m.invoke(object, value);
			} else {
				Field f = (Field) fieldAccessor;
				f.set(object, value);
			}
			return;
		} catch (Throwable e) {
			e.printStackTrace();
		}

		throw new NoSuchFieldError(object.getClass() + "."
				+ fieldInfo.getName());
	}

	public void writeObject(DataOutput out, Object obj,
			FastArrayList objectStack) throws IOException {
		registerClass(obj.getClass());

		// write class header
		int classId = getClassId(obj.getClass());
		LongPacker.packInt(out, classId);
		ObjectClassInfo classInfo = registered.get(classId);

		if (classInfo.isExternalizable) {
			Externalizable o = (Externalizable) obj;
			DataInputOutput out2 = (DataInputOutput) out;
			try {
				out2.serializer = this;
				out2.objectStack = objectStack;
				o.writeExternal(out2);
			} finally {
				out2.serializer = null;
				out2.objectStack = null;
			}
			return;
		}

		if (classInfo.isEnum) {
			int ordinal = ((Enum) obj).ordinal();
			LongPacker.packInt(out, ordinal);
		}

		ObjectStreamField[] fields = getFields(obj.getClass());
		LongPacker.packInt(out, fields.length);

		for (ObjectStreamField f : fields) {
			// write field ID
			int fieldId = classInfo.getFieldId(f.getName());
			if (fieldId == -1) {
				// field does not exists in class definition stored in db,
				// propably new field was added so add field descriptor
				fieldId = classInfo.addFieldInfo(new FieldInfo(f, obj
						.getClass()));
				db.update(serialClassInfoRecid, (Serialization) this,
						db.defaultSerializationSerializer);
			}
			LongPacker.packInt(out, fieldId);
			// and write value
			Object fieldValue = getFieldValue(classInfo.getField(fieldId), obj);
			serialize(out, fieldValue, objectStack);
		}
	}

	public Object readObject(DataInput in, FastArrayList objectStack)
			throws IOException {
		// read class header
		try {
			int classId = LongPacker.unpackInt(in);
			ObjectClassInfo classInfo = registered.get(classId);
			// Class clazz = Class.forName(classInfo.getName());
			Class clazz = classIdToclass.get(classId);
			if (clazz == null)
				clazz = Class.forName(classInfo.getName());
			assertClassSerializable(clazz);

			Object o;

			if (classInfo.isEnum) {
				int ordinal = LongPacker.unpackInt(in);
				o = clazz.getEnumConstants()[ordinal];
			} else {
				o = createInstance(clazz, Object.class);
			}

			objectStack.add(o);

			if (classInfo.isExternalizable) {
				Externalizable oo = (Externalizable) o;
				DataInputOutput in2 = (DataInputOutput) in;
				try {
					in2.serializer = this;
					in2.objectStack = objectStack;
					oo.readExternal(in2);
				} finally {
					in2.serializer = null;
					in2.objectStack = null;
				}

			} else {
				int fieldCount = LongPacker.unpackInt(in);
				for (int i = 0; i < fieldCount; i++) {
					int fieldId = LongPacker.unpackInt(in);
					FieldInfo f = classInfo.getField(fieldId);
					Object fieldValue = deserialize(in, objectStack);
					setFieldValue(f, o, fieldValue);
				}
			}
			return o;
		} catch (Exception e) {
			throw new Error("Could not instanciate class", e);
		}

	}

	static private sun.reflect.ReflectionFactory rf = sun.reflect.ReflectionFactory
			.getReflectionFactory();

	private static Map<Class, Constructor> classToconstuctor = new HashMap<Class, Constructor>();

	private static <T> T createInstance(Class<T> clazz, Class<? super T> parent) {

		try {
			Constructor<T> intConstr = classToconstuctor.get(clazz);

			if (intConstr == null) {
				Constructor objDef = parent.getDeclaredConstructor();
				intConstr = rf.newConstructorForSerialization(clazz, objDef);
				classToconstuctor.put(clazz, intConstr);
			}

			return clazz.cast(intConstr.newInstance());
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("Cannot create object", e);
		}
	}

}
