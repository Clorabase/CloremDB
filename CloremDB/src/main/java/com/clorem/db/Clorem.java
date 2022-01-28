package com.clorem.db;

import android.content.Context;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 Clorem is an android open-source no-SQL database. This is similar to firebase's real-time database
 with only of local database support.

 * @author Rahil khan
 * @since 12-10-2021
 */
public class Clorem {
    protected static ArrayList<JSONObject> parents = new ArrayList<>();
    protected static ArrayList<String> parentsNames = new ArrayList<>();
    protected static boolean encryption;
    private static Clorem instance;
    protected static File DATABASE_FILE;

    /**
     * Use static {@link Clorem#getInstance(Context, String) method}
     */
    private Clorem(){}

    /**
     * This will initialize your database. Initialization may take time if there is too much data stored in database.
     * If you feel that it is making UI unresponsive, then put this in another thread.
     * Remember : This method is singleton and runs synchronously. Calling this second time will return the same database instance
     * doesn't matter which database name you have passed.
     * @param context The context of the activity
     * @param databaseName The name of the database you want to open or create.
     * @return instance of the class
     */
    public static synchronized Clorem getInstance(Context context, String databaseName){
        new InetSocketAddress(8000);
        if (instance == null){
            instance = new Clorem();
            File db = new File("/data/data/" + context.getPackageName() + "/databases",databaseName + ".json");
            try {
                if (!db.exists() && !db.createNewFile())
                    throw new CloremDatabaseException("Unknown Error occured while creating file for " + databaseName,Reasons.REASONS_DATABASE_CREATION_ERROR);

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
     * @return The root node of the database.
     */
    public Node getDatabase(){
        parentsNames.add("root");
        return new Node(DATABASE_FILE);
    }


    /**
     * Sets if the data should be encrypted before commenting to the database. Default value is false
     * @param enabled true to enable encryption, ignore otherwise.
     */
    public Clorem enableEncoding(boolean enabled){
        encryption = enabled;
        return this;
    }


    protected static synchronized void commit(JSONObject root) {
        try {
            String json = root.toString(3);
            if (encryption)
                json = Base64.encodeToString(json.getBytes(),Base64.DEFAULT);
            PrintWriter writer = new PrintWriter(DATABASE_FILE);
            writer.write(json);
            writer.close();
            instance = null;
            System.gc();
        } catch (FileNotFoundException | JSONException e) {
            throw new CloremDatabaseException("Database missing !, database files have been deleted or moved",Reasons.REASONS_DATABASE_CORRUPTED);
        }
    }


    /**
     * Preview your database structure in the form of JSON.
     * @return A JSON string.
     */
    public String getDatabaseAsJson(){
        try {
            return getDatabase().root.toString(3);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new CloremDatabaseException("Database is corrupted and cannot be parsed into json",Reasons.REASONS_DATABASE_CORRUPTED);
        }
    }


    /**
     * Deletes this database. If the database is currently open or the app is reading or writing to database, deletion may fail.
     */
    public void delete(){
        DATABASE_FILE.delete();
    }
}
