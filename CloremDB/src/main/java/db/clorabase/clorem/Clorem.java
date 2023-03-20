package db.clorabase.clorem;

import androidx.annotation.NonNull;

import java.io.File;
import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.naming.NoPermissionException;

/**
 * Clorem is a simple, easy to use, and fast database engine for java applications.
 * It is a no-sql database which can save POJO's directly. You can organise your data
 * in volumes. Each volume is like a SQL table which contains the objects as rows. You can create
 * as many volumes as you want. You can also create as many objects as you want in
 * each volume. The only limitation is that the objects implement the {@link CloremObject}
 *
 * All the methods are thread safe. You can use the database in a multi-threaded environment.
 *
 * @author author Rahil khan
 * @version 1.0
 * @since 1.0
 */
public class Clorem {
    private static Clorem INSTANCE;
    private static File databaseDir;

    /**
     * This method returns the singleton instance of the Clorem database.
     * @param databaseDir The directory where the database will be stored on the disk. This directory must be writable.
     * @return The singleton instance of the Clorem database
     */
    public static Clorem getInstance(File databaseDir) {
        if (INSTANCE == null){
            INSTANCE = new Clorem();
            if (databaseDir.canRead() && databaseDir.canWrite())
                Clorem.databaseDir = databaseDir;
            else
                throw new RuntimeException(new NoPermissionException("You do not have permission to read/write on this directory"));
        }
        return INSTANCE;
    }

    /**
     * This method creates a new volume in the database.
     * @param name The name of the volume
     */
    public void createVolume(@NonNull String name){
        new File(databaseDir,name).mkdir();
    }

    /**
     * This method deletes a volume from the database. All the objects in the volume will be deleted.
     * @param name The name of the volume
     */
    public void deleteVolume(@NonNull String name){
        var volume = new File(databaseDir,name);
        var files = volume.listFiles();
        for (File file : files) {
            file.delete();
        }
        volume.delete();
    }

    /**
     * Put an object in the database. The object must implement the {@link CloremObject} interface.
     * @param object The object to be saved
     * @param <T> The type of the object
     */
    public synchronized <T extends CloremObject> void put(@NonNull T object){
        var volume = new File(databaseDir,object.getVolume());
        if (!volume.exists() && !volume.mkdir())
            throw new RuntimeException("The volume could not be created. You might not have permission to create a directory");
        else
            CloremUtils.writeObject(object,new File(volume,object.getKey()));
    }

    /**
     * Get an object from the database.
     * @param key The key of the object
     * @param volume The volume in which the object is stored
     * @return The object
     */
    public synchronized Object get(@NonNull String key,@NonNull String volume){
        var volumeDir = new File(databaseDir,volume);
        var obj = new File(volumeDir,key);
        if (volumeDir.isDirectory() && obj.exists())
            return CloremUtils.readObject(obj);
        else
            throw new IllegalArgumentException("The volume or the object does not exist");
    }

    /**
     * Update an object in the database. Only use this method when you want to update whole object.
     * If you want to update only a few fields of the object, use the {@link #update(String, String, Class, Consumer)} method.
     * The object id must be the same as the object id of the object in the database.
     * @param object The object to be updated
     * @param <T> The type of the object
     */
    public synchronized <T extends CloremObject> void update(@NonNull T object){
        var volume = new File(databaseDir,object.getVolume());
        var file = new File(volume,object.getKey());
        if (volume.isDirectory() && file.exists()){
            CloremUtils.writeObject(object,file);
        } else
            throw new IllegalArgumentException("The volume or the object does not exist");
    }

    /**
     * Update an object in the database. This method is used to update only required fields of the object.
     * @param key The key of the object
     * @param volume The volume in which the object is stored
     * @param clazz The class of the object
     * @param transition The transition function which will be used to update the object
     * @param <T> The type of the object
     */
    public synchronized <T extends CloremObject> void update(@NonNull String key, @NonNull String volume,@NonNull Class<T> clazz,@NonNull Consumer<T> transition){
        var obj = clazz.cast(get(key,volume));
        transition.accept(obj);
        if (key.equals(obj.getKey()) && volume.equals(obj.getVolume()))
            update(obj);
        else {
            delete(key,volume);
            put(obj);
        }
    }

    /**
     * Delete an object from the database.
     * @param key The key of the object
     * @param volume The volume in which the object is stored
     */
    public synchronized void delete(@NonNull String key,@NonNull String volume){
        var volumeDir = new File(databaseDir,volume);
        var obj = new File(volumeDir,key);
        if (volumeDir.isDirectory() && obj.exists())
            obj.delete();
        else
            throw new IllegalArgumentException("The volume or the object does not exist");
    }

    /**
     * Query the database. This method returns a list of objects which satisfy the condition.
     * @param volume The volume in which the objects are stored
     * @param clazz The class of the objects
     * @param condition The condition which the objects must satisfy. Returns true if the object is to be included in the list.
     * @param <T> The type of the objects
     * @return A list of objects which satisfy the condition
     */
    public <T extends CloremObject> List<T> query(@NonNull String volume,@NonNull Class<T> clazz, @NonNull Predicate<T> condition){
        var files = new File(databaseDir,volume).listFiles();
        if (files == null)
            throw new RuntimeException("The volume does not exist");
        else {
            var list = new ArrayList<T>();
            for (File file : files) {
                var obj = get(file.getName(),volume);
                try {
                    if (condition.test(clazz.cast(obj)))
                        list.add(clazz.cast(obj));
                } catch (ClassCastException ignored){}
            }
            return list;
        }
    }

    /**
     * List all the objects in a volume.
     * @param volume The volume in which the objects are stored
     * @return A list of objects
     */
    public List<Object> listObjects(@NonNull String volume){
        return CloremUtils.listObjects(new File(databaseDir,volume));
    }
}
