package Serializer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.util.ArrayList;
/**
 * An alternative to <code>java.io.ObjectInputStream</code> which uses more efficient serialization
 */
public class ObjectInputStream2 extends DataInputStream implements ObjectInput {


    public ObjectInputStream2(InputStream in) {
        super(in);
    }

    public Object readObject() throws ClassNotFoundException, IOException {
        //first read class data
        ArrayList<ObjectClassInfo> info = SerialClassInfo1.serializer.deserialize(this);

        Serialization ser = new Serialization();
        return ser.deserialize(this);
    }
}
