package Serializer;

import java.io.*;
import java.util.ArrayList;

/**
 * An alternative to <code>java.io.ObjectOutputStream</code> which uses more efficient serialization
 */
public class ObjectOutputStream2 extends DataOutputStream implements ObjectOutput {

    public ObjectOutputStream2(OutputStream out) {
        super(out);
    }

    public void writeObject(Object obj) throws IOException {
        ArrayList registered = new ArrayList();
        SerializeAll ser = new SerializeAll();

        byte[] data = ser.serialize(obj);
        //write class info first
        SerializeClass.serializer.serialize(this, registered);
        //and write data
        write(data);
    }
}
