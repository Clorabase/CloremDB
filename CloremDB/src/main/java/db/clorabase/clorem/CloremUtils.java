package db.clorabase.clorem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;

public class CloremUtils {

    protected static void writeObject(Object object, File file){
        try {
            var os = new ObjectOutputStream(new FileOutputStream(file));
            os.writeObject(object);
            os.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    protected static Object readObject(File file){
        try {
            var in = new ObjectInputStream(new FileInputStream(file));
            var object = in.readObject();
            in.close();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected static List<Object> listObjects(File volume){
        var files = volume.listFiles();
        if (files == null)
            throw new RuntimeException("The volume does not exist");
        else {
            var objects = new Object[files.length];
            for (int i = 0; i < files.length; i++) {
                objects[i] = readObject(files[i]);
            }
            return Arrays.asList(objects);
        }
    }
}
