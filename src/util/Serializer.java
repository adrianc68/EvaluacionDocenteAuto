package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {
    public static final String DATA_ANSWER_SELECTED_PATH = "configuration/seg/hoja_seguimiento.data";
    public static final String DATA_QUESTIONS_PATH = "configuration/seg/preguntas.data";

    public static Object unSerializeObject(String path) throws IOException, ClassNotFoundException {
        Object object;
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path)) ) {
            object = objectInputStream.readObject();
        } catch(IOException | ClassNotFoundException ioe) {
            throw ioe;
        }
        return object;
    }

    public static void serializeObject(Object object, String path) throws IOException {
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream( new FileOutputStream(path) ) ){
            objectOutputStream.writeObject(object);
        } catch(IOException ioe) {
            throw ioe;
        }
    }

}
