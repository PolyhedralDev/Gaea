package org.polydev.gaea.util;

import org.polydev.gaea.serial.MovedObjectInputStream;

import java.io.*;

public class SerializationUtil {
    public static Object fromFile(File f) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new MovedObjectInputStream(new FileInputStream(f), "com.dfsek.betterend.gaea", "org.polydev.gaea"); // Backwards compat with old BetterEnd shade location
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    public static void toFile(Serializable o, File f) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(o);
        oos.close();
    }
}
