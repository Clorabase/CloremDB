package db.clorabase.clorem;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Volume {
    private final File directory;
    private final String name;

    public Volume(File directory) {
        this.directory = directory;
        this.name = directory.getName();
    }

    public Volume volume(String name) {
        var vol = new File(directory, name);
        if (!vol.exists() && !vol.mkdir())
            throw new CloremDatabaseException("The volume could not be created. You might not have permission to create a directory");
        else
            return new Volume(vol);
    }

    /**
     * Delete the current volume and all the sub-contained volume along with all the data
     *
     * @throws CloremDatabaseException If volume cannot be deleted due to IO error
     */
    public void deleteVolume() {
        var files = directory.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (!file.delete())
                throw new CloremDatabaseException("Failed to delete volume because of IO error. You might not have permission to delete a file");
        }
        if (!directory.delete()) {
            throw new CloremDatabaseException("Failed to delete volume because of IO error. You might not have permission to delete a file");
        }
    }

    /**
     * Deletes a sub volume or an object.
     * @param key The key of the object or the name of the volume
     * @throws CloremDatabaseException if the volume or the object cannot be deleted due to IO error or does not exists.
     */
    public void delete(String key) {
        var obj = new File(directory, key);
        if (obj.exists()) {
            if (!obj.delete())
                throw new CloremDatabaseException("Failed to delete object because of IO error. You might not have permission to delete a file");
        } else
            throw new CloremDatabaseException("Object/Volume does not exist");
    }

    /**
     * Get an object from the current volume
     *
     * @param key The key of the object
     * @return The object
     */
    public synchronized Object fetch(@NonNull String key) {
        var obj = new File(directory, key);
        if (directory.isDirectory() && obj.exists())
            return CloremUtils.readObject(obj);
        else
            throw new CloremDatabaseException("Object does not exist");
    }

    public synchronized void insert(@NonNull CloremObject object) {
        var file = new File(directory, object.getKey());
        CloremUtils.writeObject(object, file);
    }


    /**
     * Update an object in the database. This method is used to update only required fields of the object.
     *
     * @param key         The key of the object
     * @param clazz       The class of the object
     * @param transaction The transition function which will be used to update the object
     * @param <T>         The type of the object
     */
    public synchronized <T extends CloremObject> void update(@NonNull String key, @NonNull Class<T> clazz, @NonNull Consumer<T> transaction) {
        var obj = clazz.cast(fetch(key));
        transaction.accept(obj);
        Clorem.update(obj);
    }

    public List<? extends CloremObject> listObjects() {
        var files = directory.listFiles(File::isFile);
        if (files == null)
            return new ArrayList<>();

        var list = new ArrayList<CloremObject>();
        for (File file : files) {
            var obj = fetch(file.getName());
            list.add((CloremObject) obj);
        }
        return list;
    }

    public <T extends CloremObject> Query<T> query(Class<T> tClass){
        return new Query<T>(directory,tClass);
    }
}
