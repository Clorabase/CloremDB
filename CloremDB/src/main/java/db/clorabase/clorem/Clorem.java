package db.clorabase.clorem;

import org.java.json.JSONException;
import org.java.json.JSONObject;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

/**
 * Clorem is an android open-source no-SQL database. This is similar to firebase's real-time database
 * with only of local database support.
 *
 * @author Rahil khan
 * @since 12-10-2021
 */
public class Clorem {
    private static Clorem instance;
    protected static File DATABASE_FILE;

    /**
     * Use static {@link Clorem#getInstance(File, String) method} to get the instance of the database.
     * Using this constructor will result in {@code NullPointerException}
     */
    private Clorem() {

    }

    /**
     * This will initialize your database. Initialization may take time if there is too much data stored in database.
     * If you feel that it is making UI unresponsive, then put this in another thread.
     * Remember : This method is singleton and runs synchronously. Calling this second time will return the same database instance
     * doesn't matter which database name you have passed.
     *
     * @param directory      The directory where the database will be stored.
     * @param databaseName The name of the database you want to open or create.
     * @return instance of the class
     */
    public static synchronized Clorem getInstance(File directory, String databaseName) {
        if (instance == null) {
            instance = new Clorem();
            File db = new File(directory, databaseName + ".db");
            try {
                if (!db.exists() && !db.createNewFile())
                    throw new CloremDatabaseException("Unknown Error occurred while creating file for " + databaseName, Reasons.REASONS_DATABASE_CREATION_ERROR);

                DATABASE_FILE = db;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Gets the main database class through which you can perform any CRUD operations on the database.
     * This database is like a JSON tree which contain nodes and mappings.
     *
     * @return The root node of the database.
     */
    public synchronized Node getDatabase() {
        try {
            ObjectInputStream in = new ObjectInputStream(new InflaterInputStream(new FileInputStream(DATABASE_FILE)));
            Map<String,?> map = (Map) in.readObject();
            in.close();
            return new Node(map);
        } catch (IOException | ClassNotFoundException e) {
            if (e instanceof EOFException || e instanceof ZipException)
                return new Node(new HashMap<>());
            else
                throw new CloremDatabaseException("Database may be corrupted or has been deleted", Reasons.REASONS_DATABASE_CORRUPTED);
        }
    }

    protected static synchronized void commit(JSONObject root) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new DeflaterOutputStream(new FileOutputStream(DATABASE_FILE)));
            os.writeObject(root.toMap());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Unknown Error occurred while writing to database", Reasons.REASONS_UNKNOWN);
        }
    }


    /**
     * Preview your database structure in the form of JSON.
     *
     * @return A human readable JSON string.
     */
    public String getDatabaseAsJson() {
        try {
            return getDatabase().root.toString(3);
        } catch (JSONException e) {
            throw new CloremDatabaseException("Database is corrupted or deleted", Reasons.REASONS_DATABASE_CORRUPTED);
        }
    }


    /**
     * Deletes this database. If the database is currently open or the app is reading or writing to database, deletion may fail.
     */
    public void delete() {
        DATABASE_FILE.delete();
    }
}
