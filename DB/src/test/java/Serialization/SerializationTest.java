package Serialization;

import java.io.IOException;
import java.util.ArrayList;

import Serializer.ClassMeta;
import Serializer.SerializeAll;
import junit.framework.TestCase;

public class SerializationTest extends TestCase {
	SerializeAll ser;
	
	ArrayList<ClassMeta> registered;

	public SerializationTest() throws Exception {
		String  filename = "storeFile1";
		ser = new SerializeAll(registered, filename);
	}

	public void testInt() throws IOException, ClassNotFoundException {
		int[] vals = { Integer.MIN_VALUE, -Short.MIN_VALUE * 2,
				-Short.MIN_VALUE + 1, -Short.MIN_VALUE, -10, -9, -8, -7, -6,
				-5, -4, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 127, 254, 255,
				256, Short.MAX_VALUE, Short.MAX_VALUE + 1, Short.MAX_VALUE * 2,
				Integer.MAX_VALUE };
		for (int i : vals) {
			byte[] buf = ser.serialize(i);
			Object l2 = ser.deserialize(buf);
			assertTrue(l2.getClass() == Integer.class);
			assertEquals(l2, i);
		}
	}

	public void testClass() throws IOException, ClassNotFoundException {
		byte[] buf = ser.serialize(String.class);
		Class l2 = (Class) ser.deserialize(buf);
		assertEquals(l2, String.class);
	}
	
	public  void  testClass1() throws  IOException,  ClassNotFoundException {
		
		byte[]  buf = ser.serialize(Person.class);
		Class  l2 = (Class)ser.deserialize(buf);
		
		assertEquals(l2, Person.class);
		
	}
	
	
	
	

}
