package com.clorem.db;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Clorem is an android open-source and free no-sql database. This is similar to firebase realtime database or you can say that it is almost
 * the same with extra support of local database. This database stores data in JSON tree and supports all types of primitive data
 * except short & float. It also support {@link java.util.ArrayList<String>} and {@link java.util.ArrayList<Integer> lists}.
 *
 * @author Rahil khan
 * @since 12-10-2021
 */
public class Clorem {

    private static Clorem instance;
    public static File DATABASE_FILE;

    /**
     * Use static {@link Clorem#getInstance(Context, String) method}
     */
    private Clorem(){}

    /**
     * This will initialize your database. Initialization may take time if there is too much data stored in database.
     * If you feel that it is making UI unresponsive, then put this in another thread.
     * Remember : This method runs synchronously.
     * @param context The context of the activity
     * @param databaseName The name of the database you want to open or create.
     * @return instance of the class
     */
    public static synchronized Clorem getInstance(Context context, String databaseName){
        if (instance == null){
            instance = new Clorem();
            File directory = new File(context.getFilesDir(),"db");
            if (!directory.exists() && !directory.mkdir()) {
                throw new CloremDatabaseException("Database cannot be created for some reason",Reasons.REASONS_DATABASE_CREATION_ERROR);
            }
            File db = new File(directory,databaseName + ".json");
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
     * The database which is initialized in the beginning while creating instance of this class.
     * This will begin from root node.
     * @return The Database class through which you can perform CRUD operations.
     */
    public Database getDatabase(){
        return new Database(DATABASE_FILE);
    }
}
