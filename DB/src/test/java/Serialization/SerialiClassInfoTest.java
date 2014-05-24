package Serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;

import Serializer.ClassMeta;
import Serializer.SerializeClass;
import Serializer.SerializeAll;
import junit.framework.TestCase;

public class SerialiClassInfoTest extends TestCase {

	static class Bean1 implements Serializable {

		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			Bean1 bean1 = (Bean1) o;

			if (Double.compare(bean1.doubleField, doubleField) != 0)
				return false;
			if (Float.compare(bean1.floatField, floatField) != 0)
				return false;
			if (intField != bean1.intField)
				return false;
			if (longField != bean1.longField)
				return false;
			if (field1 != null ? !field1.equals(bean1.field1)
					: bean1.field1 != null)
				return false;
			if (field2 != null ? !field2.equals(bean1.field2)
					: bean1.field2 != null)
				return false;

			return true;
		}

		protected String field1 = null;
		protected String field2 = null;

		protected int intField = Integer.MAX_VALUE;
		protected long longField = Long.MAX_VALUE;
		protected double doubleField = Double.MAX_VALUE;
		protected float floatField = Float.MAX_VALUE;

		transient int getCalled = 0;
		transient int setCalled = 0;
      
		public String getField2() {
			getCalled++;
			return field2;
		}

		public void setField2(String field2) {
			setCalled++;
			this.field2 = field2;
		}
     
		Bean1(String field1, String field2) {
			this.field1 = field1;
			this.field2 = field2;
		}

		Bean1() {
		}
	}

	static class Bean2 extends Bean1 {

		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			if (!super.equals(o))
				return false;

			Bean2 bean2 = (Bean2) o;

			if (field3 != null ? !field3.equals(bean2.field3)
					: bean2.field3 != null)
				return false;

			return true;
		}

		@Override
		public int hashCode() {
			return field3 != null ? field3.hashCode() : 0;
		}

		private String field3 = null;

		Bean2(String field1, String field2, String field3) {
			super(field1, field2);
			this.field3 = field3;
		}

		Bean2() {
		}
	}

	SerializeClass s;
	ArrayList<ClassMeta> registered = new ArrayList<ClassMeta>();

	Bean1 b = new Bean1("aa", "bb");
	Bean2 b2 = new Bean2("aa", "bb", "cc");

	public void testGetFieldValue1() throws Exception {
		String filename = "storeFile1"; 
		s = new SerializeAll(registered);
		assertEquals("aa", s.getFieldValue("field1", b));

	}

	public void testGetFieldValue2() throws Exception {
		s = new SerializeAll(registered);
		assertEquals("bb", s.getFieldValue("field2", b));
		assertEquals(1, b.getCalled);
	}

	public void testGetFieldValue3() throws Exception {
		s = new SerializeAll(registered);
		assertEquals("aa", s.getFieldValue("field1", b2));
	}

	public void testGetFieldValue4() throws Exception {
		s = new SerializeAll(registered);
		assertEquals("bb", s.getFieldValue("field2", b2));
		assertEquals(1, b2.getCalled);
	}

	public void testGetFieldValue5() throws Exception {
		s = new SerializeAll(registered);
		assertEquals("cc", s.getFieldValue("field3", b2));
	}

	/*
	 * 
	 * <E> E serialize(E e) throws ClassNotFoundException, IOException {
	 * Serialization s2 = new Serialization(); ByteArrayOutputStream out = new
	 * ByteArrayOutputStream(); s2.serialize(new DataOutputStream(out), e);
	 * 
	 * ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
	 * return (E) s2.deserialize(new DataInputStream(in));
	 * 
	 * }
	 * 
	 * public void testRecursion() throws Exception { AbstractMap.SimpleEntry b
	 * = new AbstractMap.SimpleEntry("abcd", null); b.setValue(b.getKey());
	 * 
	 * AbstractMap.SimpleEntry bx = serialize(b); assertEquals(bx, b); assert
	 * (bx.getKey() == bx.getValue());
	 * 
	 * }
	 */
}
