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
 * <p>
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
     *
     * @param databaseDir The directory where the database will be stored on the disk. This directory must be writable.
     * @return The singleton instance of the Clorem database
     */
    public static Clorem getInstance(File databaseDir) {
        if (INSTANCE == null) {
            INSTANCE = new Clorem();
            if (databaseDir.canRead() && databaseDir.canWrite())
                Clorem.databaseDir = databaseDir;
            else
                throw new RuntimeException(new NoPermissionException("You do not have permission to read/write on this directory"));
        }
        return INSTANCE;
    }

    /**
     * Get's the instance of the named volume, create if not already exists
     *
     * @param name The name of the volume
     * @throws CloremDatabaseException If volume cannot be created due to IO error
     */
    public Volume volume(@NonNull String name) {
        var vol = new File(databaseDir, name);
        boolean created = vol.mkdir();
        if (!vol.exists() && !vol.mkdir())
            throw new CloremDatabaseException("The volume could not be created. You might not have permission to create a directory");

        return new Volume(vol);
    }


    /**
     * Put an object in the database. The object must implement the {@link CloremObject} interface.
     *
     * @param object The object to be saved
     * @param <T>    The type of the object
     */
    public synchronized <T extends CloremObject> void insert(@NonNull T object) {
        var path = object.getVolume().trim().replace('.','/');
        var volume = new File(databaseDir, path);
        if (!volume.exists() && !volume.mkdir())
            throw new RuntimeException("The volume could not be created. You might not have permission to create a directory");
        else
            CloremUtils.writeObject(object, new File(volume, object.getKey()));
    }



    /**
     * Update an object in the database. Only use this method when you want to update whole object.
     * If you want to update only a few fields of the object, use the {@link #update(String, String, Class, Consumer)} method.
     * The object id must be the same as the object id of the object in the database.
     *
     * @param object The object to be updated
     * @param <T>    The type of the object
     */
    protected static synchronized <T extends CloremObject> void update(@NonNull T object) {
        var path = object.getVolume().trim().replace('.','/');
        var volume = new File(databaseDir, path);
        var file = new File(volume, object.getKey());
        if (volume.isDirectory() && file.exists()) {
            CloremUtils.writeObject(object, file);
        } else
            throw new IllegalArgumentException("The volume or the object does not exist");
    }
}
