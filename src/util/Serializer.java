package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Serializer {
    private static final String DATA_PATH = "seg/hoja_seguimiento.data";

    public static Object unSerializeObject() {
        Object object = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(DATA_PATH);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            object = objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } catch(ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return object;
    }

    public static boolean serializeObject(Object object) {
        try {
            FileOutputStream fos = new FileOutputStream(DATA_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }

}
