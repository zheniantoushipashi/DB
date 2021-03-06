package DBengine;

import java.io.Serializable;
import java.util.ArrayList;

import DataStructure.BTree1;
import Serializer.DataInputOutput;
import Serializer.ClassMeta;
import Serializer.SerializeClass;
import Serializer.SerializeAll;
import Serializer.Serializer;
import junit.framework.TestCase;

public class DatabaseTest extends TestCase {

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
         /*  
		public String getField2() {
			getCalled++;
			return field2;
		}

		public void setField2(String field2) {
			setCalled++;
			this.field2 = field2;
		}
      */
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
  
	SerializeAll s;
	

	public void testInsert() throws Exception {
		Bean1 b1 = new Bean1("孙彪彪", "潘苗苗苗");
		String filename = "storeFile5";
		Database database = new Database(filename);
		DataInputOutput inbuf = new DataInputOutput(100);
		DataInputOutput outbuf = new DataInputOutput(100);
		@SuppressWarnings("unchecked")
		long recid = database.insert(b1, database.serializer, inbuf);
		@SuppressWarnings("unchecked")
		Bean1 b2 = (Bean1) database.fetch(recid, database.serializer, outbuf);
		assertEquals(b1 , b2);
	}

}
