package Serializer;

import java.io.ObjectStreamField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ClassMeta {
	private final String name;
	public final List<FieldMeta> fields = new ArrayList<FieldMeta>();
	private final Map<String, FieldMeta> nameToFieldInfo = new HashMap<String, FieldMeta>();
	private final Map<String, Integer> nameToFieldId = new HashMap<String, Integer>();
	final boolean isEnum;
	final boolean isExternalizable;
	private ObjectStreamField[] objectStreamFields;
	ClassMeta(final String name, final FieldMeta[] fields,
			final boolean isEnum, final boolean isExternalizable) {
		this.name = name;
		this.isEnum = isEnum;
		this.isExternalizable = isExternalizable;
		for (FieldMeta f : fields) {
			this.nameToFieldId.put(f.getName(), this.fields.size());
			this.fields.add(f);
			this.nameToFieldInfo.put(f.getName(), f);

		}

	}

	public String getName() {
		return name;
	}

	public FieldMeta[] getFields() {
		return (FieldMeta[]) fields.toArray();
	}

	public FieldMeta getField(String name) {
		return nameToFieldInfo.get(name);
	}

	public int getFieldId(String name) {
		Integer fieldId = nameToFieldId.get(name);
		if (fieldId != null) {
			return fieldId;
		}
		return -1;
	}

	public FieldMeta getField(int serialId) {
		return fields.get(serialId);
	}
    public ObjectStreamField[] getObjectStreamFields() {
		return objectStreamFields;
	}

	public void setObjectStreamFields(ObjectStreamField[] objectStreamFields) {
		this.objectStreamFields = objectStreamFields;
	}
	
	public  int  addFieldInfo(FieldMeta field){
		nameToFieldId.put(field.getName(), fields.size());
		nameToFieldInfo.put(field.getName(), field);
		fields.add(field);
		return   fields.size() - 1;
	}

	

}
